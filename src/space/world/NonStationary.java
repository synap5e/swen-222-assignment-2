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
	 * @param position
	 *            The position
	 * @param id
	 *            The id
	 * @param elevation
	 *            The elevation
	 * @param description
	 *            The description
	 */
	public NonStationary(Vector2D position, int id, float elevation, String description) {
		super(position, id, elevation, description);
	}
}