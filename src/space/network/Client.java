package space.network;

import java.io.IOException;
import java.net.Socket;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import space.gui.pipeline.mock.MockWorld;
import space.math.Vector2D;
import space.math.Vector3D;
import space.world.Player;
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
	
	//TODO: Change type to World
	/**
	 * The local version of the game world.
	 */
	private MockWorld world;
	
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
	 * @param localPlayer the local player
	 */
	public Client(String host, int port, MockWorld world, Player localPlayer){
		this.world = world;
		this.localPlayer = localPlayer;
		
		//Connect to the server
		try {
			connection = new Connection(new Socket(host, port));
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
		return null; //TODO Return the world object, once it is of the correct type.
	}

	/**
	 * Updates the game world with changes from the server and local input.
	 * 
	 * @param delta the change in time since the last update
	 */
	public void update(int delta) {
		//Apply updates from server
		while (connection.hasMessage()){
			//TODO: Actually apply updates
			System.out.println(connection.readMessage());
		}
		
		world.update(delta);
		updatePlayer(delta);
	}
	
	/**
	 * Updates the local player.
	 * 
	 * @param delta the change in time since the last update
	 */
	private void updatePlayer(int delta){
		int x = Mouse.getX();
		int y = Mouse.getY();
		
		//Update the players viewing direction
		Vector2D mouseDelta = new Vector2D(x-lastx,y-lasty);
		localPlayer.moveLook(mouseDelta);
		//TODO: Broadcast change to server

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
	 */
	private void applyWalk(int delta){
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
		if (moveDelta.sqLen() != 0){
			//Calculate the movement for this update
			moveDelta = moveDelta.normalized().mul(delta/75f);
			
			//Move the player. TODO: Change to use a translate method
			localPlayer.setPosition(localPlayer.getPosition().add(new Vector2D(moveDelta.getX(), moveDelta.getZ())));
			
			//TODO: send change to server
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
