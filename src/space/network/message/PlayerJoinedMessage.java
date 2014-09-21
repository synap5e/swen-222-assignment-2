package space.network.message;

import java.nio.ByteBuffer;

/**
 * PlayerJoingedMessage is message used for telling a client that a player has joined the game.
 * The message composes of the ID of the new player. This message is also used for confirming that
 *  to a client that it has joined as well as assigning it's player with an ID.
 * 
 * @author James Greenwood-Thessman (greenwjame1)
 */
public class PlayerJoinedMessage implements Message {

	/**
	 * The ID of the player who joined
	 */
	private int playerId;
	
	/**
	 * Creates the message that a player joined.
	 * 
	 * @param playerId the ID of the player that joined
	 */
	public PlayerJoinedMessage(int playerId) {
		this.playerId = playerId;
	}
	
	/**
	 * Creates the message that a player joined.
	 * 
	 * @param playerId the byte array containing the ID of the player that joined
	 */
	public PlayerJoinedMessage(byte[] playerId) {
		this.playerId = ByteBuffer.wrap(playerId).getInt();
	}
	
	/**
	 * Gets the ID of the player that joined.
	 * 
	 * @return The ID of the player.
	 */
	public int getPlayerID(){
		return playerId;
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
