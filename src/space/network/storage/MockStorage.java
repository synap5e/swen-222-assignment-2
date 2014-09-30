package space.network.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.math.Vector2D;
import space.world.Door;
import space.world.Player;
import space.world.Room;
import space.world.World;

/**
 * This class is temporary 
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class MockStorage implements WorldLoader, WorldSaver {

	private World world;
	
	@Override
	public void loadWorld(String savePath) {
		world = new World();
		Room r = new Room(LightMode.BASIC_LIGHT, 1, "temp", Arrays.asList(new Vector2D(-20, 20), new Vector2D(20, 20), new Vector2D(20, -20), new Vector2D(-20, -20)), new HashMap<Integer, List<Door>>());
		world.addRoom(r);
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public List<Player> getPlayers() {
		//Not implemented in this mock object
		return new ArrayList<Player>();
	}

	@Override
	public void saveWorld(String savePath, World world, List<Player> players) {
		//Not implemented in this mock object
	}

}
