package space.network.message;

import java.awt.event.FocusAdapter;
import java.nio.ByteBuffer;

public class TransferMessage implements Message {

	private int entityId;
	private int playerId;
	private int containerId;
	private boolean fromPlayer;

	public TransferMessage(int entityId, int playerId, int containerId, boolean fromPlayer){
		this.entityId = entityId;
		this.playerId = playerId;
		this.containerId = containerId;
		this.fromPlayer = fromPlayer;
	}

	public TransferMessage(byte[] data) {
		byte tru = (byte) 1;
		ByteBuffer buffer = ByteBuffer.wrap(data);
		entityId = buffer.getInt();
		playerId = buffer.getInt();
		containerId = buffer.getInt();
		fromPlayer = (buffer.get() == tru);
	}

	public int getEntityID(){
		return entityId;
	}

	public int getPlayerId(){
		return playerId;
	}

	public int getContainerId(){
		return containerId;
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
