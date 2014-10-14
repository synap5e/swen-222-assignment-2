package space.gui.pipeline.viewable;

import space.math.Vector2D;

/** A nobject that can be viewed in the renderer
 * 
 * @author Simon Pinfold (300280028)
 *
 */
public interface ViewableObject {
	
	/**
	 * The position in the world of the object
	 * @return the object's position
	 */
	public Vector2D getPosition();
	
	/** Gets the objects rotation around the y axis in degrees
	 *  Some objects may not have meaningful angles, in this case the return value can be anything
	 * @return the angle in degrees
	 */
	public float getAngle();

	/**
	 * The elevation off the ground of the object
	 * @return
	 */
	public float getElevation();
	
	/**
	 * The type of the object - as a rule just derived from the class name
	 * @return A string naming the type of the object
	 */
	public String getType();

	/**
	 * Get the collision radius of the object
	 * @return the collision radius of the object
	 */
	public float getCollisionRadius();
}
