package space.gui.pipeline.viewable;

import space.math.Vector2D;

public interface ViewableObject {
	
	public Vector2D getPosition();
	
	/** Gets the objects rotation around the y axis in radions
	 * 
	 * @return
	 */
	public float getAngle();

	public float getElevation();
	
	public boolean canMove();

}
