package space.util;

import java.util.ArrayList;

import org.junit.Test;

import static org.junit.Assert.*;

public class Hull2Tests {

	@Test
	public void containsTrue(){
		assertTrue(hullContains(createHull(
					0,	1, 	
					1,	1,
					1,	0,
					0,	0
				), 
					0.5f, 0.5f
				));
		
	}
	
	@Test
	public void containsFalse(){
		assertFalse(hullContains(createHull(
					0,	1, 	
					1,	1,
					1,	0,
					0,	0
				), 
					0.5f, 1.5f
				));
		
	}

	private boolean hullContains(Hull2 hull, float x, float y) {
		return hull.contains(new Vec2(x, y));
	}

	private Hull2 createHull(float... v) {
		assertTrue("Require an even number of coordinates to create a hull", v.length%2==0);
		
		ArrayList<Vec2> winding = new ArrayList<>();
		for (int i=0;i<v.length/2;i++){
			winding.add(new Vec2(v[i*2], v[i*2+1]));
		}
		return new Hull2(winding);
	}
	
}
