package space.world;

import space.math.Vector2D;

public class Chair extends Stationary {

	public Chair(Vector2D position, int id, float elevation,
			String description, String name) {
		super(position, id, elevation, description, name);
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public float getCollisionRadius() {
		return 1;
	}

	@Override
	public float getHeight() {
		return 1;
	}

	@Override
	public String getType() {
		return "Chair";
	}

}
