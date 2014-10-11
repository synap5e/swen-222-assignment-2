package space.network.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.math.Vector2D;
import space.math.Vector3D;
import space.world.Container;
import space.world.Door;
import space.world.Key;
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
		Room r = new Room(new Vector3D(.4f, .4f, .4f), 1, "temp", Arrays.asList(new Vector2D(-20, 20), new Vector2D(20, 20), new Vector2D(20, -20), new Vector2D(-20, -20)));
		world.addRoom(r);
		Room r2 = new Room(new Vector3D(.1f, .01f, .01f), 2, "dark", Arrays.asList(new Vector2D(-20, -20), new Vector2D(20, -20), new Vector2D(20, -40), new Vector2D(-20, -40)));
		world.addRoom(r2);
		Key k = new Key(new Vector2D(5f, 5f), 4, 0, "A key", "Key");
		Container c = new Container(new Vector2D(-5,-5), 90, 0, "holds the key", "teapot", false, null){

			@Override
			public float getAngle() {
				return 45;
			}

			@Override
			public boolean canClip() {
				return true;
			}
			
			@Override
			public void update(int delta) {
			}

			@Override
			public float getCollisionRadius() {
				return 3;
			}

			@Override
			public float getHeight() {
				return 100;
			}
			
		};
		Door d = new Door(new Vector2D(0, -20), 3, "It can be opened", "Door", r, r2, false, true, k);
		r.addDoor(3, d);
		r2.addDoor(1, d);
		world.addEntity(d);
		world.addEntity(k);
		world.addEntity(c);
		//r.putInRoom(k);
		c.interact(null, world); //open
		c.putInside(k);
		r.putInRoom(c);
		c.interact(null, world); //close
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

	@Override
	public void loadWorldFromString(String world) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String representWorldAsString(World world) {
		// TODO Auto-generated method stub
		return null;
	}

}
