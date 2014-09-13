package space.util;

public class Vec3 {

	private float x;
	private float y;
	private float z;

	public Vec3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vec3(Vec3 v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
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
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
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
		Vec3 other = (Vec3) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}




	public final static float dot(Vec3 a, Vec3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	public final static Vec3 cross(Vec3 a, Vec3 b) {
		return new Vec3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
	}

	public Vec3 normalized() {
		float len = this.len();
		return new Vec3(x/len,y/len,z/len);
	}

	public float len() {
		return (float) Math.sqrt(sqLen());
	}

	public float sqLen() {
		return x * x + y * y + z * z;
	}

	public Vec3 addLocal(Vec3 argVec) {
		x += argVec.x;
		y += argVec.y;
		z += argVec.z;
		return this;
	}

	public Vec3 add(Vec3 argVec) {
		return new Vec3(x + argVec.x, y + argVec.y, z + argVec.z);
	}

	public Vec3 subLocal(Vec3 argVec) {
		x -= argVec.x;
		y -= argVec.y;
		z -= argVec.z;
		return this;
	}

	public Vec3 sub(Vec3 argVec) {
		return new Vec3(x - argVec.x, y - argVec.y, z - argVec.z);
	}

	public Vec3 mulLocal(float argScalar) {
		x *= argScalar;
		y *= argScalar;
		z *= argScalar;
		return this;
	}

	public Vec3 mul(float argScalar) {
		return new Vec3(x * argScalar, y * argScalar, z * argScalar);
	}
	public Vec3 negate() {
		return new Vec3(-x, -y, -z);
	}

	@Override
	public String toString() {
		return "Vec3(" + x + ", " + y + ", " + z + ")";
	}
}
