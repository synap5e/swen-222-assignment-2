package space.network.message;

import java.nio.ByteBuffer;
import space.math.Vector2D;

/**
 * EntityMovedMessage is message used for informing clients and the server that an entity has moved.
 * The message composes of the ID of the enity and the vector of its new position.
 * 
 * @author James Greenwood-Thessman (greenwjame1)
 */
public class EntityMovedMessage implements Message {

	/**
	 * The ID of the entity that has moved.
	 */
	private int entityID;
	
	/**
	 * The new position of the entity.
	 */
	private Vector2D newPosition;
	
	/**
	 * Creates the message that the specified entity has moved to the given position.
	 * 
	 * @param entityID the id of the entity that has moved
	 * @param newPosition the new position held by the entity
	 */
	public EntityMovedMessage(int entityID, Vector2D newPosition) {
		this.entityID = entityID;
		this.newPosition = newPosition;
	}
	
	/**
	 * Creates the message that an entity has moved to the given position.
	 * 
	 * @param data the byte array that holds the ID and the new position coordinates. 
	 * The first 4 bytes are the ID of the entity. The second 4 bytes are the X coordinate
	 *  of the position. The last 4 bytes are the Y coordinate of the position.
	 */
	public EntityMovedMessage(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		
		entityID = buffer.getInt();
		newPosition = new Vector2D(buffer.getFloat(), buffer.getFloat());
	}
	
	/**
	 * Gets the ID of the entity moved.
	 * 
	 * @return The ID of the moved entity.
	 */
	public int getEntityID(){
		return entityID;
	}
	
	/**
	 * Gets the new position of the entity.
	 * 
	 * @return The position vector where the entity is now at.
	 */
	public Vector2D getNewPosition(){
		return newPosition;
	}
	
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES*3);
		buffer.putInt(entityID);
		buffer.putFloat(newPosition.getX());
		buffer.putFloat(newPosition.getY());
		return buffer.array();
	}

	@Override
	public String toString(){
		return "[Entity]: Entity with the id \"" + entityID + "\" moved to " + newPosition;
	}
}
