package space.network.message;

import java.nio.ByteBuffer;

/**
 * DisconnectMessage is the message used for informing a client that a player has disconnected. 
 *  It is also used by the client to tell the server that is it disconnecting.
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class DisconnectMessage implements Message {

	/**
	 * The ID of the player who has disconnected.
	 */
	private int playerId;
	
	/**
	 * Creates the message that a player has disconnected.
	 * 
	 * @param playerId the ID of the player that has disconnected.
	 */
	public DisconnectMessage(int playerId) {
		this.playerId = playerId;
	}
	
	/**
	 * Creates the message that a player has disconnected.
	 * 
	 * @param playerId the byte array containing the ID of the player that has disconnected
	 */
	public DisconnectMessage(byte[] playerId) {
		this.playerId = ByteBuffer.wrap(playerId).getInt();
	}
	
	/**
	 * Gets the ID of the player that has disconnected.
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
		return "[Player Disconnected]: Player with the id \"" + playerId + "\" has disconnected";
	}
}
