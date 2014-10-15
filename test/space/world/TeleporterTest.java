package space.world;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import space.math.Vector2D;

public class TeleporterTest {
	@Test
	public void interact(){ //player on teleporter interacts with it
		Room r = new Room(null, 0, "",Arrays.asList(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0)));
		Room r2 = new Room(null, 1, "",Arrays.asList(new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30)));
		World w = new World();
		w.addRoom(r);
		w.addRoom(r2);
		Vector2D pos =  new Vector2D(50,50);
		Vector2D oldPos = new Vector2D(5,5);
		Teleporter t = new Teleporter(oldPos,pos, 0, 0, null, null, true);
		Player p = new Player(oldPos, 0, null);
		p.setRoom(r);
		r.putInRoom(p);
		t.interact(p, w);
		assertEquals(pos,p.getPosition());
		assertEquals(r2,p.getRoom());
	}
	
	public void interact2(){//interacted with another player not on the teleporter
		Room r = new Room(null, 0, "",Arrays.asList(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0)));
		Room r2 = new Room(null, 1, "",Arrays.asList(new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30)));
		World w = new World();
		w.addRoom(r);
		w.addRoom(r2);
		Vector2D pos =  new Vector2D(50,50);
		Vector2D interactorPos = new Vector2D(45,45);
		Vector2D oldPos = new Vector2D(5,5);
		Teleporter t = new Teleporter(oldPos,pos, 0, 0, null, null, true);
		Player p1 = new Player(oldPos, 0, null);
		Player p2 = new Player(interactorPos,1,null);
		p1.setRoom(r);
		p2.setRoom(r);
		r.putInRoom(p1);
		r.putInRoom(p2);
		t.interact(p2, w);
		assertEquals(pos,p1.getPosition());
		assertEquals(r2,p1.getRoom());
		assertEquals(interactorPos,p2.getPosition());
		assertEquals(r,p2.getRoom());
	}
}
