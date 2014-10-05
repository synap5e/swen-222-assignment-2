package space.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.Sys;

import space.math.Vector2D;
import space.network.message.DisconnectMessage;
import space.network.message.EntityMovedMessage;
import space.network.message.InteractionMessage;
import space.network.message.JumpMessage;
import space.network.message.Message;
import space.network.message.PlayerJoiningMessage;
import space.network.message.PlayerRotatedMessage;
import space.network.storage.WorldLoader;
import space.network.message.ShutdownMessage;
import space.network.message.sync.DoorSyncMessage;
import space.world.Door;
import space.world.Entity;
import space.world.Player;
import space.world.Room;
import space.world.World;

//TODO: Work out a way to let the server be shutdown nicely
/**
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class Server {
	
	private Thread connectionHandler;
	private Thread gameLoop;
	
	private boolean stillAlive;
	private ServerSocket socket;
	private Map<Integer, Connection> connections;
	
	private World world;
	private Map<Integer, Player> unactivePlayers;
	
	//TODO: Work out better way of coming up with an ID
	private int availableId = 9001;
	
	public Server(String host, int port, WorldLoader loader, String savePath){
		//Create the list of client connections
		connections = new HashMap<Integer, Connection>();
		unactivePlayers = new HashMap<Integer, Player>();
		
		//Create the socket for clients to connect to
		try {
			socket = new ServerSocket(port, 50, InetAddress.getByName(host));
		} catch (IOException e) {
			//Throw the exception again as it is a critical failure
			throw new RuntimeException(e);
		}
		
		//Create Connection Handler
		stillAlive = true;
		connectionHandler = new Thread(new ConnectionHandler());
		
		//Load the World
		
		//TODO: Load world from file
		loader.loadWorld(savePath);
		world = loader.getWorld();
		
		//Start accepting connections
		connectionHandler.start();
		
		//Start the game logic
		gameLoop = new Thread(new ServerGameLoop());
		gameLoop.start();
	}
	
	public void shutdown(){
		//Stop the connection handler
		stillAlive = false;
		connectionHandler.interrupt();
		
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
		unactivePlayers.put(disconnectedID, p);
		
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
							} else if (message instanceof PlayerRotatedMessage){
								PlayerRotatedMessage playerRotated = (PlayerRotatedMessage) message;
								Player p = (Player) world.getEntity(playerRotated.getID());

								p.moveLook(playerRotated.getDelta());

								//Forward the message to all the other clients
								sendMessageToAllExcept(id, message);
							//If a player jumped
							} else if (message instanceof JumpMessage){
								JumpMessage thePlayerWhoJumps = (JumpMessage) message;
								
								//Make the player jump
								((Player) world.getEntity(thePlayerWhoJumps.getPlayerID())).jump();
								
								//Forward the message to all the other clients
								sendMessageToAllExcept(id, message);
							//If a player interacted with an entity
							} else if (message instanceof InteractionMessage){
								InteractionMessage interaction = (InteractionMessage) message;
								
								//Get the entities involved
								Entity e = world.getEntity(interaction.getEntityID());
								Player p = (Player) world.getEntity(interaction.getPlayerID());
								
								//Make them interact
								boolean succesful = false;//TODO: replace with e.interact(p);
								
								//TODO: Remove when e.interact is implemented
								if (e instanceof Door){
									Door d = (Door) e;
									if (d.getOpenPercent() > 0.5){
										d.closeDoor();
										succesful = true;
									} else if (d.getOpenPercent() < 0.5){
										d.openDoor();
										succesful = true;
									}
								}
								
								//If the interaction succeeded, forward the message
								if (succesful){
									sendMessageToAllExcept(id, message);
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
					
					//If the client didn't have an ID previously
					if (id == -1){
						//Assign a new ID
						id = availableId++;
					}
					
					//Add the client the map of connections
					synchronized (connections) {
						connections.put(id, new Connection(socketConnection));
					}
					
					synchronized (world){
						//TODO load from map if player already exists
						world.addEntity(new Player(new Vector2D(0, 0), id));
						((Player) world.getEntity(id)).setRoom(world.getRoomAt(new Vector2D(0, 0)));
						
						//Tell clients about new player. The new client will use the id given.
						Message playerJoined = new PlayerJoiningMessage(id);
						for (Connection con : connections.values()){
							con.sendMessage(playerJoined);
						}
						
						//Add the other players to the client
						Connection con = connections.get(id);
						for (Map.Entry<Integer, Connection> cons : connections.entrySet()){
							int otherId = cons.getKey();
							if (otherId != id){
								Player other = (Player) world.getEntity(otherId);
								con.sendMessage(new PlayerJoiningMessage(otherId));
								con.sendMessage(new EntityMovedMessage(otherId, world.getEntity(otherId).getPosition()));
								con.sendMessage(new PlayerRotatedMessage(otherId, new Vector2D((other.getAngle()-280)*8, 0)));
							}
						}
						
						sendMessageToAllExcept(id, new PlayerRotatedMessage(id, new Vector2D((-180)*8, 0)));
						
						//Sync the doors
						for (Room room : world.getRooms().values()){
							for (List<Door> doors : room.getDoors().values()){
								for (Door door : doors){
									//TODO: Might need a more reliable way of checking whether the door is open
									newClient.sendMessage(new DoorSyncMessage(door.getID(), door.canGoThrough(), door.isLocked()));
								}
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
}
