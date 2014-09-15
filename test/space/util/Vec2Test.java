package space.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class Vec2Test {
	
	private static final float EPSILON = 1f;
	
	@Test
	public void testBearingReflexive(){
		Vec2 v = new Vec2(100f, 47.5f);
		
		float t = v.getPolarAngle();
		float r = v.len();
		
		Vec2 fp = Vec2.fromPolar(t, r);
		
		assertTrue("expected " + v + " to be within " + EPSILON + " for all values, but was" + fp, v.equals(fp, EPSILON));
	}

}
