package space.math;

/**
 * Represents a 2D line segment
 *
 * @author Simon Pinfold (300280028)
 *
 */
public class Segment2D {

	public final Vector2D start;
	public final Vector2D end;

	/** tolerance for floating point errors */
	private final static float EPSILON = 0.01f;

	/**
	 * constructs a new line segment from v1 to v2
	 * @param v1 the start point of segment
	 * @param v2 the end point of a segment
	 */
	public Segment2D(Vector2D v1, Vector2D v2) {
		this.start = v1;
		this.end = v2;
	}
	/**
	 * checks whether or not given segment intersects
	 * @param other segment to check intersection with
	 * @return
	 */
	public boolean intersects(Segment2D other) {
		if (start.equals(other.start, EPSILON) || end.equals(other.end, EPSILON)) return true;

		// this line defined as (p, p+r)
		Vector2D p = start;
		Vector2D r = end.sub(start);

		// other line defined as (q, q+s)
		Vector2D q = other.start;
		Vector2D s = other.end.sub(other.start);

		// lines intersect if t and u exist in 0...1 for  p + t*r = q + u*s

		// t = (q - p) x s / (r x s)
		// u = (q - p) x r / (r x s)

		float rCrossS = r.cross(s);
		Vector2D qSubP = q.sub(p);
		float t = qSubP.cross(s) / rCrossS;
		float u = qSubP.cross(r) / rCrossS;

		return 0 <= t && t <= 1 && 0 <= u && u <= 1;
	}
	/**
	 * Gets the point where the two segments intersect
	 * @param other the other segment
	 * @return where the segments intersect
	 */
	public Vector2D getIntersection(Segment2D other) {
		if (start.equals(other.start, EPSILON)) return start;
		if (end.equals(other.end, EPSILON)) return end;

		// this line defined as (p, p+r)
		Vector2D p = start;
		Vector2D r = end.sub(start);

		// other line defined as (q, q+s)
		Vector2D q = other.start;
		Vector2D s = other.end.sub(other.start);

		// lines intersect if t and u exist in 0...1 for  p + t*r = q + u*s

		// t = (q - p) x s / (r x s)
		// u = (q - p) x r / (r x s)

		float rCrossS = r.cross(s);
		Vector2D qSubP = q.sub(p);
		float t = qSubP.cross(s) / rCrossS;
		float u = qSubP.cross(r) / rCrossS;

		return p.add(r.mul(t));
	}
	/**
	 * checks whether given point is on the line
	 * @param point
	 * @return
	 */
	public boolean onLine(Vector2D point){
		float x1 = start.getX();
		float y1 = start.getY();

		float x2 = end.getX();
		float y2 = end.getY();

		float x3 = point.getX();
		float y3 = point.getY();


		// check points are colinear
		float slope1 = (y2 - y1)*(x3 - x2);
		float slope2 = (y3 - y2)*(x2 - x1);
		// if slopes differ by more than EPSI then points are not collinear (enough)
		if (Math.abs(slope1 - slope2) > EPSILON) return false;

		float dot = end.sub(start).dot(point.sub(start));
		if (dot < 0) return false;

		if (dot > this.sqLen()) return false;

		return true;
	}
	/**
	 * returns length squared
	 * @return
	 */
	private float sqLen() {
		return	Math.abs(end.getX() - start.getX()) * Math.abs(end.getX() - start.getX()) +
				Math.abs(end.getY() - start.getY()) * Math.abs(end.getY() - start.getY());
	}
	/**
	 * returns length of segment
	 * @return
	 */
	public float len(){
		return (float) Math.sqrt(sqLen());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Segment2D other = (Segment2D) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}
	/**
	 * Returns a string representation
	 * @return String
	 */
	public String toString(){
		return "Segment2(" + start + ", " + end + ")";
	}
}
