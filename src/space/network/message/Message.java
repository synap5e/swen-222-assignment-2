package space.network.message;

/**
 * Represents any type of message that can be sent over the network as an integer array.
 * Classes implementing this interface are required to provide a constructor that takes
 *  an integer array in the same format as their return value from toIntArray().
 * 
 * @author James Greenwood-Thessman (greenwjame1)
 *
 */
public interface Message {
	
	/**
	 * Creates an integer array representation of the message.
	 * This integer array would be then be able to be sent over the network.
	 * 
	 * @return An integer array representing the message.
	 */
	public int[] toIntArray();
}
