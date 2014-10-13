package space.world;

import space.gui.pipeline.viewable.ViewableObject;
import space.math.Vector2D;

/**Represents an item which has a position and elevation*/
public abstract class Entity implements ViewableObject {
	private Vector2D position;
	private int id;
	private float elevation;
	private String description;
	private String name;

	/**
	 * Constructs new entity
	 * 
	 * @param position
	 *            The entity's position
	 * @param id
	 *            The id
	 * @param description
	 *            The description of the entity
	 * @param name The name of the entity
	 */
	public Entity(Vector2D position, int id, float elevation, String description, String name) {
		this.position = position;
		this.id = id;
		this.elevation = elevation;
		this.description = description;
		this.name = name;
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
	 * Returns the entity's elevation, how far off the ground it is
	 * 
	 * @return
	 */
	public float getElevation() {
		return elevation;
	}
	
	public void setElevation(float e){
		elevation = e;
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
	
	/**@return The entity's name*/
	public String getName() {
		return name;
	}

	/**Lets the Character interact with the entity. This includes
	 * trying to unlock the entity or trying to pick it up
	 * @return whether or not the interaction was successful*/
	public boolean interact(Character c, World w){
		return false;
	}
	
	/**Whether or not an entity can be interacted with
	 * @return*/
	public boolean canInteract(){
		return false;
	}
	
	@Override
	public float getAngle() {
		return 0;
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
	
	/**Returns the height of the entity, how tall it is
	 * @return
	 */
	public abstract float getHeight();
}
