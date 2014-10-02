package space.world;

import space.math.Vector2D;

public class Key extends Pickup {
	private Door door; // the door it unlocks
	private static final float COL_RADIUS = 1; // the collision radius

	/**
	 * Creates a new Key entity
	 * 
	 * @param position
	 *            The position of the key
	 * @param id
	 *            The id
	 * @param elevation
	 *            The elevation
	 * @param description
	 *            The description of the key
	 * @param door
	 *            The door it unlocks
	 */
	public Key(Vector2D position, int id, String description, float elevation, Door door) {
		super(position, id, elevation, description);
		this.door = door;
	}
	
	public Door getExit() {
		return door;
	}

	@Override
	public void update(int delta) {
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public float getAngle() {
		return 0;
	}

	@Override
	public float getCollisionRadius() {
		return COL_RADIUS;
	}

	/**
	 * Returns the door the key unlocks
	 * 
	 * @return
	 */
	public Door getDoor() {
		return door;
	}

}
