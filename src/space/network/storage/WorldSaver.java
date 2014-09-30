package space.network.storage;

import java.util.List;
import space.world.Player;
import space.world.World;

/**
 * This interface specifies how the server will save the world.
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public interface WorldSaver {
	
	/**
	 * Saves the world. 
	 * 
	 * @param savePath the path of the save file(s)
	 * @param world the world to save
	 * @param players any additional players that need to be saved but don't exist in the world
	 */
	public void saveWorld(String savePath, World world, List<Player> players);
}
