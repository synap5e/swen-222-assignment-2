package space.util;

import org.junit.Test;
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
		
		Vec2 a1 = new Vec2(ax1, ay1);
		Vec2 a2 = new Vec2(ax2, ay2);
		Segment2 a = new Segment2(a1, a2);
		
		return a.onLine(new Vec2(px, py));
	}


	private boolean checkIntersection(float ax1, float ay1, float ax2, float ay2,
									  float bx1, float by1, float bx2, float by2) {
		Vec2 a1 = new Vec2(ax1, ay1);
		Vec2 a2 = new Vec2(ax2, ay2);
		
		Vec2 b1 = new Vec2(bx1, by1);
		Vec2 b2 = new Vec2(bx2, by2);
		
		Segment2 a = new Segment2(a1, a2);
		Segment2 b = new Segment2(b1, b2);
		
		assertEquals(a.intersects(b), b.intersects(a));
		
		return a.intersects(b);
	}
	
}
