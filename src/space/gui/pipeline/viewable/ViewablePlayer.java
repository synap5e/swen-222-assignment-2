package space.gui.pipeline.viewable;

import space.math.Vector3D;

/** A (human controller) player that can be viewed
 * 
 * @author Simon Pinfold (300280028)
 *
 */
public interface ViewablePlayer extends ViewableObject{
	
	/** Get the direction that the player is looking in
	 * 
	 * @return the direction the player is looking
	 */
	public Vector3D getLookDirection();

	/**
	 * The height of the player's eyes/camera
	 * @return the eye height
	 */
	public float getEyeHeight();

	/**
	 * If the player has their torch on
	 * @return if the player's torch is on
	 */
	public boolean isTorchOn();

}
