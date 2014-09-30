package space.network.message;

import java.nio.ByteBuffer;

import space.math.Vector2D;

//TODO: Create actual class to rotate entities, not just players
public class PlayerRotatedMessage implements Message {

	private Vector2D mouseDelta;
	private int id;
	
	public PlayerRotatedMessage(int id, Vector2D change) {
		mouseDelta = change;
		this.id = id;
	}
	
	public PlayerRotatedMessage(byte[] data){
		ByteBuffer buffer = ByteBuffer.wrap(data);
		id = buffer.getInt();
		mouseDelta = new Vector2D(buffer.getFloat(), buffer.getFloat());
	}
	
	public Vector2D getDelta(){
		return mouseDelta;
	}
	
	public int getID(){
		return id;
	}
	
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate((Integer.SIZE+Float.SIZE*2)/8);
		buffer.putInt(id);
		buffer.putFloat(mouseDelta.getX());
		buffer.putFloat(mouseDelta.getY());
		return buffer.array();
	}

}
