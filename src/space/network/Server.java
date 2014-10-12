package space.network;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.lwjgl.Sys;

import space.math.Vector2D;
import space.network.message.DisconnectMessage;
import space.network.message.DropPickupMessage;
import space.network.message.EntityMovedMessage;
import space.network.message.InteractionMessage;
import space.network.message.JumpMessage;
import space.network.message.Message;
import space.network.message.PlayerJoiningMessage;
import space.network.message.EntityRotationMessage;
import space.network.message.TextMessage;
import space.network.message.TransferMessage;
import space.network.storage.WorldLoader;
import space.network.storage.WorldSaver;
import space.network.message.ShutdownMessage;
import space.world.Container;
import space.world.Door;
import space.world.Entity;
import space.world.Pickup;
import space.world.Player;
import space.world.Room;
import space.world.World;

/**
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class Server {
	
	/**
	 * The time between when the server saves the game. Set to 5 minutes.
	 */
	private static final int TIME_BETWEEN_SAVES = 300000;
	
	private Thread connectionHandler;
	private Thread gameLoop;
	private Thread saveRoutine;
	
	private boolean stillAlive;
	private ServerSocket socket;
	private Map<Integer, Connection> connections;
	
	private World world;
	private Map<Integer, Player> inactivePlayers;
	
	private Set<Integer> usedIds;
	private Random idGenerator;
	
	private WorldSaver saver;
	private String savePath;
	
	public Server(String host, int port, WorldLoader loader, WorldSaver saver, String savePath){
		//Create the list of client connections
		connections = new HashMap<Integer, Connection>();
		inactivePlayers = new HashMap<Integer, Player>();
		
		//Create the socket for clients to connect to
		try {
			socket = new ServerSocket(port, 50, InetAddress.getByName(host));
		} catch (IOException e) {
			//Throw the exception again as it is a critical failure
			throw new RuntimeException(e);
		}
		
		//Keep track of how to save the world
		this.saver = saver;
		this.savePath = savePath;
		
		//Create set of used IDs
		usedIds = new HashSet<Integer>();
		idGenerator = new Random();
		
		//Create Connection Handler
		stillAlive = true;
		connectionHandler = new Thread(new ConnectionHandler());
		
		//Make sure not to have the default world overwritten
		if (savePath.equals("default_world")){
			savePath += "2";
		}
		
		//Load the World
		try {
			loader.loadWorld(savePath);
		} catch (Exception e){
			//If something went wrong assume the file didn't exist
			loader.loadWorld("default_world");
		}
		world = loader.getWorld();
		//Mark the IDs of all the entities in the world as used
		for (Room r : world.getRooms().values()){
			for (Entity e : r.getEntities()){
				usedIds.add(e.getID());
			}
		}
		//Load previous players
		for (Player p : loader.getPlayers()){
			inactivePlayers.put(p.getID(), p);
			usedIds.add(p.getID());
			//Mark the IDs of the contents of the players inventory as used
			for (Pickup pu : p.getInventory()){
				usedIds.add(((Entity) pu).getID());
			}
		}
		
		//Start accepting connections
		connectionHandler.start();
		
		//Start the game logic
		gameLoop = new Thread(new ServerGameLoop());
		gameLoop.start();
		
		//Start
		saveRoutine = new Thread(new WorldSaveRoutine());
		saveRoutine.start();
	}
	
	public void shutdown(){
		//Stop the connection handler
		stillAlive = false;
		connectionHandler.interrupt();
		saveRoutine.interrupt();
		
		//Save the state of the world
		saver.saveWorld(savePath, world, new ArrayList<Player>(inactivePlayers.values()));
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized (connections) {
			for (Connection c : connections.values()){
				try {
					c.sendMessage(new ShutdownMessage());
				} catch (IOException e) {
				}
				c.close();
			}
		}
	}
	
	private void handleDisconnect(int disconnectedID){
		Player p = (Player) world.getEntity(disconnectedID);
		
		//Remove player from world
		world.getRoomAt(p.getPosition()).removeFromRoom(p);
		//world.removeEntity(p); TODO add world.removeEntity(Entity e)
		
		//Keep track of the player allowing for reconnects
		inactivePlayers.put(disconnectedID, p);
		
		//Close the connection to the client
		connections.get(disconnectedID).close();
		
		synchronized (connections) {
			connections.remove(disconnectedID);
		}
		
		Map<Integer,Connection> connections = new HashMap<Integer, Connection>();
		synchronized (connections) {
			connections.putAll(this.connections);
		}
		//Tell the other clients that the player has disconnected
		DisconnectMessage disconnect = new DisconnectMessage(disconnectedID);
		for (Map.Entry<Integer, Connection> con : connections.entrySet()){
			try {
				con.getValue().sendMessage(disconnect);
			} catch (IOException e) {
				handleDisconnect(con.getKey());
			}
		}
	}
	
	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	private void sendMessageToAllExcept(int connectionID, Message message){
		for (Map.Entry<Integer, Connection> cons : connections.entrySet()){
			if (cons.getKey() != connectionID){
				try {
					cons.getValue().sendMessage(message);
				} catch (IOException e) {
					handleDisconnect(cons.getKey());
				}
			}
		}
	}
	
	/**
	 * 
	 * @author James Greenwood-Thessman (300289004)
	 */
	private class ServerGameLoop implements Runnable {
		
		@Override
		public void run() {
			long last = getTime();
			while (stillAlive){
				long now = getTime();
				int delta = (int) (now - last);
				
				Map<Integer,Connection> connections = new HashMap<Integer, Connection>();
				synchronized (connections) {
					connections.putAll(Server.this.connections);
				}
				for (Map.Entry<Integer, Connection> cons : connections.entrySet()){
					Connection con = cons.getValue();
					int id = cons.getKey();
					try {
						while (!con.isClosed() && con.hasMessage()){
							Message message = con.readMessage();

							//Handle disconnected players
							if (message instanceof DisconnectMessage){
								DisconnectMessage playerDisconnected = (DisconnectMessage) message;
								handleDisconnect(playerDisconnected.getPlayerID());
							//If an entity moved
							} else if (message instanceof EntityMovedMessage){
								EntityMovedMessage entityMoved = (EntityMovedMessage) message;
								Entity e = world.getEntity(entityMoved.getEntityID());
								
								world.moveCharacter((Player) e, entityMoved.getNewPosition());
								if (e.getPosition().equals(entityMoved.getNewPosition(), 0.1f)){
									//Forward the message to all the other clients
									sendMessageToAllExcept(id, message);
								}
							//If an entity rotated
							} else if (message instanceof EntityRotationMessage){
								EntityRotationMessage playerRotated = (EntityRotationMessage) message;
								Player p = (Player) world.getEntity(playerRotated.getID());

								p.setXRotation(playerRotated.getXRotation());
								p.setYRotation(playerRotated.getYRotation());

								//Forward the message to all the other clients
								sendMessageToAllExcept(id, message);
							//If a player jumped
							} else if (message instanceof JumpMessage){
								JumpMessage thePlayerWhoJumps = (JumpMessage) message;
								
								//Make the player jump
								((Player) world.getEntity(thePlayerWhoJumps.getPlayerID())).jump();
								
								//Forward the message to all the other clients
								sendMessageToAllExcept(id, message);
							//If a player dropped an entity
							} else if (message instanceof DropPickupMessage){
								DropPickupMessage drop = (DropPickupMessage) message;
								Player p = (Player) world.getEntity(drop.getPlayerId());
								Entity e = world.getEntity(drop.getPickupId());
								
								world.dropEntity(p, e, drop.getPosition());
								
								sendMessageToAllExcept(id, message);
							//If a player interacted with an entity
							} else if (message instanceof InteractionMessage){
								InteractionMessage interaction = (InteractionMessage) message;
								
								//Get the entities involved
								Entity e = world.getEntity(interaction.getEntityID());
								Player p = (Player) world.getEntity(interaction.getPlayerID());
								
								//Make them interact
								boolean succesful = e.interact(p, world);
								
								//If the interaction succeeded, forward the message
								if (succesful){
									sendMessageToAllExcept(id, message);
								}
							} else if (message instanceof TransferMessage){
								TransferMessage transfer = (TransferMessage) message;
								
								//Get the entities involved
								Entity e = world.getEntity(transfer.getEntityID());
								Player p = (Player) world.getEntity(transfer.getPlayerID());
								Container c = (Container) world.getEntity(transfer.getContainerID());

								//If from the player
								if (transfer.fromPlayer()){
									if (p.getInventory().contains(e) && c.canPutInside(e)){
										//Transfer the entity to the container
										p.getInventory().remove(e);
										c.putInside(e);

										//Forward the message to the other clients
										sendMessageToAllExcept(id, message);
									}
								//Other from the container
								} else {
									if (c.getItemsContained().contains(e)){
										//Transfer the entity to the player
										c.removeContainedItem(e);
										p.pickup(e);

										//Forward the message to the other clients
										sendMessageToAllExcept(id, message);
									}
								}
							}
						}
					} catch (IOException e) {
						handleDisconnect(cons.getKey());
					}
				}
				
				//Update world
				world.update(delta);
				
				last = now;
				
				//Sleep, iterating over the loop roughly 60 times a second
				try {
					Thread.sleep(17);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	/**
	 * 
	 * @author James Greenwood-Thessman (300289004)
	 */
	private class ConnectionHandler implements Runnable {
		
		@Override
		public void run() {
			Socket socketConnection;
			Connection newClient;
			while(stillAlive){
				try {
					socketConnection = socket.accept();
					
					//Create the connection for the new client
					newClient = new Connection(socketConnection);
					//Get the previous ID of the client
					int id = ((PlayerJoiningMessage) newClient.readMessage()).getPlayerID();
					
					Player p = null;
					
					//If the client already has an ID
					if (id != -1){
						//Retrieve the player
						p = inactivePlayers.remove(id);
						
						//If no such player, ensure a new ID is assigned
						if (p == null){
							id = -1;
						}
					}
					
					//If the client didn't have an ID previously
					if (id == -1){
						//Assign a new ID
						while (usedIds.contains(id = idGenerator.nextInt(1000)));
						usedIds.add(id);
						p = new Player(new Vector2D(0, 0), id, "Player"); //TODO use name
					}
					
					//Add the client the map of connections
					synchronized (connections) {
						connections.put(id, new Connection(socketConnection));
					}
					
					synchronized (world){
						//Add the player to the world
						world.addEntity(p);
						p.setRoom(world.getRoomAt(p.getPosition()));
						p.getRoom().putInRoom(p);
						
						//Tell clients about new player. The new client will use the id given.
						Message playerJoined = new PlayerJoiningMessage(id);
						for (Connection con : connections.values()){
							con.sendMessage(playerJoined);
						}
						
						//Send the current state of the world to the player
						String representation = saver.representWorldAsString(world);
						newClient.sendMessage(new TextMessage(representation));
						
						//Ensure the new player is in the correct spot for existing clients
						for (Connection other : connections.values()){
							if (other != newClient){
								other.sendMessage(new EntityMovedMessage(id, p.getPosition()));
								other.sendMessage(new EntityRotationMessage(id, p.getXRotation(), p.getYRotation()));
							}
						}
						
						
					}
				} catch (SocketException se){
					if (!se.getMessage().equals("socket closed")){
						System.err.println("Socket Failure");
					}
				} catch (IOException e) {
					System.err.println("Connection Attempt Failed");
				}
			}
		}
	}
	
	/**
	 * 
	 * @author James Greenwood-Thessman (300289004)
	 */
	private class WorldSaveRoutine implements Runnable {
		
		@Override
		public void run() {
			while (stillAlive){
				//Sleep for the required time
				try {
					Thread.sleep(TIME_BETWEEN_SAVES);
				} catch (InterruptedException e) {
					continue;
				}
				
				synchronized (world){
					saver.saveWorld(savePath, world, new ArrayList<Player>(inactivePlayers.values()));
				}
			}
		}
	}
}
