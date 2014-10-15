package space.world;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Test;

import space.math.Vector2D;


public class DoorTest {
	private Room r1 = new Room(null, 0, "", Arrays.asList(new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30)));
	private Room r2 =  new Room(null, 1, "", Arrays.asList(new Vector2D(60,60),new Vector2D(90,60),new Vector2D(90,30),new Vector2D(60,30)));

	@Test
	public void validUnlock1(){
		Key k = new Key(new Vector2D(0,0), 0, 0, null, null);
		Door d = new Door(new Vector2D(60,50), 2, "", null, r1, r2, false, true, k,false);
		Player p = new Player(new Vector2D(59,50), 3,"");
		p.pickup(k);
		d.unlock(p);
		assertFalse("Door should be unlocked after player with key tries to unlock it",d.isLocked());
	}

	@Test
	public void validUnlock2(){
		Key k1 = new Key(new Vector2D(0,0), 0, 0, null, null);
		Key k2 = new Key(new Vector2D(0,0), 0,0, null, null);
		Door d = new Door(new Vector2D(60,50), 2, "", null, r1, r2, false, true,k2, false);
		Player p = new Player(new Vector2D(59,50), 3,"");
		p.pickup(k1);
		p.pickup(k2);
		d.unlock(p);
		assertFalse("Door should be unlocked after player with key tries to unlock it",d.isLocked());
	}

	@Test
	public void invalidUnlock1(){//no key in inventory
		Door d = new Door(new Vector2D(60,50), 2, "", "", r1, r2, false, true,new Key(new Vector2D(0,0), 0, 0, null, null), false);
		Player p = new Player(new Vector2D(59,50),3, "");
		d.unlock(p);
		assertTrue("Door should still be locked because player has no key",d.isLocked());
	}

	@Test
	public void invalidUnlock2(){//player's key does not unlock door
		Key k1 = new Key(new Vector2D(0,0), 0, 0, null, null);
		Key k2 = new Key(new Vector2D(0,0), 0,0, null, null);
		Door d = new Door(new Vector2D(60,50), 2, "", "", r1, r2, false, true,k1, false);
		Player p = new Player(new Vector2D(59,50), 3, "");
		p.pickup(k2);
		d.unlock(p);
		assertTrue("Door should still be locked because player has no key to the door",d.isLocked());
	}

	@Test
	public void otherRoom(){
		Door d = new Door(new Vector2D(60,50), 2, "", null, r1, r2, false, true,null, false);
		assertEquals(r1, d.otherRoom(r2));
		assertEquals(r2, d.otherRoom(r1));
	}

	@Test
	public void otherRoomOneWay(){
		Door d = new Door(new Vector2D(60,50), 2, "", null, r1, r2, true, true, null, false);
		assertEquals(r2,d.otherRoom(r1));
		assertEquals(null, d.otherRoom(r2));
	}

}
