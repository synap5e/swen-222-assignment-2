package space.math;

/**
 * represents a vector2D point object
 * @author Simon Pinfold (300280028)
 *
 */
public class Vector2D {

	private float x;
	private float y;
	/**
	 * construct a Vector2D point from given x and y
	 * @param x
	 * @param y
	 */
	public Vector2D(float x, float y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs new Vector2D from given Vector2D
	 * @param v
	 */
	public Vector2D(Vector2D v){
		this.x = v.getX();
		this.y = v.getY();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
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
		Vector2D other = (Vector2D) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
	/**
	 * Creates a vector from theta angle and length r
	 * @param theta angle in radians
	 * @param r length
	 * @return
	 */
	public static Vector2D fromPolar(float theta, float r){
		return new Vector2D((float) (r * Math.cos(theta)), (float) (r * Math.sin(theta)));
	}
	/**
	 * returns x value
	 * @return
	 */
	public float getX() {
		return x;
	}
	/**
	 * sets x value
	 * @param x
	 */
	public void setX(float x) {
		this.x = x;
	}
	/**
	 * returns y value
	 * @return
	 */
	public float getY() {
		return y;
	}
	/**
	 * sets y value
	 * @param y
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * adds given vectors to this
	 * @param rhs vector to add
	 */
	public void addLocal(Vector2D rhs) {
		this.x += rhs.x;
		this.y += rhs.y;
	}
	/**
	 * returns string representation of object
	 * @returns String
	 */
	public String toString(){
		return "Vec2(" + x + ", " + y + ")";
	}
	/**
	 * normalizes the vector
	 * @return normalized vector
	 */
	public Vector2D normalized() {
		float len = this.len();
		return new Vector2D(x/len,y/len);
	}
	/**
	 * returns length of vector
	 * @return
	 */
	public float len() {
		return (float) Math.sqrt(sqLen());
	}
	/**
	 * returns square length
	 * @return
	 */
	public float sqLen() {
		return x * x + y * y;
	}
	/**
	 * returns vector multiplied by scalar
	 * @param s
	 * @return
	 */
	public Vector2D mul(float s) {
		return new Vector2D(x*s,y*s);
	}

	/**
	 * returns angle to given vector from this one
	 * @param rhs
	 * @return
	 */
	public float angleTo(Vector2D rhs) {
		return (float) Math.acos(this.normalized().dot(rhs.normalized()) / (len() * rhs.len()));
	}
	/**
	 * returns vector subtracted from given vector
	 * @param rhs right hand side vector
	 * @return
	 */
	public Vector2D sub(Vector2D rhs) {
		return new Vector2D(x-rhs.x, y-rhs.y);
	}
	/**
	 * return cross product of vector
	 * @param rhs
	 * @return
	 */
	public float cross(Vector2D rhs) {
		return this.x * rhs.y - this.y * rhs.x;
	}
	/**
	 * returns new vector as a result of dividing vector by given vector
	 * @param rhs vector to divide by
	 * @return
	 */
	public Vector2D div(float rhs) {
		return new Vector2D(x/rhs, y/rhs);
	}
	/**
	 * returns vector as a result of adding vector to given vector
	 * @param rhs
	 * @return
	 */
	public Vector2D add(Vector2D rhs) {
		return new Vector2D(x+rhs.x, y+rhs.y);
	}
	/**
	 * returns the dot product calculated with given vector
	 * @param rhs
	 * @return
	 */
	public float dot(Vector2D rhs) {
		return this.x * rhs.x + this.y * rhs.y;
	}
	/**
	 * divides the vector by the given vector
	 * @param rhs the given vector
	 */
	public void divLocal(float rhs) {
		this.x /= rhs;
		this.y /= rhs;
	}
	/**
	 * returns the angle of this vector in it's polar form
	 * @return the angle in radians
	 */
	public float getPolarAngle(){
		return (float) Math.atan2(y, x);
	}
	/**
	 * checks whether is equal within given range
	 * @param other Vector2D to check against
	 * @param epsilon range of error
	 * @return
	 */
	public boolean equals(Vector2D other, float epsilon) {
		return Math.abs(x - other.x) < epsilon && Math.abs(y - other.y) < epsilon;
	}

}
