package space.network.storage;

import java.util.List;
import space.world.Player;
import space.world.World;

/**
 * This interface specifies how the server and client will load the world.
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public interface WorldLoader {
	
	/**
	 * Load the world from a save file.
	 * 
	 * @param savePath the path of the save file
	 */
	public void loadWorld(String savePath);
	
	/**
	 * Get the world that has been loaded. The world must not contain any players.
	 * The loadWorld method must be called before calling this method.
	 * 
	 * @return The loaded world.
	 */
	public World getWorld();
	
	/**
	 * Get the list of players that has been loaded. 
	 * The loadWorld method must be called before calling this method.
	 * 
	 * @return The list of players
	 */
	public List<Player> getPlayers();
	
}
