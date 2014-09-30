package space.world;

import space.gui.pipeline.viewable.ViewableNonStationary;
import space.math.Vector2D;

/**
 * Represents an entity which is animated. This could mean that it can change
 * its position or has the ability to be opened or closed
 * 
 * @author Maria Libunao
 */
public abstract class NonStationary extends Entity implements ViewableNonStationary {
	/**
	 * Constructs a new non-stationary entity
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
	public NonStationary(Vector2D pos, int i, float e, String d) {
		super(pos, i, e, d);
	}
}