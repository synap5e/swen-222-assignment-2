package space.gui.pipeline.viewable;

import space.util.Vec2;

public interface ViewableObject {
	
	public Vec2 getPosition();
	
	/** Gets the objects rotation around the y axis in radions
	 * 
	 * @return
	 */
	public float getAngle();

	public float getElevation();
	
	public boolean canMove();

}
