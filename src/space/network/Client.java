package space.network;

import java.io.IOException;
import java.net.Socket;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import space.math.Vector2D;
import space.math.Vector3D;
import space.network.message.DisconnectMessage;
import space.network.message.EntityMovedMessage;
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
	 * The last x coordinate for the mouse.
	 */
	private int lastx;
	
	/**
	 * The last y coordinate for the mouse.
	 */
	private int lasty;
	
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
		} catch (IOException e) {
			//Client failed to connect, critical failure
			throw new RuntimeException(e);
		}
		
		//Get the initial location of the mouse
		lastx = Mouse.getX();
		lasty = Mouse.getY();
	}
	
	/**
	 * Shuts down the client.
	 */
	public void shutdown(){
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
		return null;
	}

	/**
	 * Updates the game world with changes from the server and local input.
	 * 
	 * @param delta the change in time since the last update
	 */
	public void update(int delta) {
		//Apply updates from server
		try {
			while (connection.hasMessage()){
				Message message = connection.readMessage();
				
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
				} else if (message instanceof ShutdownMessage){
					shutdown();
					//TODO: Inform application window to go to main menu
					throw new RuntimeException("Server shutdown");
				} else {
					//TODO: Decide when to log
					System.out.println(connection.readMessage());
				}
			}
			
			//world.update(delta);
			updatePlayer(delta);
		} catch (IOException e) {
			shutdown();
			//TODO: Inform application window to go to main menu
			throw new RuntimeException("Server connection lost");
		}
	}
	
	/**
	 * Updates the local player.
	 * 
	 * @param delta the change in time since the last update
	 * @throws IOException 
	 */
	private void updatePlayer(int delta) throws IOException{
		int x = Mouse.getX();
		int y = Mouse.getY();
		
		//Update the players viewing direction
		Vector2D mouseDelta = new Vector2D(x-lastx,y-lasty);
		localPlayer.moveLook(mouseDelta);
		
		//Broadcast change to server
		if (mouseDelta.sqLen() > 0){
			connection.sendMessage(new PlayerRotatedMessage(localPlayer.getID(), mouseDelta));
		}

		//Deal with player movement
		applyWalk(delta);
		updateJump(delta);

		//Record the latest location of the mouse
		lastx = x;
		lasty = y;
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
			//Move the player. TODO: Change to use a translate method
			if (world.getRoomAt(position) != null){
				localPlayer.setPosition(position);
				
				//Tell the server that the player moved
				connection.sendMessage(new EntityMovedMessage(localPlayer.getID(), position));
			}
		}
	}
	
	/**
	 * Updates the local player's jump status.
	 * 
	 * @param delta the change in time since the last update
	 */
	private void updateJump(int delta){
		//TODO: implement jumping once methods to do so exist
		/*float jumpTime = localPlayer.getJumpTime();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && localPlayer.getJumpTime() == 0){
			localPlayer.setJumpTime(1);
			jumpTime = 1;
		}
		if (jumpTime > 0){
			localPlayer.setJumpTime(1);
			jumpTime -= delta/500f;
		} else {
			jumpTime = 0;
		}*/
	}
}
