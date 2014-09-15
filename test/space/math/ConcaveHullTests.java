package space.math;

import java.util.ArrayList;

import org.junit.Test;

import space.math.ConcaveHull;
import space.math.Vector2D;
import static org.junit.Assert.*;

public class ConcaveHullTests {

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
	
	@Test
	public void getCentre(){
		assertEquals(createHull(
					-1,		 1,
					 1,		 1,
					 1,		-1,
					-1,		-1
				).getCentre(),
				new Vector2D(0, 0));
	}

	private boolean hullContains(ConcaveHull hull, float x, float y) {
		return hull.contains(new Vector2D(x, y));
	}

	private ConcaveHull createHull(float... v) {
		assertTrue("Require an even number of coordinates to create a hull", v.length%2==0);
		
		ArrayList<Vector2D> winding = new ArrayList<>();
		for (int i=0;i<v.length/2;i++){
			winding.add(new Vector2D(v[i*2], v[i*2+1]));
		}
		return new ConcaveHull(winding);
	}
	
}
