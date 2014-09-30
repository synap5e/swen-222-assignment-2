package space.world;

import space.gui.pipeline.viewable.ViewableStationary;
import space.math.Vector2D;

/**
 * Represents an entity which cannot move
 * 
 * @author Maria Libunao
 */
public abstract class Stationary extends Entity implements ViewableStationary {
	/**
	 * Constructs a new stationary entity
	 * 
	 * @param pos
	 *            The position
	 * @param i
	 *            The id
	 * @param e
	 *            The elevation
	 * @param d
	 *            The description
	 */
	public Stationary(Vector2D pos, int i, float e, String d) {
		super(pos, i, e, d);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean canClip() {
		return true;
	}

	/**
	 * This method does nothing since stationary entities cannot be moved
	 * 
	 * @param
	 */
	@Override
	public final void setPosition(Vector2D pos) {
	}

	/**
	 * This method does nothing since stationary entities cannot be moved, and
	 * so will not need to be updated.
	 * 
	 * @param
	 */
	public final void update(int delta) {
	}
}
