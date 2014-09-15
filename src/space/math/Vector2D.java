package space.math;

/**
 * 
 * @author Simon Pinfold
 *
 */
public class Vector2D {

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

	private float x;
	private float y;

	public Vector2D(float x, float y){
		this.x = x;
		this.y = y;
	}
	public Vector2D(Vector2D v){
		this.x = v.x;
		this.y = v.y;
	}
	public static Vector2D fromPolar(float theta, float r){
		return new Vector2D((float) (r * Math.cos(theta)), (float) (r * Math.sin(theta)));
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}

	public void addLocal(Vector2D rhs) {
		this.x += rhs.x;
		this.y += rhs.y;
	}

	public String toString(){
		return "Vec2(" + x + ", " + y + ")";
	}

	public Vector2D normalized() {
		float len = this.len();
		return new Vector2D(x/len,y/len);
	}

	public float len() {
		return (float) Math.sqrt(sqLen());
	}

	public float sqLen() {
		return x * x + y * y;
	}
	
	public Vector2D mul(float s) {
		return new Vector2D(x*s,y*s);
	}
	
	public float angleTo(Vector2D rhs) {
		return (float) Math.acos(this.normalized().dot(rhs.normalized()) / (len() * rhs.len()));
	}
	
	public Vector2D sub(Vector2D rhs) {
		return new Vector2D(x-rhs.x, y-rhs.y);
	}
	
	public float cross(Vector2D rhs) {
		return this.x * rhs.y - this.y * rhs.x;
	}
	
	public Vector2D div(float rhs) {
		return new Vector2D(x/rhs, y/rhs);
	}
	
	public Vector2D add(Vector2D rhs) {
		return new Vector2D(x+rhs.x, y+rhs.y);
	}
	
	public float dot(Vector2D rhs) {
		return this.x * rhs.x + this.y * rhs.y;
	}
	
	public void divLocal(float rhs) {
		this.x /= rhs;
		this.y /= rhs;
	}
	
	public float getPolarAngle(){
		return (float) Math.tanh(y/x);
	}
	
	public boolean equals(Vector2D other, float epsilon) {
		return Math.abs(x - other.x) < epsilon && Math.abs(y - other.y) < epsilon;
	}
	
}
