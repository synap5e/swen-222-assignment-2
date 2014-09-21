package space.gui.pipeline.viewable;

import space.math.Vector2D;

/**
 * 
 * @author Simon Pinfold
 *
 */
public interface ViewableObject {
	
	public Vector2D getPosition();
	
	/** Gets the objects rotation around the y axis in degrees
	 *  Some objects may not have meaningful angles, in this case the return value can be anything
	 * @return the angle in degrees
	 */
	public float getAngle();

	public float getElevation();
	
	public boolean canMove();

}
