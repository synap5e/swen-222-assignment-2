package space.world;

import space.math.Vector2D;

public class Box extends Container {
	private static final float COL_RADIUS = 1; // the collision radius
	public Box(Vector2D position, int id, float elevation, String description) {
		super(position, id, elevation, description);
	}

	@Override
	public float getAngle() {
		return 0;
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public float getCollisionRadius() {
		return COL_RADIUS;
	}

}
