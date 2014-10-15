package space.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a Concave hull object from a list of points in a clockwise direction
 * @author Simon Pinfold (300280028)
 *
 */
public class ConcaveHull implements Iterable<Segment2D>{

	private List<Vector2D> hullPoints;

	/** This point will always be outside the hull */
	private Vector2D outside = new Vector2D(0,0);

	private Vector2D topLeft;
	private Vector2D bottomRight;
	private Vector2D centre;

	/** Construct the hull from the specified points, winding clockwise
	 * @param points
	 */
	public ConcaveHull(List<Vector2D> points){
		this.hullPoints = new ArrayList<Vector2D>(points);
		this.centre = new Vector2D(0,0);
		topLeft = new Vector2D(points.get(0));
		bottomRight = new Vector2D(points.get(0));
		for (Vector2D p : points){
			if (p.getX() <= outside.getX()){
				outside.setX(p.getX()-1);
			}
			if (p.getY() <= outside.getY()){
				outside.setY(p.getY()-1);
			}

			if (topLeft.getX() > p.getX()){
				topLeft.setX(p.getX());
			}
			if (topLeft.getY() > p.getY()){
				topLeft.setY(p.getY());
			}
			if (bottomRight.getX() < p.getX()){
				bottomRight.setX(p.getX());
			}
			if (bottomRight.getY() < p.getY()){
				bottomRight.setY(p.getY());
			}



			centre.addLocal(p);
		}
		centre.divLocal(points.size());
	}
	/**
	 * checks whether a 2D Vector point is contained within
	 * @param point
	 * @return
	 */
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
	/**
	 * checks whether a line segment intersects with a point in the line of the hull
	 * @param ray the line
	 * @return
	 */
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

	/** Gets the centre, defined by the average of all points on the hull
	 *
	 * @return the centre of the hull
	 */
	public Vector2D getCentre(){
		return this.centre;
	}

	/**
	 * Returns string representation
	 * @return String
	 */
	public String toString(){
		String s = this.hullPoints.toString();
		return "Hull2(" + s.substring(1, s.length()-1) + ")";
	}
	/**
	 * returns the top left axis aligned bounding box
	 * @return
	 */
	public Vector2D getAABBTopLeft() {
		return topLeft;
	}

	/**
	 * returns the bottom right axis aligned bounding box
	 * @return
	 */
	public Vector2D getAABBBottomRight() {
		return bottomRight;
	}

	/**
	 * return the amount of points in hull
	 * @return
	 */
	public int size() {
		return hullPoints.size();
	}

	/**
	 * returns the 2D line segment connecting the point from point i and point i + 1
	 * @param i index of first point in segment
	 * @return
	 */
	public Segment2D get(int i) {
		if (i == 0){
			return new Segment2D(hullPoints.get(hullPoints.size()-1), hullPoints.get(i));
		} else {
			return new Segment2D(hullPoints.get(i-1), hullPoints.get(i));
		}
	}
	/**
	 * returns whether the circle with radius from point position is fully contained with hull
	 * @param position centre of circle
	 * @param radius radius of circle
	 * @return
	 */
	public boolean contains(Vector2D position, float radius) {
		return contains(position) && getClosestPointOnHull(position).sub(position).sqLen() > radius*radius;
	}

	/**
	 * returns closest point
	 * @param p the position being compared
	 * @return closest point
	 */
	public Vector2D getClosestPointOnHull(Vector2D p){
		Vector2D closest = null;
		for (Segment2D s : this){
			Vector2D closestPointOnLine;

			float xM = s.end.getX() - s.start.getX();
			float yM = s.end.getY() - s.start.getY();

			float u = ((p.getX() - s.start.getX()) * xM + (p.getY() - s.start.getY()) * yM) / (xM * xM + yM * yM);

			if (u < 0) {
				closestPointOnLine = new Vector2D(s.start.getX(), s.start.getY());
			} else if (u > 1) {
				closestPointOnLine = new Vector2D(s.end.getX(), s.end.getY());
			} else {
				closestPointOnLine = new Vector2D(s.start.getX() + u * xM, s.start.getY() + u * yM);
			}

			if (closest == null || closest.sub(p).sqLen() > closestPointOnLine.sub(p).sqLen()){
				closest = closestPointOnLine;
			}
		}
		return closest;
	}
	/**
	 * gets list of points making up hull
	 * @return
	 */
	public List<Vector2D> getDefiningPoints(){
		return Collections.unmodifiableList(this.hullPoints);
	}

}
