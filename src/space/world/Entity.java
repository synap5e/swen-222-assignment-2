package space.world;

import space.gui.pipeline.viewable.ViewableObject;
import space.math.Vector2D;

public abstract class Entity implements ViewableObject {
	private Vector2D position;
	private int id;
	private float elevation;
	private String description;

	/**
	 * Constructs new entity
	 * 
	 * @param pos
	 *            The entity's position
	 * @param i
	 *            The id
	 * @param d
	 *            The description of the entity
	 */
	public Entity(Vector2D pos, int i, float e, String d) {
		position = pos;
		id = i;
		elevation = e;
		description = d;
	}

	/**
	 * Returns the entity's position
	 * 
	 * @return
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Returns the entity's id
	 * 
	 * @return
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns the entity's elevation
	 * 
	 * @return
	 */
	public float getElevation() {
		return elevation;
	}

	/**
	 * Returns the entity's description
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Changes the position
	 * 
	 * @param pos
	 *            The position it will be changed to
	 */
	public void setPosition(Vector2D pos) {
		position = pos;
	}

	/**
	 * Whether or not something can pass through the entity
	 * 
	 * @return
	 */
	public abstract boolean canClip();

	/**
	 * Updates the entity's status
	 * 
	 * @param delta
	 *            the amount of time since the previous update
	 */
	public abstract void update(int delta);

	/**
	 * Returns the radius of the entity's bounding circle
	 * 
	 * @return
	 */
	public abstract float getCollisionRadius();
}
