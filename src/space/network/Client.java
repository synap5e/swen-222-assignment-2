package space.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import space.math.Vector2D;
import space.math.Vector3D;
import space.network.message.DisconnectMessage;
import space.network.message.EntityMovedMessage;
import space.network.message.JumpMessage;
import space.network.message.Message;
import space.network.message.PlayerJoiningMessage;
import space.network.message.PlayerRotatedMessage;
import space.network.message.ShutdownMessage;
import space.world.Entity;
import space.world.Player;
import space.world.Room;
import space.world.World;

/**
 * The Client class manages the local version of the game state, 
 *  applying changes from the server as well as from local input.
 * Changes caused by local input are sent to the connected server.
 * 
 * @author James Greenwood-Thessman (greenwjame1)
 */
public class Client {

	/**
	 * The client's connection to the server.
	 */
	private Connection connection;
	
	/**
	 * The local version of the game world.
	 */
	private World world;
	
	/**
	 * The player who is being played by the client.
	 */
	private Player localPlayer;
	
	/**
	 * Whether the client is active and should take user input.
	 */
	private boolean active;
	
	private boolean stillAlive;
	
	/**
	 * The list of listeners to be alerted of events
	 */
	public List<ClientListener> listeners;
	
	/**
	 * Creates a game client that connects to a server.
	 * 
	 * @param host the host name of the server
	 * @param port the port of the server
	 * @param world the local instance of the game world
	 */
	public Client(String host, int port, World world){
		this.world = world;
		
		//Connect to the server
		try {
			connection = new Connection(new Socket(host, port));
			
			//Request to join the game
			connection.sendMessage(new PlayerJoiningMessage(-1)); //TODO use previous ID if one exists 
			
			//Create the local player, using the ID supplied by the server
			PlayerJoiningMessage joinConfirmation = (PlayerJoiningMessage) connection.readMessage();
			localPlayer = new Player(new Vector2D(0, 0), joinConfirmation.getPlayerID());
			localPlayer.setRoom(world.getRoomAt(localPlayer.getPosition()));
			localPlayer.getRoom().putInRoom(localPlayer);
		} catch (IOException e) {
			//Client failed to connect, critical failure
			throw new RuntimeException(e);
		}
		
		stillAlive = true;
		
		//Create list of listeners
		listeners = new ArrayList<ClientListener>();
		
		new Thread(new MessageHandler()).start();
	}
	
	/**
	 * Shuts down the client.
	 */
	public void shutdown(){
		stillAlive = false;
		
		//Inform the server
		try {
			connection.sendMessage(new DisconnectMessage(localPlayer.getID()));
		} catch (IOException e) {
			//Exception disregarded as it was being closed anyway
		}
		
		connection.close();
	}
	
	/**
	 * Gets the local player.
	 * 
	 * @return The local player.
	 */
	public Player getLocalPlayer(){
		return localPlayer;
	}
	
	/**
	 * Gets the current known state of the game world.
	 * 
	 * @return The game world.
	 */
	public World getWorld(){
		return world;
	}
	
	/**
	 * Sets whether the client is actively accepting user input.
	 * 
	 * @param isActive whether the client should accept user input
	 */
	public void setActive(boolean isActive){
		this.active = isActive;
	}
	
	/**
	 * Adds a listener to the client. The listener will be alerted of critical events that occur.
	 * 
	 * @param listener the listener to add
	 * @return Whether the listener was added successfully
	 */
	public boolean addListener(ClientListener listener){
		return listeners.add(listener);
	}
	
	/**
	 * Removes a listener to the client. The listener will no longer be alerted of critical events that occur.
	 * 
	 * @param listener the listener to remove
	 * @return Whether the listener was removed successfully
	 */
	public boolean removeListener(ClientListener listener){
		return listeners.remove(listener);
	}

	/**
	 * Updates the game world with changes from the server and local input.
	 * 
	 * @param delta the change in time since the last update
	 */
	public void update(int delta) {
		try {
			synchronized (world) {
				//Update the world
				world.update(delta);
				updatePlayer(delta);
			}
		} catch (IOException e) {
			shutdown();
			alertListenersOfShutdown("Connection to server has been lost");
		}
	}
	
	/**
	 * Updates the local player.
	 * 
	 * @param delta the change in time since the last update
	 * @throws IOException 
	 */
	private void updatePlayer(int delta) throws IOException{
		if (!active) return;
		
		//Update the players viewing direction
		int dx = Mouse.getDX();
		int dy = Mouse.getDY();
		Vector2D mouseDelta = new Vector2D(dx,dy);
		localPlayer.moveLook(mouseDelta);
		
		//Broadcast change to server
		if (mouseDelta.sqLen() > 0){
			connection.sendMessage(new PlayerRotatedMessage(localPlayer.getID(), mouseDelta));
		}

		//Deal with player movement
		applyWalk(delta);

		//Deal with jumping
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			localPlayer.jump();
			
			//Inform the server of the heroic jump
			connection.sendMessage(new JumpMessage(localPlayer.getID()));
		}
	}
	
	/**
	 * Moves the local player depending on the keys pressed.
	 * 
	 * @param delta the change in time since the last update
	 * @throws IOException 
	 */
	private void applyWalk(int delta) throws IOException{
		Vector3D moveDirection = localPlayer.getLookDirection();
		Vector3D moveDelta = new Vector3D(0, 0, 0);

		//Calculate the players general movement
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			moveDelta.addLocal(moveDirection);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			moveDelta.subLocal(moveDirection.cross(new Vector3D(0,1,0)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			moveDelta.addLocal(moveDirection.cross(new Vector3D(0,1,0)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			moveDelta.subLocal(moveDirection);
		}
		
		//If the player is intended to move
		moveDelta.setY(0);
		if (moveDelta.sqLen() != 0){
			//Calculate the movement for this update
			moveDelta = moveDelta.normalized().mul(delta/75f);
			
			Vector2D position = localPlayer.getPosition().add(new Vector2D(moveDelta.getX(), moveDelta.getZ()));
			//Move the player.
			world.moveCharacter(localPlayer, position);
			if (localPlayer.getPosition().equals(position, 0.1f)){
				localPlayer.setPosition(position);
				
				//Tell the server that the player moved
				connection.sendMessage(new EntityMovedMessage(localPlayer.getID(), position));
			}
		}
	}
	
	/**
	 * Alerts all client listeners that the client has shutdown.
	 * 
	 * @param reason a brief explanation of why the client has shutdown
	 */
	private void alertListenersOfShutdown(String reason){
		//Inform all the listeners
		for (ClientListener listener : listeners){
			listener.onConnectionClose(reason);
		}
	}
	
	private class MessageHandler implements Runnable {

		@Override
		public void run() {
			try {
				while (stillAlive){
					if (connection.hasMessage()){
						Message message = connection.readMessage();
						
						synchronized (world) {
							//Add any new players
							if (message instanceof PlayerJoiningMessage){
								PlayerJoiningMessage playerJoined = (PlayerJoiningMessage) message;
								Entity e = new Player(new Vector2D(0, 0), playerJoined.getPlayerID());
								world.addEntity(e);
								world.getRoomAt(e.getPosition()).putInRoom(e);
							//Remove disconnected players
							} else if (message instanceof DisconnectMessage){
								DisconnectMessage playerDisconnected = (DisconnectMessage) message;
								Entity e = world.getEntity(playerDisconnected.getPlayerID());
								
								//Remove from the room and world
								world.getRoomAt(e.getPosition()).removeFromRoom(e);
								//world.removeEntity(e);
							//Move remotely controlled entities
							} else if (message instanceof EntityMovedMessage){
								EntityMovedMessage entityMoved = (EntityMovedMessage) message;
								Entity e = world.getEntity(entityMoved.getEntityID());
								
								//Move the room the entity is in if required
								Room from = world.getRoomAt(e.getPosition());
								Room to = world.getRoomAt(entityMoved.getNewPosition());
								if (to != from){
									from.removeFromRoom(e);
									to.putInRoom(e);
								}
								
								//Move the entity
								e.setPosition(entityMoved.getNewPosition());
							//Rotate remote player
							} else if (message instanceof PlayerRotatedMessage){
								PlayerRotatedMessage playerRotated = (PlayerRotatedMessage) message;
								Player p = (Player) world.getEntity(playerRotated.getID());
		
								p.moveLook(playerRotated.getDelta());
							} else if (message instanceof JumpMessage){
								JumpMessage thePlayerWhoJumps = (JumpMessage) message;
								
								//Make the player jump
								((Player) world.getEntity(thePlayerWhoJumps.getPlayerID())).jump();
							} else if (message instanceof ShutdownMessage){
								shutdown();
								alertListenersOfShutdown("Server has shutdown");
								return;
							} else {
								//TODO: Decide when to log
								System.out.println(connection.readMessage());
							}
						}
					}
				}
			} catch (IOException e){
				shutdown();
				alertListenersOfShutdown("Connection to server has been lost");
			}
		}
	}
}
