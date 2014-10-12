package space.network.message;

import java.nio.ByteBuffer;

import space.math.Vector2D;

//TODO: Create actual class to rotate entities, not just players
/**
 * EntityRotationMessage represents the message tell the client/server what the rotation of an entity is. 
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class EntityRotationMessage implements Message {

	
	private float xRotation;
	private float yRotation;
	
	private int id;
	
	public EntityRotationMessage(int id, float xRotation, float yRotation) {
		this.xRotation = xRotation;
		this.yRotation = yRotation;
		this.id = id;
	}
	
	public EntityRotationMessage(byte[] data){
		ByteBuffer buffer = ByteBuffer.wrap(data);
		id = buffer.getInt();
		xRotation = buffer.getFloat();
		yRotation = buffer.getFloat();
	}
	
	public float getXRotation(){
		return xRotation;
	}
	
	public float getYRotation(){
		return yRotation;
	}
	
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

}
