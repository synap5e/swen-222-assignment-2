package space.math;

public class Vector3D {

	private float x;
	private float y;
	private float z;

	public Vector3D(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3D(Vector3D v){
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
		Vector3D other = (Vector3D) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

	public float dot(Vector3D rhs) {
		return this.x * rhs.x + this.y * rhs.y + this.z * rhs.z;
	}

	public Vector3D cross(Vector3D rhs) {
		return new Vector3D(this.y * rhs.z - this.z * rhs.y, this.z * rhs.x - this.x * rhs.z, this.x * rhs.y - this.y * rhs.x);
	}

	public Vector3D normalized() {
		float len = this.len();
		return new Vector3D(x/len,y/len,z/len);
	}

	public float len() {
		return (float) Math.sqrt(sqLen());
	}

	public float sqLen() {
		return x * x + y * y + z * z;
	}

	public Vector3D addLocal(Vector3D argVec) {
		x += argVec.x;
		y += argVec.y;
		z += argVec.z;
		return this;
	}

	public Vector3D add(Vector3D argVec) {
		return new Vector3D(x + argVec.x, y + argVec.y, z + argVec.z);
	}

	public Vector3D subLocal(Vector3D argVec) {
		x -= argVec.x;
		y -= argVec.y;
		z -= argVec.z;
		return this;
	}

	public Vector3D sub(Vector3D argVec) {
		return new Vector3D(x - argVec.x, y - argVec.y, z - argVec.z);
	}

	public Vector3D mulLocal(float argScalar) {
		x *= argScalar;
		y *= argScalar;
		z *= argScalar;
		return this;
	}

	public Vector3D mul(float argScalar) {
		return new Vector3D(x * argScalar, y * argScalar, z * argScalar);
	}
	public Vector3D negate() {
		return new Vector3D(-x, -y, -z);
	}

	@Override
	public String toString() {
		return "Vec3(" + x + ", " + y + ", " + z + ")";
	}
}
