package space.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Hull2 implements Iterable<Segment2>{
	
	private List<Vec2> hullPoints;
	
	/** This point will always be outside the hull */
	private Vec2 outside = new Vec2(0,0);

	private Vec2 centre;
	
	/** Construct the hull from the specified points, winding clockwise
	 * @param points
	 */
	public Hull2(List<Vec2> points){
		this.hullPoints = new ArrayList<Vec2>(points);
		this.centre = new Vec2(0,0);
		for (Vec2 p : points){
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
	
	public boolean contains(Vec2 point){
		// crossing number algorithm
		Segment2 ray = new Segment2(point, outside);
		while (intersectsPointOnHull(ray)){
			ray = new Segment2(point, outside.sub(new Vec2((float) Math.random()/10f, (float) Math.random()/10f)));
		}
		
		int intersections = 0;
		for (Segment2 edge : this){
			if (ray.intersects(edge)) ++intersections;
		}
		return intersections % 2 == 1;
	}

	private boolean intersectsPointOnHull(Segment2 ray) {
		for (Vec2 pointOnHull : hullPoints){
			if (ray.onLine(pointOnHull)) return true;
		}
		return false;
	}

	@Override
	public Iterator<Segment2> iterator() {
		ArrayList<Segment2> it = new ArrayList<Segment2>();
		Vec2 prev = hullPoints.get(hullPoints.size()-1);
		for (Vec2 point : hullPoints){
			it.add(new Segment2(prev, point));
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
	public Vec2 getCentre(){
		return this.centre;
	}
	
	public String toString(){
		String s = this.hullPoints.toString();
		return "Hull2(" + s.substring(1, s.length()-1) + ")";
	}
	
}
