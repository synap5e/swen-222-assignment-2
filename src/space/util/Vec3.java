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
}
