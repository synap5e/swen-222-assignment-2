package space.world;

import space.math.Vector2D;

public class Key extends Pickup {
	private Door exit; //the exit it opens
	private Vector2D position;
	private int i;
	private String description;
	private static final float COL_RADIUS = 1;

	public Key(Vector2D pos, int i, String d,Door e) {
		super(pos, i,d);
		exit = e;
		position = pos;
		this.i = i;
		description = d;
	}
	
	public Vector2D getPosition(){
		return position;
	}
	
	public int geti(){
		return i;
	}
	
	public String getDescription(){
		return description;
	}
	
	public Door getExit() {
		return exit;
	}

	@Override
	public void update(int delta) {
	}

	@Override
	public boolean canClip() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getElevation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getCollisionRadius() {
		return COL_RADIUS;
	}

}
