package space.network.message.sync;

import space.network.message.Message;
import space.world.World;

public interface SyncMessage extends Message {
	
	/**
	 * Synchronises the world with the contents of the message. 
	 * 
	 * @param world the world to synchronise
	 */
	public void applyTo(World world);
}
