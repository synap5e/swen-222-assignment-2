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
	 * @param position
	 *            The position
	 * @param id
	 *            The id
	 * @param elevation
	 *            The elevation
	 * @param description
	 *            The description
	 */
	public Stationary(Vector2D position, int id, float elevation, String description) {
		super(position, id, elevation, description);
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
