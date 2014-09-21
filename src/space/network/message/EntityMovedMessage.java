package space.network.message;

import java.nio.ByteBuffer;

import space.math.Vector2D;


public class EntityMovedMessage implements Message {

	private int entityID;
	private Vector2D newPosition;
	
	public EntityMovedMessage(int entityID, Vector2D newPosition) {
		this.entityID = entityID;
		this.newPosition = newPosition;
	}
	
	public EntityMovedMessage(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		
		entityID = buffer.getInt();
		newPosition = new Vector2D(buffer.getFloat(), buffer.getFloat());
	}
	
	public int getEntityID(){
		return entityID;
	}
	
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
