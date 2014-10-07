package space.world;

import space.math.Vector2D;

public class Light extends Stationary {
	private static final float HEIGHT = 11;
	
	public Light(Vector2D position, int id, float elevation,String description, String name) {
		super(position, id, elevation, description, name);
	}

	@Override
	public float getAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getCollisionRadius() {
		return 0;
	}

	@Override
	public float getHeight() {
		return HEIGHT;
	}

	@Override
	public boolean canClip() {
		return false;
	}

}
