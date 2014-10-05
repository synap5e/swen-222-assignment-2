package space.network.message;

import java.nio.ByteBuffer;

/**
 * InteractionMessage is the message used for informing clients and the server that an entity has interacted with an entity.
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class InteractionMessage implements Message {

	/**
	 * The ID of the player who is interacting.
	 */
	private int playerId;
	
	/**
	 * The ID of the entity being interacted with.
	 */
	private int entityId;
	
	/**
	 * Creates the message that a player has interacted with an entity.
	 * 
	 * @param playerId the ID of the player
	 * @param entityId the ID of the entity
	 */
	public InteractionMessage(int playerId, int entityId) {
		this.playerId = playerId;
		this.entityId = entityId;
	}
	
	/**
	 * Creates the message that a player has interacted with an entity.
	 * 
	 * @param data the byte array that holds the ID of both the player and entity. 
	 * The first 4 bytes are the ID of the player. The second 4 bytes are the ID of the entity.
	 */
	public InteractionMessage(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		playerId = buffer.getInt();
		entityId = buffer.getInt();
	}
	
	/**
	 * Gets the ID of the player who is interacting.
	 * 
	 * @return The ID of the player.
	 */
	public int getPlayerID(){
		return playerId;
	}
	
	/**
	 * Gets the ID of the entity being interacted with.
	 * 
	 * @return The ID of the entity.
	 */
	public int getEntityID(){
		return entityId;
	}
	
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE*2/8);
		buffer.putInt(playerId);
		buffer.putInt(entityId);
		return buffer.array();
	}

	@Override
	public String toString(){
		return "[Interaction]: Player (" + playerId + ") has interacted with the entity (" + entityId + ")";
	}
}
