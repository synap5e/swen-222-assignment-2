package space.gui.pipeline.viewable;

import space.math.Vector2D;

public interface ViewableDoor {
	
	public Vector2D getLocation();

	/** 1 = fully open
	 *  0 = fully closed
	 * @return
	 */
	public float getOpenPercent();

}
