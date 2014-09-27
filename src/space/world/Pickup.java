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
	 * @param pos
	 *            The position
	 * @param i
	 *            The id
	 * @param e
	 *            The elevation
	 * @param d
	 *            The description
	 */
	public Pickup(Vector2D pos, int i, float e, String d) {
		super(pos, i, e, d);
	}
}
