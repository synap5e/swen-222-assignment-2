package space.network.message;

import java.nio.ByteBuffer;

/**
 * PlayerJoiningMessage is message used for telling a client or server that a player is joining the game.
 * The message composes of the ID of the new player. This message is also used for confirming that
 *  to a client that it has joined as well as assigning it's player with an ID. 
 *  When used by a client connecting to a server, the ID is the previous ID the client had. -1 is used when no previous ID exists.
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class PlayerJoiningMessage implements Message {

	/**
	 * The ID of the player who joined
	 */
	private int playerId;
	
	/**
	 * Creates the message that a player is joining the game.
	 * 
	 * @param playerId the ID of the player that is joining.
	 */
	public PlayerJoiningMessage(int playerId) {
		this.playerId = playerId;
	}
	
	/**
	 * Creates the message that a player is joining the game.
	 * 
	 * @param playerId the byte array containing the ID of the player that joined
	 */
	public PlayerJoiningMessage(byte[] playerId) {
		this.playerId = ByteBuffer.wrap(playerId).getInt();
	}
	
	/**
	 * Gets the ID of the player that is joining.
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
		return "[Player Joining]: Player Joined with the id \"" + playerId + "\"";
	}
}
