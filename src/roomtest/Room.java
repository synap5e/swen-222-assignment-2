package roomtest;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {

	
	private List<Wall> walls;
	
	public Room(List<Wall> walls) {
		this.walls = walls;
	}
	
	
	private static Random random = new Random();
	public static Room generateRandomRoom(float xo, float yo) {
		ArrayList<Point2D.Float> points = new ArrayList<Point2D.Float>(200);
		for (int i=0;i<500;i++){
			points.add(new Point2D.Float((float)(xo + random.nextGaussian() * 5), (float)(yo + random.nextGaussian() * 5)));
		}
		
		return new Room(giftWrap(points));
	}
	/*
	 
	 jarvis(S)
	   pointOnHull = leftmost point in S
	   i = 0
	   repeat
	      P[i] = pointOnHull
	      endpoint = S[0]         // initial endpoint for a candidate edge on the hull
	      for j from 1 to |S|
	         if (endpoint == pointOnHull) or (S[j] is on left of line from P[i] to endpoint)
	            endpoint = S[j]   // found greater left turn, update endpoint
	      i = i+1
	      pointOnHull = endpoint
	   until endpoint == P[0]      // wrapped around to first hull point
	 
	 */
	private static List<Wall> giftWrap(List<Point2D.Float> pts){
		Point2D.Float pointOnHull = getLeftMost(pts);
		Point2D.Float endpoint = null;
		//int i=0;
		
		ArrayList<Point2D.Float> hull = new ArrayList<Point2D.Float>();
		do{
			hull.add(pointOnHull);
			endpoint = pts.get(0);
			for (Point2D.Float point : pts){
				if (endpoint == pointOnHull || pointLeftOfLine(point, pointOnHull, endpoint)){
					endpoint = point;
				}
			}
			//i++;
			pointOnHull = endpoint;
			
		} while (endpoint != hull.get(0));
		
		
		ArrayList<Wall> walls = new ArrayList<Wall>();
		Point2D.Float prev = hull.get(hull.size()-1);
		for (Point2D.Float point : hull){
			walls.add(new Wall(prev.x, prev.y, point.x, point.y));
			prev = point;
		}
		
		return walls;
	}
	private static boolean pointLeftOfLine(Point2D.Float point, Point2D.Float lineStart, Point2D.Float lineEnd) {
		return 1 == Math.signum((lineEnd.x-lineStart.x)*(point.y-lineStart.y) - (lineEnd.y-lineStart.y)*(point.x-lineStart.x));
	}
	private static Point2D.Float getLeftMost(List<Point2D.Float> pts) {
		Point2D.Float l = pts.get(0);
		for (Point2D.Float p : pts){
			if (p.x < l.x) l=p;
		}
		return l;
	}
	public List<Wall> getWalls() {
		return walls;
	}
	
	
}
