package space.world;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import space.math.Vector2D;


public class ContainerTest { 
	@Test
	public void cannotPutInside(){//putting container inside itself
		Wallet w = new Wallet(null, 0, 0, "", "", true, new ArrayList<Pickup>());
		assertFalse(w.canPutInside(w));
	}
	
	@Test
	public void cannotPutInside1(){//putting something bigger than the container
		Wallet w = new Wallet(null, 0, 0, "", "", true, new ArrayList<Pickup>());
		assertFalse(w.canPutInside(new Table(null, 0, 0, null, null)));
	}
	
	@Test
	public void canPutInside(){
		Wallet w = new Wallet(null, 0, 0, "", "", true, new ArrayList<Pickup>());
		Key k = new Key(null, 0, 0, null, null);
		assertTrue(w.canPutInside(k));
	}
	
	@Test
	public void canPutInside1(){//putting a container inside another
		Chest c = new Chest(null, 0, 0, "", "",false,null, true, new ArrayList<Pickup>());
		Wallet w = new Wallet(null, 0, 0, null, null);
		assertTrue(c.canPutInside(w));
	}
	
	@Test
	public void validOpen(){//no lock
		Chest c = new Chest(null,0,0,"","",false,null);
		Player p = new Player(null,0,"");
		assertTrue(c.openClose(p));
		assertTrue(c.isOpen());
	}
	
	@Test
	public void validOpen1(){//player has key
		Key k = new Key(null, 0, 0, null, null);
		Chest c = new Chest(null,0,0,"","",true,k);
		Player p = new Player(null,0,"");
		p.pickup(k);
		assertTrue(c.openClose(p));
		assertTrue(c.isOpen());
	}
	
	@Test
	public void invalidOpen(){ //trying to open locked container with no key
		Key k = new Key(null, 0, 0, null, null);
		Chest c = new Chest(null,0,0,"","",true,k);
		Player p = new Player(null,0,"");
		assertFalse(c.openClose(p));
		assertFalse(c.isOpen());
	}
	
	@Test 
	public void interact(){
		Chest c = new Chest(null,0,0,"","",false,null);
		Player p = new Player(null,0,"");
		assertTrue(c.interact(p, new World()));
		assertTrue(c.isOpen());
	}
	
	@Test
	public void interact1(){
		Room r = new Room(null, 0, "",Arrays.asList(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0)));
		Wallet w = new Wallet(new Vector2D(0,0), 0, 0, null, null);
		Player p = new Player(new Vector2D(1,1),0,"");
		p.setRoom(r);
		r.putInRoom(w);
		assertTrue(w.interact(p, new World()));
		assertEquals(w,p.getInventory().get(0));
	}
	
}