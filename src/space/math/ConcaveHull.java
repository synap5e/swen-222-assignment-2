package space.math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConcaveHull implements Iterable<Segment2D>{
	
	private List<Vector2D> hullPoints;
	
	/** This point will always be outside the hull */
	private Vector2D outside = new Vector2D(0,0);

	private Vector2D centre;
	
	/** Construct the hull from the specified points, winding clockwise
	 * @param points
	 */
	public ConcaveHull(List<Vector2D> points){
		this.hullPoints = new ArrayList<Vector2D>(points);
		this.centre = new Vector2D(0,0);
		for (Vector2D p : points){
			if (p.getX() <= outside.getX()){
				outside.setX(p.getX()-1);
			}
			if (p.getY() <= outside.getY()){
				outside.setY(p.getY()-1);
			}
			centre.addLocal(p);
		}
		centre.divLocal(points.size());
	}
	
	public boolean contains(Vector2D point){
		// crossing number algorithm
		
		Segment2D ray = new Segment2D(point, outside);
		
		// crossing number fails if the ray intersects one of the hull points
		// if this happens we can correct it by randomly moving the end of the 
		// ray - we try doing this 10 times and give up after this
		// this should catch 99.999% of cases in the first few iterations
		// but prevents the possibility of a deadlock
		for (int i=0;i<10 && intersectsPointOnHull(ray); i++){
			ray = new Segment2D(point, outside.sub(new Vector2D((float) Math.random()/10f, (float) Math.random()/10f)));
		}
		
		int intersections = 0;
		for (Segment2D edge : this){
			if (ray.intersects(edge)) ++intersections;
		}
		return intersections % 2 == 1;
	}

	private boolean intersectsPointOnHull(Segment2D ray) {
		for (Vector2D pointOnHull : hullPoints){
			if (ray.onLine(pointOnHull)) return true;
		}
		return false;
	}

	@Override
	public Iterator<Segment2D> iterator() {
		ArrayList<Segment2D> it = new ArrayList<Segment2D>();
		Vector2D prev = hullPoints.get(hullPoints.size()-1);
		for (Vector2D point : hullPoints){
			it.add(new Segment2D(prev, point));
			prev = point;
		}
		return it.iterator();
	}
	//TODO need to get centre of the shape
	//either implement getBounds() method which returns a Rectangle 
	//then I'll call getCenter() on the rectangle
	//or implement getCenter() directly
	
	/** Gets the centre, defined by the average of all points on the hull
	 * 
	 * @return the centre of the hull
	 */
	public Vector2D getCentre(){
		return this.centre;
	}
	
	public String toString(){
		String s = this.hullPoints.toString();
		return "Hull2(" + s.substring(1, s.length()-1) + ")";
	}
	
}