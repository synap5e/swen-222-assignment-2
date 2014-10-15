package space.math;

/**
 * represents a vector3D point object
 * @author Simon Pinfold (300280028)
 *
 */
public class Vector3D {

	private float x;
	private float y;
	private float z;
	/**
	 * constructs a vector3d with given x, y and z object
	 * @param x x value
	 * @param y y value
	 * @param z z value
	 */
	public Vector3D(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	/**
	 * Constructs a new Vector3D from given Vector3D
	 * @param v
	 */
	public Vector3D(Vector3D v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
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
	 * gets y value
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
	 * gets z value
	 * @return
	 */
	public float getZ() {
		return z;
	}
	/**
	 * sets z value
	 * @param z
	 */
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
	/**
	 * returns a dot product calculated with given vector
	 * @param rhs
	 * @return
	 */
	public float dot(Vector3D rhs) {
		return this.x * rhs.x + this.y * rhs.y + this.z * rhs.z;
	}

	/**
	 * returns a new vector3D cross product from given vector
	 * @param rhs given vector to calculate
	 * @return Vector3D
	 */
	public Vector3D cross(Vector3D rhs) {
		return new Vector3D(this.y * rhs.z - this.z * rhs.y, this.z * rhs.x - this.x * rhs.z, this.x * rhs.y - this.y * rhs.x);
	}
	/**
	 * returns normalized vector
	 * @return
	 */
	public Vector3D normalized() {
		float len = this.len();
		return new Vector3D(x/len,y/len,z/len);
	}
	/**
	 * returns length of vector3D
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
		return x * x + y * y + z * z;
	}
	/**
	 * adds given vector to this vector
	 * @param argVec
	 * @return
	 */
	public Vector3D addLocal(Vector3D argVec) {
		x += argVec.x;
		y += argVec.y;
		z += argVec.z;
		return this;
	}
	/**
	 * returns new vector that is the addition of given vector and this
	 * @param argVec
	 * @return
	 */
	public Vector3D add(Vector3D argVec) {
		return new Vector3D(x + argVec.x, y + argVec.y, z + argVec.z);
	}
	/**
	 * returns subtracts given vector from this
	 * @param argVec
	 * @return subtracted vector
	 */
	public Vector3D subLocal(Vector3D argVec) {
		x -= argVec.x;
		y -= argVec.y;
		z -= argVec.z;
		return this;
	}
	/**
	 * returns new vector3D constructed from subtraction of given vector from this
	 * @param argVec
	 * @return new Vector3D
	 */
	public Vector3D sub(Vector3D argVec) {
		return new Vector3D(x - argVec.x, y - argVec.y, z - argVec.z);
	}
	/**
	 * multiplies vector from a given scalar
	 * @param argScalar
	 * @return
	 */
	public Vector3D mulLocal(float argScalar) {
		x *= argScalar;
		y *= argScalar;
		z *= argScalar;
		return this;
	}
	/**
	 * returns a new vector3D that is this multiplied by given scalar
	 * @param argScalar
	 * @return
	 */
	public Vector3D mul(float argScalar) {
		return new Vector3D(x * argScalar, y * argScalar, z * argScalar);
	}
	/**
	 * negates this
	 * @return
	 */
	public Vector3D negate() {
		return new Vector3D(-x, -y, -z);
	}
	/**
	 * returns angle to given Vector3D
	 * @param other
	 * @return
	 */
	public float angleTo(Vector3D other){
		return (float) Math.acos(dot(other) / (len() * other.len()));
	}

	@Override
	public String toString() {
		return "Vec3(" + x + ", " + y + ", " + z + ")";
	}
}
