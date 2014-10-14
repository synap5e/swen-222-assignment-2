package space.math;

import org.junit.Test;

import space.math.Vector2D;
import static org.junit.Assert.*;

public class Vector2DTests {
	
	private static final float EPSILON = 1f;
	
	@Test
	public void testBearingReflexive(){
		Vector2D v = new Vector2D(100f, 47.5f);
		
		float t = v.getPolarAngle();
		float r = v.len();
		
		Vector2D fp = Vector2D.fromPolar(t, r);
		
		assertTrue("expected " + v + " to be within " + EPSILON + " for all values, but was" + fp, v.equals(fp, EPSILON));
	}
	
	@Test
	public void testVectorAdd(){
		Vector2D v1 = new Vector2D(10f, 30f);
		Vector2D v2 = new Vector2D(20f, 40f);
		
		
		Vector2D result = v1.add(v2);
		
		assertTrue(result.equals(new Vector2D(30f, 70f)));
	}

	@Test
	public void testVectorSubtract(){
		Vector2D v1 = new Vector2D(10f, 30f);
		Vector2D v2 = new Vector2D(20f, 40f);
		
		
		Vector2D result = v1.sub(v2);
		
		assertTrue(result.equals(new Vector2D(-10f, -10f)));
	}
	
	@Test
	public void testVectorLocalAdd(){
		Vector2D v1 = new Vector2D(10f, 30f);
		Vector2D v2 = new Vector2D(20f, 40f);
		v1.addLocal(v2);

		assertTrue(v1.equals(new Vector2D(30f, 70f)));
	}
}
