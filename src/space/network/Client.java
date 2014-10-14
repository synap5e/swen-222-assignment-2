package space.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import space.gui.application.KeyBinding;
import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewableWall;
import space.math.Segment2D;
import space.math.Vector2D;
import space.math.Vector3D;
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
import space.world.Container;
import space.world.Door;
import space.world.Entity;
import space.world.Player;
import space.world.World;

/**
 * The Client class manages the local version of the game state, 
 *  applying changes from the server as well as from local input.
 * Changes caused by local input are sent to the connected server.
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class Client {
	
	/**
	 * The default host to connect to.
	 */
	public final static String DEFAULT_HOST = "localhost";
	
	/**
	 * The default port to connect to.
	 */
	public final static int DEFAULT_PORT = 1234;
	
	/**
	 * The default player id.
	 */
	public final static int DEFAULT_ID = -1;

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
	
	/**
	 * Whether the client is running
	 */
	private boolean stillAlive;
	
	/**
	 * The list of listeners to be alerted of events
	 */
	private List<ClientListener> listeners;

	/**
	 * The binding between actions and keys
	 */
	private KeyBinding keyBinding;
	
	/**
	 * Creates a game client that connects to a server.
	 * 
	 * @param host the host name of the server
	 * @param port the port of the server
	 * @param loader the world loader that will load the world sent by the server
	 * @param prevId the previous ID used when connecting to the server
	 */
	public Client(String host, int port, WorldLoader loader, KeyBinding keyBinding, int prevId){
		this.keyBinding = keyBinding;
		
		//Connect to the server
		try {
			connection = new Connection(new Socket(host, port));
			
			//Request to join the game
			connection.sendMessage(new PlayerJoiningMessage(prevId));
			
			//Create the local player, using the ID supplied by the server
			PlayerJoiningMessage joinConfirmation = (PlayerJoiningMessage) connection.readMessage();
			
			//Load the world
			TextMessage worldData = (TextMessage) connection.readMessage();
			loader.loadWorldFromString(worldData.getText());
			world = loader.getWorld();
			for (Player p : loader.getPlayers()){
				world.addEntity(p);
				p.setRoom(world.getRoomAt(p.getPosition()));
				p.getRoom().putInRoom(p);
			}
			
			//Get the local player
			localPlayer = (Player) world.getEntity(joinConfirmation.getPlayerID());
		} catch (IOException e) {
			//Client failed to connect, critical failure
			throw new RuntimeException(e);
		}
		
		stillAlive = true;
		
		//Create list of listeners
		listeners = new ArrayList<ClientListener>();
		
		//Start handling incoming messages
		new Thread(new MessageHandler(this, connection)).start();
	}
	
	/**
	 * Creates a game client that connects to a server as a new player.
	 * 
	 * @param host the host name of the server
	 * @param port the port of the server
	 * @param loader the world loader that will load the world sent by the server
	 * @param keyBinding the key bindings model
	 */
	public Client(String host, int port, WorldLoader loader, KeyBinding keyBinding){
		this(host, port, loader, keyBinding, -1);
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
	 * Shuts down the client and alerts all client listeners of the reason.
	 * 
	 * @param reason a brief explanation of why the client has shutdown
	 */
	public void shutdown(String reason){
		shutdown();
		alertListenersOfShutdown(reason);
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
	 * Gets whether the client is running.
	 * 
	 * @return Whether the client is running.
	 */
	public boolean isRunning() {
		return stillAlive;
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
			//Ensures the world is able to be safely updated
			synchronized (world) {
				world.update(delta);
				updatePlayer(delta);
			}
		} catch (IOException e) {
			shutdown();
			alertListenersOfShutdown("Connection to server has been lost");
		}
	}
	
	/**
	 * Gets the entity that the local player is currently looking at in the current room.
	 * 
	 * @return The entity being viewed. Is null if no entity is in line of sigh
	 */
	public Entity getViewedEntity(){
		Vector3D look = localPlayer.getLookDirection();
		Vector2D pos = localPlayer.getPosition();
		
		//Create a line of sight from the player
		Segment2D lineOfSight = new Segment2D(pos, pos.add(new Vector2D(look.getX()*10, look.getZ()*10)));
		
		float closest = Float.MAX_VALUE;
		Entity viewed = null;
		for (Entity e : localPlayer.getRoom().getEntities()){
			if (e == localPlayer) continue;
			
			//Get the 3D vector along the floor that is the displacement
			Vector2D to = e.getPosition().sub(localPlayer.getPosition());
			Vector3D towards = new Vector3D(to.getX(), 0, to.getY()).normalized();
			
			//Find the cross section of the entity perpendicular to the player 
			Vector3D side =  towards.cross(new Vector3D(0, 1, 0));
			Vector2D sideways = new Vector2D(side.getX(), side.getZ()).normalized().mul(e.getCollisionRadius());
			Segment2D face = new Segment2D(e.getPosition().sub(sideways), e.getPosition().add(sideways));
			
			//Skip if the player isn't looking at the entity
			if (!face.intersects(lineOfSight)) continue;
			
			//Get the intersection
			Vector2D intersection = face.getIntersection(lineOfSight);
			
			//Check the player was looking vertically at the entity
			float t = (intersection.getX()-pos.getX())/look.getX();
			float y = localPlayer.getEyeHeight()+look.getY()*t;
			if (y < e.getElevation()){
				t = (e.getElevation()-localPlayer.getEyeHeight())/look.getY();
				intersection.setX(pos.getX()+look.getX()*t);
				intersection.setY(pos.getY()+look.getZ()*t);
				if (intersection.sub(e.getPosition()).sqLen() > e.getCollisionRadius()*e.getCollisionRadius()) continue;
			} else if (y > e.getElevation()+e.getHeight()) continue;
			
			//If that entity is the closest, the player must be looking at it
			float distanceBetween = e.getPosition().sub(intersection).sqLen();
			if (distanceBetween < closest){
				viewed = e;
				closest = distanceBetween;
			}
		}
		//Return if an entity was found
		if (viewed != null) return viewed;
		
		//No entity within the room so check doors
		
		//For each wall
		for (ViewableWall w : localPlayer.getRoom().getWalls()){
			//Find intersection along wall
			Segment2D line = new Segment2D(w.getStart(), w.getEnd());
			if (!line.intersects(lineOfSight)) continue;
			Vector2D intersection = line.getIntersection(lineOfSight);
			
			//If there is an intersection then find the door
			for (ViewableDoor vd : w.getDoors()){
				Door d = (Door) vd;
				float distanceBetween = d.getPosition().sub(intersection).sqLen();
				if(distanceBetween < d.getCollisionRadius()*d.getCollisionRadius()){
					return d;
				}
			}
		}
		
		//No entity found
		return null;
	}
	
	/**
	 * Makes the local player interact with the given entity.
	 * 
	 * @param e the entity to interact with
	 * @return Whether the interaction was successful.
	 */
	public boolean interactWith(Entity e){
		//Ensure there is an entity to interact with
		if(e == null || !e.canInteract()){
			return false;
		}
		
		boolean interactionSuccessful = e.interact(localPlayer, world);
		
		//Alert the server of the interaction
		if (interactionSuccessful){
			sendMessage(new InteractionMessage(localPlayer.getID(), e.getID()));
		}

		return interactionSuccessful;
	}
	
	/**
	 * Drops an entity from the local players inventory.
	 * 
	 * @param e the entity to drop
	 */
	public void drop(Entity e){
		Vector3D look = localPlayer.getLookDirection();
		Vector2D pos = localPlayer.getPosition();
		float distance = -localPlayer.getEyeHeight()/look.getY();
		
		//If the floor location is in front of the player
		if (distance >= 0){
			Vector2D locationOnFloor = new Vector2D(pos.getX()+look.getX()*distance, pos.getY()+look.getZ()*distance);
			
			synchronized (world) {
				world.dropEntity(localPlayer, e, locationOnFloor);
				
				//If the entity was dropped tell the server
				if (e.getPosition().equals(locationOnFloor, 0.01f)){
					sendMessage(new DropPickupMessage(localPlayer.getID(), e.getID(), locationOnFloor));
				}
			}
		}
	}
	
	/**
	 * Transfers a pickup from a container to a player.
	 * 
	 * @param pickup the entity being transferred
	 * @param from the container containing the entity
	 * @param to the player that will hold the entity
	 */
	public void transfer(Entity pickup, Container from, Player to){
		if (from.getItemsContained().contains(pickup)){
			System.out.println("test");
			from.removeContainedItem(pickup);
			to.pickup(pickup);

			//Tell the server
			sendMessage(new TransferMessage(pickup.getID(), to.getID(), from.getID(), false));
		}
	}

	/**
	 * Transfers a pickup from a player to a container.
	 * 
	 * @param pickup the entity being transferred
	 * @param from the player whose inventory contains the entity
	 * @param to the container that will hold the entity
	 */
	public void transfer(Entity pickup, Player from, Container to){
		if (from.getInventory().contains(pickup) && to.canPutInside(pickup)){
			from.getInventory().remove(pickup);
			to.putInside(pickup);

			//Tell the server
			sendMessage(new TransferMessage(pickup.getID(), from.getID(), to.getID(), true));
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
		if (mouseDelta.sqLen() > 0.01){
			connection.sendMessage(new EntityRotationMessage(localPlayer.getID(), localPlayer.getXRotation(), localPlayer.getYRotation()));
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
		if (Keyboard.isKeyDown(keyBinding.getKey(KeyBinding.MOVE_FORWARD))){
			moveDelta.addLocal(moveDirection);
		}
		if (Keyboard.isKeyDown(keyBinding.getKey(KeyBinding.MOVE_LEFT))){
			moveDelta.subLocal(moveDirection.cross(new Vector3D(0,1,0)));
		}
		if (Keyboard.isKeyDown(keyBinding.getKey(KeyBinding.MOVE_RIGHT))){
			moveDelta.addLocal(moveDirection.cross(new Vector3D(0,1,0)));
		}
		if (Keyboard.isKeyDown(keyBinding.getKey(KeyBinding.MOVE_BACKWARD))){
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
	 * Sends a message to the server dealing with any connection errors.
	 * 
	 * @param message the message to send
	 */
	private void sendMessage(Message message){
		try {
			connection.sendMessage(message);
		} catch (IOException e1) {
			shutdown();
			alertListenersOfShutdown("Connection to server has been lost");
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
}
