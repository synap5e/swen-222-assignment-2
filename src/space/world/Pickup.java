package space.world;

import space.math.Vector2D;

/**
 * Represents an entity which can be picked up. It could also be placed in an
 * inventory or a container
 * 
 * @author Maria Libunao
 */
public abstract class Pickup extends NonStationary {
	/**
	 * Constructs a new pickup-able entity
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
	public Pickup(Vector2D position, int id, float elevation, String description) {
		super(position, id, elevation, description);
	}
}
