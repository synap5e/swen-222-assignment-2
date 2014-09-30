package space.network.message;

import java.nio.ByteBuffer;

/**
 * JumpMessage is the message used for informing clients and the server that a player has jumped.
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class JumpMessage implements Message {

	/**
	 * The ID of the player who joined
	 */
	private int playerId;
	
	/**
	 * Creates the message that a player has jumped.
	 * 
	 * @param playerId the ID of the player that has jumped
	 */
	public JumpMessage(int playerId) {
		this.playerId = playerId;
	}
	
	/**
	 * Creates the message that a player has jumped.
	 * 
	 * @param playerId the byte array containing the ID of the player that has jumped
	 */
	public JumpMessage(byte[] playerId) {
		this.playerId = ByteBuffer.wrap(playerId).getInt();
	}
	
	/**
	 * Gets the ID of the player that has jumped.
	 * 
	 * @return The ID of the player.
	 */
	public int getPlayerID(){
		return playerId;
	}
	
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE/8);
		buffer.putInt(playerId);
		
		return buffer.array();
	}

	@Override
	public String toString(){
		return "[Player Jumped]: Player with the id \"" + playerId + "\" has jumped";
	}
}
