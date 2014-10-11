package space.serialization;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import space.math.Vector2D;
import space.network.storage.MockStorage;
import space.network.storage.WorldLoader;
import space.world.Container;
import space.world.Entity;
import space.world.Pickup;
import space.world.Player;
import space.world.Room;
import space.world.World;

public class StorageTests {

	private static final int PLAYER_ID = 9001;
	
	@Test
	public void testSaveAndLoad() {
		String savepath = "testSaveAndLoad";
		
		World w = createTestWorld();
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
	public void testStringRepresentionSaveAndLoad() {
		World w = createTestWorld();
		String json = new ModelToJson().representWorldAsString(w);
		
		WorldLoader loader = new JsonToModel();
		loader.loadWorldFromString(json);
		World loaded = loader.getWorld();
		List<Player> ps = loader.getPlayers();
		assertEquals(1, ps.size());
		assertEquals(PLAYER_ID, ps.get(0).getID());
		match(w, loaded, false);
	}

	private World createTestWorld(){
		MockStorage loader = new MockStorage();
		loader.loadWorld("");
		World w = loader.getWorld();
		Player p = new Player(new Vector2D(0, 0), PLAYER_ID, "Player");
		System.out.println(w.getRoomAt(p.getPosition()));
		p.setRoom(w.getRoomAt(p.getPosition()));
		p.getRoom().putInRoom(p);
		w.addEntity(p);
		return w;
	}
	
	private void match(World original, World loaded, boolean allowPlayers){
		for (Room r : original.getRooms().values()){
			Room other = loaded.getRoom(r.getID());
			assertNotNull(other);
			for (Entity e : r.getEntities()){
				if (!(e instanceof Player) || allowPlayers){
					Entity oe = loaded.getEntity(e.getID());
					assertTrue(other.containsEntity(oe));
					assertEquals(e.getClass(), oe.getClass());
					if (e instanceof Container){
						Container c = (Container) e;
						Container oc = (Container) oe;
						assertEquals(c.getItemsContained().size(), oc.getItemsContained().size());
						for (int i = 0; i < c.getItemsContained().size(); ++i){
							Entity p = (Entity) c.getItemsContained().get(i);
							Entity op = (Entity) oc.getItemsContained().get(i);
							assertEquals(p.getID(), op.getID());
							assertEquals(p.getClass(), op.getClass());
						}
					}
				}
			}
		}
	}
}
