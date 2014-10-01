package space.network;

/**
 * ClientListener enables the client to be listened to for critical events. 
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public interface ClientListener {
	
	/**
	 * Called when the connection to the server closes. 
	 * Can be from a connection failure or the server shutting down.
	 * 
	 * @param reason the reason for the disconnect
	 */
	public void onConnectionClose(String reason);
}
