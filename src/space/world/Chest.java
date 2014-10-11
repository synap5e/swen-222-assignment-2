package space.world;

import space.math.Vector2D;

public class Chest extends Container {

	public Chest(Vector2D position, int id, float elevation,
			String description, String name, boolean isLocked, Key key) {
		super(position, id, elevation, description, name, isLocked, key);
	}
	
	public Chest(Vector2D position, int id, float elevation,
			String description, String name, boolean isLocked, Key key,boolean isOpen) {
		super(position, id, elevation, description, name, isLocked,isOpen, key);
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public void update(int delta) {
	}

	@Override
	public float getCollisionRadius() {
		return 1;
	}

	@Override
	public float getHeight() {
		return 5;
	}

}
