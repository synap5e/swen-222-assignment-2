package space.world;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import space.math.Vector2D;

public class TurretTest {
	@Test
	public void update(){
		Room r = new Room(null, 0, "",Arrays.asList(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0)));
		Turret turret = new Turret(new Vector2D(5,5), 0, 0,"","", r);
		Vector2D teleportTo = new Vector2D(0,0);
		TurretStrategyImpl strategy = new TurretStrategyImpl(turret,0,teleportTo,r);
		turret.setStrategy(strategy);
		Player p = new Player(new Vector2D(2,2), 0, "");
		r.putInRoom(p);
		p.setRoom(r);
		r.putInRoom(turret);
		r.update(5);
		assertTrue(turret.getAngle() != 0);
	}
	
	@Test
	public void shutDown(){
		Room r = new Room(null, 0, "",Arrays.asList(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0)));
		Turret turret = new Turret(new Vector2D(5,5), 0, 0,"","", r);
		TurretStrategyImpl strategy = new TurretStrategyImpl(turret,0,new Vector2D(5,5),r);
		turret.setStrategy(strategy);
		r.putInRoom(turret);
		turret.shutDown();
		assertFalse(r.containsEntity(turret));
	}
}
