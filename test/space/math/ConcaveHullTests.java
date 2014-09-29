package space.math;

import java.util.ArrayList;
import java.util.Iterator;

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
	
	@Test
	public void iteratorEqualsIteration(){
		ConcaveHull h = createHull(
				0,	1, 	
				1,	1,
				1,	0,
				2,  0, 
				0, 	2,
				5,  1				
			);
		Iterator<Segment2D> it = h.iterator();
		for (int i=0;i<h.size();i++){
			assertEquals(it.next(), h.get(i));
		}
		assertFalse(it.hasNext());
	}
	
	@Test
	public void containsCircleTrue1(){
		assertTrue(hullContains(createHull(
					0,	1, 	
					1,	1,
					1,	0,
					0,	0
				), 
					0.5f, 0.5f, 0.2f
				));
	}
	
	@Test
	public void containsCircleTrue2(){
		assertTrue(hullContains(createHull(
					0,	1, 	
					1,	1,
					1,	0,
					0,	0
				), 
					0.2f, 0.2f, 0.1f
				));
	}
	
	@Test
	public void containsCircleFalse1(){
		assertFalse(hullContains(createHull(
					0,	1, 	
					1,	1,
					1,	0,
					0,	0
				), 
					0.5f, 0.5f, 0.6f
				));
	}
	
	@Test
	public void containsCircleFalse2(){
		assertFalse(hullContains(createHull(
					0,	1, 	
					1,	1,
					1,	0,
					0,	0
				), 
					0.2f, 0.2f, 0.25f
				));
	}
	
	

	private boolean hullContains(ConcaveHull hull, float x, float y, float radius) {
		return hull.contains(new Vector2D(x, y), radius);
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
