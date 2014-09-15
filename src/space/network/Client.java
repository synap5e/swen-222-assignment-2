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

public class Client {

	//TODO: Change to World
	private MockWorld world;
	
	private Player localPlayer;
	
	/**
	 * The last x coordinate for the mouse
	 */
	private int lastx = -1;
	/**
	 * The last y coordinate for the mouse
	 */
	private int lasty = -1;
	
	public Client(String host, int port, MockWorld world, Player localPlayer){
		this.world = world;
		this.localPlayer = localPlayer;
		
		try {
			Connection connection = new Connection(new Socket(host, port));
			System.out.println(connection.readMessage());
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Player getLocalPlayer(){
		return localPlayer;
	}
	
	public World getWorld(){
		return null; //TODO Return the world object, once it is of the correct type.
	}

	public void update(int delta) {
		world.update(delta);
		updatePlayer(delta);
	}
	
	private void updatePlayer(int delta){
		int x = Mouse.getX();
		int y = Mouse.getY();


		Mouse.setGrabbed(true);
		Mouse.setClipMouseCoordinatesToWindow(false);

		if (lastx != -1){
			Vector2D mouseDelta = new Vector2D(x-lastx,y-lasty);
			localPlayer.moveLook(mouseDelta);
		}

		applyWalk(delta);
		
		//TODO reimplement jumping when methods exist
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

		lastx = x;
		lasty = y;
	}
	
	private void applyWalk(int delta){
		Vector3D moveDirection = localPlayer.getLookDirection();
		Vector3D moveDelta = new Vector3D(0, 0, 0);

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
		if (moveDelta.sqLen() != 0){
			moveDelta = moveDelta.normalized().mul(delta/75f);
			
			//TODO: Change to use a translate method
			localPlayer.setPosition(localPlayer.getPosition().add(new Vector2D(moveDelta.getX(), moveDelta.getZ())));
		}
	}
}
