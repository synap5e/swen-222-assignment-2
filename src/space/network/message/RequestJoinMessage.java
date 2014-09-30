package space.network.message;

import java.nio.ByteBuffer;

/**
 * RequestJoinMessage is the message used by a client, specifying that the player wants to join the game.
 * The message composes of the previous ID of the player. If the player does not have a previous ID then the value -1 is sent instead.
 * 
 * @author James Greenwood-Thessman (greenwjame1)
 */
public class RequestJoinMessage implements Message {

	/**
	 * The previous ID of the player requesting to join. -1 if there is no previous ID.
	 */
	private int prevId;
	
	/**
	 * Creates the request to join the game. 
	 * 
	 * @param prevId the previous ID of the player. -1 if the player does not have a previous ID
	 */
	public RequestJoinMessage(int prevId) {
		this.prevId = prevId;
	}
	
	/**
	 * Creates the request to join the game.
	 * 
	 * @param data the byte array containing the previous ID of the player
	 */
	public RequestJoinMessage(byte[] data){
		ByteBuffer buffer = ByteBuffer.wrap(data);
		prevId = buffer.getInt();
	}
	
	/**
	 * Gets the previous ID of the player that is requesting to join. -1 if the player does not have a previous ID.
	 * 
	 * @return The previous ID of the player.
	 */
	public int getPreviousID(){
		return prevId;
	}
	
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE);
		buffer.putInt(prevId);
		
		return buffer.array();
	}

}
