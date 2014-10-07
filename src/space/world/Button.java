package space.world;

import space.math.Vector2D;

public class Button extends Stationary {

	public Button(Vector2D position, int id, float elevation,
			String description, String name) {
		super(position, id, elevation, description, name);
	}

	@Override
	public float getAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public float getCollisionRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
