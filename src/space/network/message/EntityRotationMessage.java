package space.network.message;

import java.nio.ByteBuffer;

/**
 * EntityRotationMessage is the message used to inform the client/server of the current rotation of an entity. 
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class EntityRotationMessage implements Message {

	/**
	 * The rotation of the entity around the x axis.
	 */
	private float xRotation;
	
	/**
	 * The rotation of the entity around the y axis.
	 */
	private float yRotation;
	
	/**
	 * The ID of the entity.
	 */
	private int id;
	
	/**
	 * Create the message specifying the entity's rotation.
	 * 
	 * @param id the ID of the entity
	 * @param xRotation the x rotation of the entity.
	 * @param yRotation the y rotation of the entity.
	 */
	public EntityRotationMessage(int id, float xRotation, float yRotation) {
		this.xRotation = xRotation;
		this.yRotation = yRotation;
		this.id = id;
	}
	
	/**
	 * Create the message specifying the entity's rotation.
	 * 
	 * @param data the byte array representing the message.
	 */
	public EntityRotationMessage(byte[] data){
		ByteBuffer buffer = ByteBuffer.wrap(data);
		id = buffer.getInt();
		xRotation = buffer.getFloat();
		yRotation = buffer.getFloat();
	}
	
	/**
	 * Gets the x rotation of the entity.
	 * 
	 * @return The x rotation.
	 */
	public float getXRotation(){
		return xRotation;
	}
	
	/**
	 * Gets the y rotation of the entity.
	 * 
	 * @return The y rotation.
	 */
	public float getYRotation(){
		return yRotation;
	}
	
	/**
	 * Gets the ID of the entity.
	 * 
	 * @return The ID of the entity.
	 */
	public int getID(){
		return id;
	}
	
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate((Integer.SIZE+Float.SIZE*2)/8);
		buffer.putInt(id);
		buffer.putFloat(xRotation);
		buffer.putFloat(yRotation);
		return buffer.array();
	}
	
	@Override
	public String toString(){
		return "[Rotation]: Entity with the id \"" + id + "\" has the rotation " + xRotation + " around the x axis and " + yRotation + " around the y axis";
	}

}
