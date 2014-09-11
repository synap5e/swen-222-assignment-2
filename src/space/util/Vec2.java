package space.util;

public class Vec2 {

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
}
