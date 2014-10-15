package space.world;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import space.math.Vector2D;
import space.math.Vector3D;

public class BeamTest {
	@Test
	public void beamUpdate(){
		Room r = new Room(null, 0, "" , Arrays.asList(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0)));
		Turret turret = new Turret(new Vector2D(0,0), 0, 0, null, null, r);
		Beam b = new Beam(new Vector2D(0,5), 0, 0, new Vector3D(0,0,0), turret);
		b.update(3*400);
		assertTrue(b.getRemainingLife() == 0);	
	}
	
	@Test
	public void beamShooterUpdate(){
		Room r = new Room(null, 0, "" , Arrays.asList(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0)));
		Turret turret = new Turret(new Vector2D(0,0), 0, 0, null, null, r);
		BeamShooter b = new BeamShooter(new Vector2D(0,5), 0, 0, null, null, r, turret);
		b.update(200);
		assertFalse(b.getAngle()==0);
	}
	
	@Test
	public void beamShooterInteract(){
		Room r = new Room(null, 0, "" , Arrays.asList(new Vector2D(0,10),new Vector2D(10,10),new Vector2D(10,0),new Vector2D(0,0)));
		Turret turret = new Turret(new Vector2D(0,0), 0, 0, null, null, r);
		BeamShooter b = new BeamShooter(new Vector2D(0,5), 0, 0, null, null, r, turret);
		b.interact(new Player(null,1,""), new World());
		assertTrue(b.isStopped());
	}
}
