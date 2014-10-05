package space.network.message;

import java.nio.ByteBuffer;
import space.math.Vector2D;

public class DropPickupMessage implements Message {

	private int playerId;
	private int pickupId;
	private Vector2D position;

	public DropPickupMessage(int playerId, int pickupId, Vector2D position) {
		this.playerId = playerId;
		this.pickupId = pickupId;
		this.position = position;
	}
	
	public DropPickupMessage(byte[] data){
		ByteBuffer buffer = ByteBuffer.wrap(data);
		playerId = buffer.getInt();
		pickupId = buffer.getInt();
		position = new Vector2D(buffer.getFloat(), buffer.getFloat());
	}

	public int getPlayerId() {
		return playerId;
	}

	public int getPickupId() {
		return pickupId;
	}

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

}
