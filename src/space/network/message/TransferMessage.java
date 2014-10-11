package space.network.message;

import java.nio.ByteBuffer;

/**
 * TransferMessage is message used for informing the client/server that an entity has been transferred between
 *  a player and a container. The direction of the transfer is also contained in the message.
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class TransferMessage implements Message {

	/**
	 * The ID of the entity being transferred.
	 */
	private int entityId;
	
	/**
	 * The ID of the player that is part of the transfer.
	 */
	private int playerId;
	
	/**
	 * The ID of the container that is part of the transfer.
	 */
	private int containerId;
	
	/**
	 * Whether the entity is being transferred from the player to the container.
	 */
	private boolean fromPlayer;

	/**
	 * Creates a message that the entity has been transferred between the player and the container.
	 * 
	 * @param entityId the ID of the entity being transferred
	 * @param playerId the ID the player that is part of the transfer 
	 * @param containerId the ID of the container that is part of the transfer
	 * @param fromPlayer whether the entity is being transferred from the player to the container
	 */
	public TransferMessage(int entityId, int playerId, int containerId, boolean fromPlayer){
		this.entityId = entityId;
		this.playerId = playerId;
		this.containerId = containerId;
		this.fromPlayer = fromPlayer;
	}

	/**
	 * Creates a message that the entity has been transferred between the player and the container.
	 * 
	 * @param data the byte array that holds the IDs of the entity, player and container as well 
	 *  as the whether the transfer is from the player. The first 4 bytes are the ID of the entity.
	 *  The second 4 bytes are the ID of the player. The third 4 bytes are theID of the container. 
	 *  The last byte represents the direction of the transfer.
	 */
	public TransferMessage(byte[] data) {
		byte tru = (byte) 1;
		ByteBuffer buffer = ByteBuffer.wrap(data);
		entityId = buffer.getInt();
		playerId = buffer.getInt();
		containerId = buffer.getInt();
		fromPlayer = (buffer.get() == tru);
	}

	/**
	 * Gets the ID of the entity being transferred.
	 * 
	 * @return The ID of the entity being transferred.
	 */
	public int getEntityID(){
		return entityId;
	}

	/**
	 * Gets the ID of the player that is part of the transfer.
	 * 
	 * @return The ID of the player that is part of the transfer.
	 */
	public int getPlayerID(){
		return playerId;
	}

	/**
	 * Gets the ID of the container that is part of the transfer.
	 * 
	 * @return The ID of the container that is part of the transfer.
	 */
	public int getContainerID(){
		return containerId;
	}
	
	/**
	 * Whether the entity is being transferred from the player to the container.
	 * 
	 * @return Whether the entity is being transferred from the player to the container.
	 */
	public boolean fromPlayer(){
		return fromPlayer;
	}

	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE*3/8+1);
		buffer.putInt(entityId);
		buffer.putInt(playerId);
		buffer.putInt(containerId);
		buffer.put((byte) ((fromPlayer) ? 1 : 0));
		return buffer.array();
	}
	
	@Override
	public String toString(){
		return "[Transfer]: Entity with the id \"" + entityId + "\" transferred between player \"" + playerId + "\" and the container \"" + containerId + "\"";
	}

}
