package space.network.message;

import java.nio.ByteBuffer;

public class PlayerJoinedMessage implements Message {

	private int playerId;
	
	public PlayerJoinedMessage(int playerId) {
		this.playerId = playerId;
	}
	
	public PlayerJoinedMessage(byte[] playerId) {
		this.playerId = ByteBuffer.wrap(playerId).getInt();
	}
	
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		buffer.putInt(playerId);
		
		return buffer.array();
	}

	@Override
	public String toString(){
		return "[Player Joined]: Player Joined with the id \"" + playerId + "\"";
	}
}
