package space.world;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Test;

import space.math.Vector2D;

public class RoomTest {
	private Key item = new Key(new Vector2D(0,0), 0, 0, "", "");
	@Test
	public void positionVacant1(){//no entities
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		assertTrue(r.isPositionVacant(new Vector2D(5,5), item));
	}

	@Test
	public void postitionVacant2(){//with some entity but it isnt in the position
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		Key k = new Key(new Vector2D(5,5), 1, 0, "","");
		r.putInRoom(k);
		assertTrue(r.isPositionVacant(new Vector2D(2,2), k));
	}

	@Test
	public void positionNotVacant1(){//an entity is in the position specified
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		Vector2D pos = new Vector2D(5,5);
		Key k = new Key(pos, 1, 0, "","");
		r.putInRoom(k);
		assertFalse(r.isPositionVacant(pos, item));
	}

	@Test
	public void positionNotVacant2(){//an entity is not on the position but it will collide with something
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		Key k = new Key(new Vector2D(5,5), 1, 0,"","");
		r.putInRoom(k);
		assertFalse(r.isPositionVacant(new Vector2D(3,2), item));
	}
	
	@Test
	public void collidedWithPlayerTrue(){
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		Vector2D pos = new Vector2D(5,5);
		Key k = new Key(pos, 1, 0,"","");
		Player p = new Player(pos, 0, "");
		r.putInRoom(p);
		assertEquals(p,r.collidedWithPlayer(k));
	}
	
	@Test
	public void collidedWithPlayerFalse(){
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		Key k = new Key(new Vector2D(5,5), 1, 0,"","");
		Player p = new Player(new Vector2D(10,10), 0, "");
		r.putInRoom(p);
		assertEquals(null,r.collidedWithPlayer(k));
	}
	
	@Test
	public void closestPlayer1(){//no players in room
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		assertEquals(null,r.closestPlayer(new Vector2D(5,5)));
	}
	
	@Test
	public void closestPlayer2(){//only one Player in room
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		Player p = new Player(new Vector2D(3,3), 0, "");
		r.putInRoom(p);
		assertEquals(p,r.closestPlayer(new Vector2D(5,5)));
	}
	
	@Test
	public void closestPlayer3(){//2 Players in room
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		Player p1 = new Player(new Vector2D(3,3), 0, "");
		Player p2 = new Player(new Vector2D(4,3), 0, "");
		r.putInRoom(p1);
		r.putInRoom(p2);
		assertEquals(p2,r.closestPlayer(new Vector2D(5,5)));
	}

	private Room createRoom(Vector2D ... roomPoints){
		return new Room(null, 0, "" , Arrays.asList(roomPoints));
	}
}
