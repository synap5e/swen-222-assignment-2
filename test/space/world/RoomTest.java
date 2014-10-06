package space.world;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.math.Vector2D;

public class RoomTest {
	@Test
	public void positionVacant1(){//no entities
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		assertTrue(r.isPositionVacant(new Vector2D(5,5), 2));
	}

	@Test
	public void postitionVacant2(){//with some entity but it isnt in the position
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		Key k = new Key(new Vector2D(5,5), 1, "", 0, null);
		r.putInRoom(k);
		assertTrue(r.isPositionVacant(new Vector2D(2,2), 1));
	}

	@Test
	public void positionNotVacant1(){//an entity is in the position specified
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		Vector2D pos = new Vector2D(5,5);
		Key k = new Key(pos, 1, "", 0, null);
		r.putInRoom(k);
		assertFalse(r.isPositionVacant(pos, 1));
	}

	@Test
	public void positionNotVacant2(){//an entity is not on the position but it will collide with something
		Room r  = createRoom(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0));
		Key k = new Key(new Vector2D(5,5), 1, "", 0, null);
		r.putInRoom(k);
		assertFalse(r.isPositionVacant(new Vector2D(3,2), 5));
	}

	private Room createRoom(Vector2D ... roomPoints){
		return new Room(LightMode.BASIC_LIGHT, 0, "" , Arrays.asList(roomPoints), new HashMap<Integer,List<Door>>());
	}
}
