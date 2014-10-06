package space.world;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.math.Vector2D;

public class DoorTest {
	private Room r1 = new Room(LightMode.BASIC_LIGHT, 0, "", Arrays.asList(new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30)), new HashMap<Integer, List<Door>>());
	private Room r2 =  new Room(LightMode.BASIC_LIGHT, 1, "", Arrays.asList(new Vector2D(60,60),new Vector2D(90,60),new Vector2D(90,30),new Vector2D(60,30)), new HashMap<Integer, List<Door>>());

	@Test
	public void validUnlock1(){
		Door d = new Door(new Vector2D(60,50), 2, "", r1, r2, false, true);
		Player p = new Player(new Vector2D(59,50), 3);
		p.pickup(new Key(new Vector2D(0,0), 0, null, 0, d));
		d.unlock(p);
		assertFalse("Door should be unlocked after player with key tries to unlock it",d.isLocked());
	}

	@Test
	public void validUnlock2(){
		Room r3 = new Room(LightMode.DARK, 100, "", Arrays.asList(new Vector2D(3,6),new Vector2D(6,6),new Vector2D(6,3),new Vector2D(3,3)), new HashMap<Integer, List<Door>>());
		Door d = new Door(new Vector2D(60,50), 2, "", r1, r2, false, true);
		Player p = new Player(new Vector2D(59,50), 3);
		p.pickup(new Key(new Vector2D(0,0), 0, null, 0, new Door(new Vector2D(1,1), 4,"", r1,r3,false,false)));
		p.pickup(new Key(new Vector2D(0,0), 0, null, 0, d));
		d.unlock(p);
		assertFalse("Door should be unlocked after player with key tries to unlock it",d.isLocked());
	}

	@Test
	public void invalidUnlock1(){//no key in inventory
		Door d = new Door(new Vector2D(60,50), 2, "", r1, r2, false, true);
		Player p = new Player(new Vector2D(59,50), 3);
		d.unlock(p);
		assertTrue("Door should still be locked because player has no key",d.isLocked());
	}

	@Test
	public void invalidUnlock2(){//player's key does not unlock door
		Room r3 = new Room(LightMode.DARK, 100, "", Arrays.asList(new Vector2D(3,6),new Vector2D(6,6),new Vector2D(6,3),new Vector2D(3,3)), new HashMap<Integer, List<Door>>());
		Door d = new Door(new Vector2D(60,50), 2, "", r1, r2, false, true);
		Player p = new Player(new Vector2D(59,50), 3);
		p.pickup(new Key(new Vector2D(0,0), 0, null, 0, new Door(new Vector2D(1,1), 4,"", r1,r3,false,false)));
		d.unlock(p);
		assertTrue("Door should still be locked because player has no key to the door",d.isLocked());
	}

	@Test
	public void otherRoom(){
		Door d = new Door(new Vector2D(60,50), 2, "", r1, r2, false, true);
		assertEquals(r1, d.otherRoom(r2));
		assertEquals(r2, d.otherRoom(r1));
	}

	@Test
	public void otherRoomOneWay(){
		Door d = new Door(new Vector2D(60,50), 2, "", r1, r2, true, true);
		assertEquals(r2,d.otherRoom(r1));
		assertEquals(null, d.otherRoom(r2));
	}

}
