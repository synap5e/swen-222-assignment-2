package space.world;

import static org.junit.Assert.*;

import org.junit.Test;

import space.math.Vector2D;

public class PlayerTest {
	@Test
	public void withinReachTrue1(){
		Player p = new Player(new Vector2D(10,10), 0, "");
		assertTrue(p.withinReach(new Vector2D(11,11)));
	}
	
	@Test
	public void withinReachTrue2(){
		Player p = new Player(new Vector2D(10,10), 0, "");
		assertTrue(p.withinReach(new Vector2D(13,11)));
	}
	
	@Test
	public void withinReachFalse1(){
		Player p = new Player(new Vector2D(10,10), 0, "");
		assertFalse(p.withinReach(new Vector2D(100,100)));
	}
	
	@Test
	public void withinReachFalse2(){
		Player p = new Player(new Vector2D(10,10), 0, "");
		assertFalse(p.withinReach(new Vector2D(20,15)));
	}
}
