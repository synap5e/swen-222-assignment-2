package space.util;

public class Vec2 {

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
		Vec2 other = (Vec2) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	private float x;
	private float y;

	public Vec2(float x, float y){
		this.x = x;
		this.y = y;
	}
	public Vec2(Vec2 v){
		this.x = v.x;
		this.y = v.y;
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

	public void addLocal(Vec2 rhs) {
		this.x += rhs.x;
		this.y += rhs.y;
	}

	public String toString(){
		return "Vec2(" + x + ", " + y + ")";
	}

	public Vec2 normalized() {
		float len = this.len();
		return new Vec2(x/len,y/len);
	}

	public float len() {
		return (float) Math.sqrt(sqLen());
	}

	public float sqLen() {
		return x * x + y * y;
	}
	
	public Vec2 mul(float s) {
		return new Vec2(x*s,y*s);
	}
	
	public float angleTo(Vec2 rhs) {
		return (float) Math.acos(this.normalized().dot(rhs.normalized()) / (len() * rhs.len()));
	}
	
	public Vec2 sub(Vec2 rhs) {
		return new Vec2(x-rhs.x, y-rhs.y);
	}
	
	public float cross(Vec2 rhs) {
		return this.x * rhs.y - this.y * rhs.x;
	}
	
	public Vec2 div(float rhs) {
		return new Vec2(x/rhs, y/rhs);
	}
	
	public Vec2 add(Vec2 rhs) {
		return new Vec2(x+rhs.x, y+rhs.y);
	}
	
	public float dot(Vec2 rhs) {
		return this.x * rhs.x + this.y * rhs.y;
	}
	
	public void divLocal(float rhs) {
		this.x /= rhs;
		this.y /= rhs;
	}
	
}
