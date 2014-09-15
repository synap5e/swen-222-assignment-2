package space.util;

import org.junit.Test;

import space.math.Vector2D;
import static org.junit.Assert.*;

public class Vec2Test {
	
	private static final float EPSILON = 1f;
	
	@Test
	public void testBearingReflexive(){
		Vector2D v = new Vector2D(100f, 47.5f);
		
		float t = v.getPolarAngle();
		float r = v.len();
		
		Vector2D fp = Vector2D.fromPolar(t, r);
		
		assertTrue("expected " + v + " to be within " + EPSILON + " for all values, but was" + fp, v.equals(fp, EPSILON));
	}

}
