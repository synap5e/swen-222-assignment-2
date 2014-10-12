package space.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

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
import space.network.message.ShutdownMessage;
import space.network.message.TextMessage;
import space.network.message.TransferMessage;
import space.network.storage.WorldLoader;
import space.world.Container;
import space.world.Door;
import space.world.Entity;
import space.world.Pickup;
import space.world.Player;
import space.world.Room;
import space.world.World;

/**
 * The Client class manages the local version of the game state, 
 *  applying changes from the server as well as from local input.
 * Changes caused by local input are sent to the connected server.
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class Client {
	
	
	public final static String DEFAULT_HOST = "localhost";
	
	public final static int DEFAULT_PORT = 1234;

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
	 * @param loader the world loader that will load the world sent by the server
	 * @param prevId the previous ID used when connecting to the server
	 */
	public Client(String host, int port, WorldLoader loader, int prevId){
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
			
			localPlayer = (Player) world.getEntity(joinConfirmation.getPlayerID());
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
	 * Creates a game client that connects to a server as a new player.
	 * 
	 * @param host the host name of the server
	 * @param port the port of the server
	 * @param loader the world loader that will load the world sent by the server
	 */
	public Client(String host, int port, WorldLoader loader){
		this(host, port, loader, -1);
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

	//TODO: remove
	boolean torch = false;
	boolean interact = false;
	
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
				
				//TODO: Tempory code to test getViewedEntity() works for doors
				if (Keyboard.isKeyDown(Keyboard.KEY_E)){
					if (!interact){
						interact = true;
						Entity viewed = getViewedEntity();
						if (viewed != null){
							interactWith(viewed);
							
							if (viewed instanceof Container){
								Container c = (Container) viewed;
								if (c.isOpen()){
									if (c.getItemsContained().size() > 0){
										transfer((Entity) c.getItemsContained().get(0), c, localPlayer);
									} else if (localPlayer.getInventory().size() > 0){
										transfer((Entity) localPlayer.getInventory().get(0), localPlayer, c);
									}
								}
							}
						}
					}
				} else {
					interact = false;
				}
				
				//TODO: Tempory code to test dropping entities
				if (Keyboard.isKeyDown(Keyboard.KEY_Q)){
					List<Pickup> inv = localPlayer.getInventory();
					if (inv.size() > 0){
						for (Pickup p : inv){
							drop((Entity) p);
							break;
						}
					}
				}
				
				//TODO: Temp code for toggle torch
				if (Keyboard.isKeyDown(Keyboard.KEY_F)){
					if (!torch){
						localPlayer.setTorch(!localPlayer.isTorchOn());
						torch = true;
					}
				} else {
					torch = false;
				}
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
		float distance = -localPlayer.getEyeHeight()/look.getY();
		
		//If the floor location is in front of the player
		if (distance >= 0){
			Vector2D locationOnFloor = new Vector2D(pos.getX()+look.getX()*distance, pos.getY()+look.getZ()*distance);
			
			Entity viewed = null;
			float closest = Float.MAX_VALUE;
			
			//Find the closest entity to the floor location
			for (Entity e : localPlayer.getRoom().getEntities()){
				float distanceBetween = e.getPosition().sub(locationOnFloor).sqLen();
				if(distanceBetween < closest && distanceBetween < e.getCollisionRadius()*e.getCollisionRadius()){
					viewed = e;
					closest = distanceBetween;
				}
			}

			if (viewed != null) return viewed;
		}
		
		//No entity on floor so check doors
		
		//Create a line of sight from the player
		Segment2D lineOfSight = new Segment2D(pos, pos.add(new Vector2D(look.getX()*10, look.getZ()*10)));
		
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
		boolean interactionSuccessful = e.interact(localPlayer, world);
		
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
	
	public void transfer(Entity pickup, Container from, Player to){
		if (from.getItemsContained().contains(pickup)){
			from.removeContainedItem(pickup);
			to.pickup(pickup);

			//Tell the server
			sendMessage(new TransferMessage(pickup.getID(), to.getID(), from.getID(), false));
		}
	}

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
	
	/**
	 * MessageHandler handles incoming messages from the server and applies them to the world.
	 * 
	 * @author James Greenwood-Thessman (300289004)
	 */
	private class MessageHandler implements Runnable {
		
		@Override
		public void run() {
			try {
				while (stillAlive){
					while (connection.hasMessage()){
						Message message = connection.readMessage();
						
						//Ensure the world is able to be modified
						synchronized (world) {
							//Add any new players
							if (message instanceof PlayerJoiningMessage){
								handlePlayerJoin((PlayerJoiningMessage) message);
							//Remove disconnected players
							} else if (message instanceof DisconnectMessage){
								handlePlayerDisconnect((DisconnectMessage) message);
							//Move remotely controlled entities
							} else if (message instanceof EntityMovedMessage){
								handleMove((EntityMovedMessage) message);
							//Rotate remote player
							} else if (message instanceof EntityRotationMessage){
								handlePlayerLook((EntityRotationMessage) message);
							//Make player jump
							} else if (message instanceof JumpMessage){
								handlePlayerJump((JumpMessage) message);
							//Make player interact with entity
							} else if (message instanceof InteractionMessage){
								handleInteraction((InteractionMessage) message);
							//Drop a pickup from a player
							} else if (message instanceof DropPickupMessage){
								handleDrop((DropPickupMessage) message);
							//Transfer an entity between player and a container
							} else if (message instanceof TransferMessage){
								handleTransfer((TransferMessage) message);
							//Remote shutdown
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
					
					//Sleep, iterating over the loop roughly 60 times a second
					try {
						Thread.sleep(17);
					} catch (InterruptedException e) {
					}
				}
			} catch (IOException e){
				shutdown();
				alertListenersOfShutdown("Connection to server has been lost");
			}
		}

		/**
		 * Handles a remote player joining the game.
		 * 
		 * @param playerJoined the message containing the information about this player
		 */
		private void handlePlayerJoin(PlayerJoiningMessage playerJoined){
			Player p = new Player(new Vector2D(0, 0), playerJoined.getPlayerID(), "Player");
			world.addEntity(p);
			p.setRoom(world.getRoomAt(p.getPosition()));
			p.getRoom().putInRoom(p);
		}
		
		/**
		 * Handles a remote player disconnecting from the game.
		 * 
		 * @param playerDisconnected the message containing the information about this player
		 */
		private void handlePlayerDisconnect(DisconnectMessage playerDisconnected){
			Entity e = world.getEntity(playerDisconnected.getPlayerID());
			
			//Remove from the room and world
			world.getRoomAt(e.getPosition()).removeFromRoom(e);
			//world.removeEntity(e);
		}
		
		/**
		 * Handles an entity moving.
		 * 
		 * @param entityMoved the message containing the information about the moving entity
		 */
		private void handleMove(EntityMovedMessage entityMoved){
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
		}
		
		/**
		 * Handles dropping an entity from a remote player's inventory.
		 * 
		 * @param drop the message containing information about the drop
		 */
		private void handleDrop(DropPickupMessage drop){
			Player p = (Player) world.getEntity(drop.getPlayerId());
			Entity e = world.getEntity(drop.getPickupId());
			world.dropEntity(p, e, drop.getPosition());
		}
		
		/**
		 * Handles rotating a remote player's look direction.
		 * 
		 * @param playerRotated the message containing the information about the rotation
		 */
		private void handlePlayerLook(EntityRotationMessage playerRotated){
			Player p = (Player) world.getEntity(playerRotated.getID());
			p.setXRotation(playerRotated.getXRotation());
			p.setYRotation(playerRotated.getYRotation());
		}
		
		/**
		 * Handles a remote player interacting with an entity.
		 * 
		 * @param interaction the message containing the information about the interaction
		 */
		private void handleInteraction(InteractionMessage interaction){
			Entity e = world.getEntity(interaction.getEntityID());
			Player p = (Player) world.getEntity(interaction.getPlayerID());
			
			if (e.canInteract()){
				e.interact(p, world);
			}
		}
		
		/**
		 * Handles a remote player jumping.
		 * 
		 * @param jumpingPlayer the message containing the information about the jumping player
		 */
		private void handlePlayerJump(JumpMessage jumpingPlayer){
			Player p = (Player) world.getEntity(jumpingPlayer.getPlayerID());
			p.jump();
		}
		
		/**
		 * Handles the transfer of an entity between a player and a container.
		 * 
		 * @param transfer the message containing the information about the transfer
		 */
		private void handleTransfer(TransferMessage transfer){
			//Get the entities involved
			Entity e = world.getEntity(transfer.getEntityID());
			Player p = (Player) world.getEntity(transfer.getPlayerID());
			Container c = (Container) world.getEntity(transfer.getContainerID());

			if (transfer.fromPlayer()){
				if (p.getInventory().contains(e) && c.canPutInside(e)){
					p.getInventory().remove(e);
					c.putInside(e);
				}
			} else {
				if (c.getItemsContained().contains(e)){
					c.removeContainedItem(e);
					p.pickup(e);
				}
			}
		}
	}
}
