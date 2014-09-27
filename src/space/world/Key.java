package space.world;

import space.math.Vector2D;

public class Key extends Pickup {
	private Door door; // the door it unlocks
	private static final float COL_RADIUS = 1; // the collision radius

	/**
	 * Creates a new Key entity
	 * 
	 * @param pos
	 *            The position of the key
	 * @param i
	 *            The id
	 * @param e
	 *            The elevation
	 * @param desc
	 *            The description of the key
	 * @param d
	 *            The door it unlocks
	 */
	public Key(Vector2D pos, int i, String desc, float e, Door d) {
		super(pos, i, e, desc);
		door = d;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(int delta) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canClip() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getAngle() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
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
