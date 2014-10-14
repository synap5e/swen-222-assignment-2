package space.network.message;

import java.nio.ByteBuffer;

import space.math.Vector2D;

/**
 * DropPickupMessage is the message used to inform clients/servers that a player has dropped an entity from their inventory.
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class DropPickupMessage implements Message {

	/**
	 * The ID of the player dropping the pickup.
	 */
	private int playerId;
	
	/**
	 * The ID of the pickup being dropped.
	 */
	private int pickupId;
	
	/**
	 * The position on the ground where the pickup is being dropped.
	 */
	private Vector2D position;

	/**
	 * Create the message that a player has dropped a pickup.
	 * 
	 * @param playerId the ID of the player that is dropping the pickup
	 * @param pickupId the ID of the pickup that is being dropped
	 * @param position the position the pickup is being dropped to
	 */
	public DropPickupMessage(int playerId, int pickupId, Vector2D position) {
		this.playerId = playerId;
		this.pickupId = pickupId;
		this.position = position;
	}
	
	/**
	 * Create the message that a player has dropped a pickup.
	 * 
	 * @param data the byte array representing the message.
	 */
	public DropPickupMessage(byte[] data){
		ByteBuffer buffer = ByteBuffer.wrap(data);
		playerId = buffer.getInt();
		pickupId = buffer.getInt();
		position = new Vector2D(buffer.getFloat(), buffer.getFloat());
	}

	/**
	 * Get the ID of the player dropping the pickup.
	 * 
	 * @return The ID of the player.
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * Get the ID of the pickup being dropped.
	 * 
	 * @return The ID of the pickup.
	 */
	public int getPickupId() {
		return pickupId;
	}

	/**
	 * Get the position where the the pickup is being dropped.
	 * 
	 * @return The position of the drop.
	 */
	public Vector2D getPosition() {
		return position;
	}
	
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate((Integer.SIZE*2+Float.SIZE*2)/8);
		buffer.putInt(playerId);
		buffer.putInt(pickupId);
		buffer.putFloat(position.getX());
		buffer.putFloat(position.getY());
		return buffer.array();
	}
	
	@Override
	public String toString(){
		return "[Drop]: Player with the id \"" + playerId + "\" has dropped the pickup with the id \"" + pickupId + "\" at the position " + position;
	}

}
