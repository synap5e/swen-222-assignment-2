package space.serialization;

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
	 * Load the world from a save file. The world must not contain any players.
	 * 
	 * @param savePath the path of the save file
	 * @throws SaveFileNotAccessibleException 
	 * @throws SaveFileNotValidException 
	 */
	public void loadWorld(String savePath) throws SaveFileNotAccessibleException, SaveFileNotValidException;
	
	/**
	 * Load the world from a string representation the world. The world can contain players.
	 * 
	 * @param world the string representation of the world
	 */
	public void loadWorldFromString(String world);
	
	/**
	 * Get the world that has been loaded.
	 * One of the loadWorld methods must be called before calling this method.
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
