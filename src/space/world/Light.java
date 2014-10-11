package space.world;

import space.math.Vector2D;

public class Light extends Stationary {
	
	public Light(Vector2D position, int id, float elevation,String description, String name) {
		super(position, id, elevation, description, name);
	}

	@Override
	public float getCollisionRadius() {
		return 0;
	}

	@Override
	public float getHeight() {
		return 11;
	}

	@Override
	public boolean canClip() {
		return false;
	}

}
