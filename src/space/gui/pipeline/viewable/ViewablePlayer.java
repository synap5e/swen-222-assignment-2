package space.gui.pipeline.viewable;

import space.math.Vector3D;

/**
 * 
 * @author Simon Pinfold
 *
 */
public interface ViewablePlayer extends ViewableObject{
	
	/** Get the direction that the player is looking in
	 * 
	 * @return the direction the player is looking
	 */
	public Vector3D getLookDirection();

	public float getEyeHeight();

	public boolean isTorchOn();

}
