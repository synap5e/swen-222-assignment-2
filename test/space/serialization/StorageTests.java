package space.serialization;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import space.math.Vector2D;
import space.math.Vector3D;
import space.world.Chest;
import space.world.Container;
import space.world.Door;
import space.world.Entity;
import space.world.Key;
import space.world.Player;
import space.world.Room;
import space.world.Teleporter;
import space.world.Turret;
import space.world.TurretStrategyImpl;
import space.world.World;

public class StorageTests {

	private static final int PLAYER_ID = 9001;

	@Test
	public void testSaveAndLoad() throws SaveFileNotAccessibleException,SaveFileNotValidException {
		String savepath = "testSaveAndLoad";

		World w = createTestWorldRoomsandDoorsandPlayer();
		new ModelToJson().saveWorld(savepath, w, new ArrayList<Player>());

		WorldLoader loader = new JsonToModel();
		loader.loadWorld(savepath);
		World loaded = loader.getWorld();
		List<Player> ps = loader.getPlayers();
		assertEquals(1, ps.size());
		assertEquals(PLAYER_ID, ps.get(0).getID());
		match(w, loaded, false);
	}

	@Test
	public void testStringRepresentionSaveAndLoad()throws SaveFileNotValidException {
		World w = createTestWorldRoomsandDoorsandPlayer();
		String json = new ModelToJson().representWorldAsString(w);

		WorldLoader loader = new JsonToModel();
		loader.loadWorldFromString(json);
		World loaded = loader.getWorld();
		List<Player> ps = loader.getPlayers();
		assertEquals(1, ps.size());
		assertEquals(PLAYER_ID, ps.get(0).getID());
		match(w, loaded, false);
	}
	
	@Test
	public void testTurretsandStrategy()throws SaveFileNotValidException {
		World w = createTestWorldTurretsandStratandTeleporter();
		String json = new ModelToJson().representWorldAsString(w);

		WorldLoader loader = new JsonToModel();
		loader.loadWorldFromString(json);
		World loaded = loader.getWorld();
		match(w, loaded, false);
	}
	
	@Test
	public void testException()throws SaveFileNotValidException {
		World w = createTestWorldRoomsandDoorsandPlayer();
		String json = new ModelToJson().representWorldAsString(w);
		WorldLoader loader = new JsonToModel();
		try{
		loader.loadWorldFromString("notjson");
		new AssertionError("this should be caught by exception");
		}
		catch(SaveFileNotValidException e) {
			assert true;
		}

	}

	private World createTestWorldRoomsandDoorsandPlayer() {
		World world = new World();
		Room r = new Room(new Vector3D(.4f, .4f, .4f), 1, "temp",
				Arrays.asList(new Vector2D(-20, 20), new Vector2D(20, 20),
						new Vector2D(20, -20), new Vector2D(-20, -20)));
		world.addRoom(r);
		Room r2 = new Room(new Vector3D(.1f, .01f, .01f), 2, "dark",
				Arrays.asList(new Vector2D(-20, -20), new Vector2D(20, -20),
						new Vector2D(20, -40), new Vector2D(-20, -40)));
		world.addRoom(r2);
		Key k = new Key(new Vector2D(5f, 5f), 4, 0, "A key", "Key");
		Chest c = new Chest(new Vector2D(-5, -5), 90, 0, "holds the key",
				"teapot", false, null);
		Door d = new Door(new Vector2D(0, -20), 3, "It can be opened", "Door",
				r, r2, false, true, k, true);
		r.addDoor(3, d);
		r2.addDoor(1, d);
		world.addEntity(d);
		world.addEntity(k);
		world.addEntity(c);
		// r.putInRoom(k);
		c.interact(null, world); // open
		c.putInside(k);
		r.putInRoom(c);
		c.interact(null, world); // close
		Player p = new Player(new Vector2D(0, 0), PLAYER_ID, "Player");
		System.out.println(world.getRoomAt(p.getPosition()));
		p.setRoom(world.getRoomAt(p.getPosition()));
		p.getRoom().putInRoom(p);
		world.addEntity(p);
		return world;
	}
	
	private World createTestWorldTurretsandStratandTeleporter() {
		World world = new World();
		Room r = new Room(new Vector3D(.2f, .4f, .7f), 1, "temp",
				Arrays.asList(new Vector2D(-20, 20), new Vector2D(20, 20),
						new Vector2D(20, -20), new Vector2D(-20, -20)));
		world.addRoom(r);
		Room r2 = new Room(new Vector3D(.1f, .01f, .01f), 2, "light",
				Arrays.asList(new Vector2D(-20, -20), new Vector2D(20, -20),
						new Vector2D(20, -40), new Vector2D(-20, -40)));
		world.addRoom(r2);
		Teleporter t = new Teleporter(new Vector2D(10, 15),new Vector2D(1, 5), 10, (float) 0.01, "A Teleporter", "Tele", true);
		Turret tur = new Turret(new Vector2D(2,3), 2, (float) 0.1,"pew pew", "turret", r2);
		TurretStrategyImpl ts = new TurretStrategyImpl(tur,10f,new Vector2D(1,2), r);
		tur.setStrategy(ts);
		world.addEntity(t);
		world.addEntity(tur);
		return world;
	}

	private void match(World original, World loaded, boolean allowPlayers) {
		for (Room r : original.getRooms().values()) {
			Room other = loaded.getRoom(r.getID());
			assertNotNull(other);
			for (Entity e : r.getEntities()) {
				if (!(e instanceof Player) || allowPlayers) {
					Entity oe = loaded.getEntity(e.getID());
					assertTrue("the room must contain the entity",other.containsEntity(oe));
					assertEquals("the clesses of the entities must be the same",e.getClass(), oe.getClass());
					if (e instanceof Container) {
						Container c = (Container) e;
						Container oc = (Container) oe;
						assertEquals("the size of container should be the same",c.getItemsContained().size(), oc
								.getItemsContained().size());
						for (int i = 0; i < c.getItemsContained().size(); ++i) {
							Entity p = (Entity) c.getItemsContained().get(i);
							Entity op = (Entity) oc.getItemsContained().get(i);
							assertEquals(p.getID(), op.getID());
							assertEquals(p.getClass(), op.getClass());
						}
					}
					if(e instanceof Turret){
						Turret t = (Turret) e;
						Turret ot = (Turret) oe;
						assertEquals("the ids should be the same",t.getID(),ot.getID());
						assertEquals("j","j");
						assertEquals("the strategies should be the same",t.getStrategy().hashCode(),ot.getStrategy().hashCode());
					}
					if(e instanceof Teleporter){
						Teleporter t = (Teleporter) e;
						Teleporter ot = (Teleporter) oe;
						assertEquals("the ids should be the same",t.getID(),ot.getID());
					}
				}
			}
		}
	}
}