package space.util;

import org.junit.Test;

import space.math.Segment2D;
import space.math.Vector2D;
import static org.junit.Assert.*;

public class Segment2Test {

	@Test
	public void intersectionTrue(){
		/*   
			╲ ╱
			 ╳
			╱ ╲
		*/
		assertTrue(checkIntersection(
					0, 	0,
					1,	1,
					
					1,	0,
					0,	1
				));
		/*   
 		  	  ╱
			 ╱
			╱ 
			
			twice
		*/
		assertTrue(checkIntersection(
				0, 	0,
				1,	1,
				
				0,	0,
				1,	1
			));
	}
	
	
	@Test
	public void intersectionFalse(){
		/*   
		 		  	╱
			       ╱
			      ╱ 
		  
		  
		  
		  	  ╱
			 ╱
			╱ 
		*/
		assertFalse(checkIntersection(
					0, 	0,
					1,	1,
					
					2,	2,
					3,	3
				));
	}

	@Test
	public void onLineTest(){
		assertTrue(checkOnLine(
					0, 		0,
					1,		1,
					
					0.5f,	0.5f
				));
	}
	
	private boolean checkOnLine(float ax1, float ay1, float ax2, float ay2,
								float px, float py){
		
		Vector2D a1 = new Vector2D(ax1, ay1);
		Vector2D a2 = new Vector2D(ax2, ay2);
		Segment2D a = new Segment2D(a1, a2);
		
		return a.onLine(new Vector2D(px, py));
	}


	private boolean checkIntersection(float ax1, float ay1, float ax2, float ay2,
									  float bx1, float by1, float bx2, float by2) {
		Vector2D a1 = new Vector2D(ax1, ay1);
		Vector2D a2 = new Vector2D(ax2, ay2);
		
		Vector2D b1 = new Vector2D(bx1, by1);
		Vector2D b2 = new Vector2D(bx2, by2);
		
		Segment2D a = new Segment2D(a1, a2);
		Segment2D b = new Segment2D(b1, b2);
		
		assertEquals(a.intersects(b), b.intersects(a));
		
		return a.intersects(b);
	}
	
}
