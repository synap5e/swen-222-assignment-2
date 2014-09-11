package space.gui.pipeline;

import space.util.Vec3;

public interface ViewablePlayer extends ViewableObject{
	
	/** Get the direction that the player is looking in
	 * 
	 * @return the direction the player is looking
	 */
	public Vec3 getLookDirection();

	public float getEyeHeight();

}
