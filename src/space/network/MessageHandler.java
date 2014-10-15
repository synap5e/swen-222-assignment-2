package space.network;

import java.io.IOException;
import space.math.Vector2D;
import space.network.message.DisconnectMessage;
import space.network.message.DropPickupMessage;
import space.network.message.EntityMovedMessage;
import space.network.message.EntityRotationMessage;
import space.network.message.InteractionMessage;
import space.network.message.JumpMessage;
import space.network.message.Message;
import space.network.message.PlayerJoiningMessage;
import space.network.message.ShutdownMessage;
import space.network.message.TransferMessage;
import space.world.Container;
import space.world.Entity;
import space.world.Player;
import space.world.Room;
import space.world.World;

/**
 * MessageHandler handles incoming messages from the server and applies them to the world.
 *
 * @author James Greenwood-Thessman (300289004)
 */
class MessageHandler implements Runnable {

	/**
	 * The client that is having it's messages handled
	 */
	private Client client;

	/**
	 * The connection that receives the messages
	 */
	private Connection connection;

	/**
	 * Creates a message handler for a client.
	 *
	 * @param client the client to handle messages for
	 * @param connection the connection to read messages from
	 */
	public MessageHandler(Client client, Connection connection) {
		this.client = client;
		this.connection = connection;
	}

	@Override
	public void run() {
		try {
			while (client.isRunning()){
				while (connection.hasMessage()){
					Message message = connection.readMessage();

					World world = client.getWorld();
					//Ensure the world is able to be modified
					synchronized (world) {
						//Add any new players
						if (message instanceof PlayerJoiningMessage){
							handlePlayerJoin((PlayerJoiningMessage) message, world);
						//Remove disconnected players
						} else if (message instanceof DisconnectMessage){
							handlePlayerDisconnect((DisconnectMessage) message, world);
						//Move remotely controlled entities
						} else if (message instanceof EntityMovedMessage){
							handleMove((EntityMovedMessage) message, world);
						//Rotate remote player
						} else if (message instanceof EntityRotationMessage){
							handlePlayerLook((EntityRotationMessage) message, world);
						//Make player jump
						} else if (message instanceof JumpMessage){
							handlePlayerJump((JumpMessage) message, world);
						//Make player interact with entity
						} else if (message instanceof InteractionMessage){
							handleInteraction((InteractionMessage) message, world);
						//Drop a pickup from a player
						} else if (message instanceof DropPickupMessage){
							handleDrop((DropPickupMessage) message, world);
						//Transfer an entity between player and a container
						} else if (message instanceof TransferMessage){
							handleTransfer((TransferMessage) message, world);
						//Remote shutdown
						} else if (message instanceof ShutdownMessage){
							client.shutdown("Server has shutdown");
							return;
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
			client.shutdown("Connection to server has been lost");
		}
	}

	/**
	 * Handles a remote player joining the game.
	 *
	 * @param playerJoined the message containing the information about this player
	 * @param world the world to apply any changes to
	 */
	private void handlePlayerJoin(PlayerJoiningMessage playerJoined, World world){
		Player p = new Player(new Vector2D(0, 0), playerJoined.getPlayerID(), "Player");
		world.addEntity(p);
		p.setRoom(world.getRoomAt(p.getPosition()));
		p.getRoom().putInRoom(p);
	}

	/**
	 * Handles a remote player disconnecting from the game.
	 *
	 * @param playerDisconnected the message containing the information about this player
	 * @param world the world to apply any changes to
	 */
	private void handlePlayerDisconnect(DisconnectMessage playerDisconnected, World world){
		Entity e = world.getEntity(playerDisconnected.getPlayerID());

		//Remove from the room and world
		world.getRoomAt(e.getPosition()).removeFromRoom(e);
		//world.removeEntity(e);
	}

	/**
	 * Handles an entity moving.
	 *
	 * @param entityMoved the message containing the information about the moving entity
	 * @param world the world to apply any changes to
	 */
	private void handleMove(EntityMovedMessage entityMoved, World world){
		Entity e = world.getEntity(entityMoved.getEntityID());

		//Move the room the entity is in if required
		Room from = world.getRoomAt(e.getPosition());
		Room to = world.getRoomAt(entityMoved.getNewPosition());
		from.removeFromRoom(e);
		to.putInRoom(e);
		if (e instanceof Player){
			((Player) e).setRoom(to);
		}

		//Move the entity
		e.setPosition(entityMoved.getNewPosition());
	}

	/**
	 * Handles dropping an entity from a remote player's inventory.
	 *
	 * @param drop the message containing information about the drop
	 * @param world the world to apply any changes to
	 */
	private void handleDrop(DropPickupMessage drop, World world){
		Player p = (Player) world.getEntity(drop.getPlayerId());
		Entity e = world.getEntity(drop.getPickupId());
		world.dropEntity(p, e, drop.getPosition());
	}

	/**
	 * Handles rotating a remote player's look direction.
	 *
	 * @param playerRotated the message containing the information about the rotation
	 * @param world the world to apply any changes to
	 */
	private void handlePlayerLook(EntityRotationMessage playerRotated, World world){
		Entity e =  world.getEntity(playerRotated.getID());
		e.setAngle(playerRotated.getYRotation());
		if (e instanceof Player){
			((Player) e).setXRotation(playerRotated.getXRotation());
		}
	}

	/**
	 * Handles a remote player interacting with an entity.
	 *
	 * @param interaction the message containing the information about the interaction
	 * @param world the world to apply any changes to
	 */
	private void handleInteraction(InteractionMessage interaction, World world){
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
	 * @param world the world to apply any changes to
	 */
	private void handlePlayerJump(JumpMessage jumpingPlayer, World world){
		Player p = (Player) world.getEntity(jumpingPlayer.getPlayerID());
		p.jump();
	}

	/**
	 * Handles the transfer of an entity between a player and a container.
	 *
	 * @param transfer the message containing the information about the transfer
	 * @param world the world to apply any changes to
	 */
	private void handleTransfer(TransferMessage transfer, World world){
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
