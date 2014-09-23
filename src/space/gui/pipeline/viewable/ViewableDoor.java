package space.gui.pipeline.viewable;

import space.math.Vector2D;

public interface ViewableDoor extends ViewableObject{
	
	/** 1 = fully open
	 *  0 = fully closed
	 * @return
	 */
	public float getOpenPercent();

	/** Can be null if the door does not lead to another room
	 * 
	 * @return
	 */
	public ViewableRoom getRoom1();
	
	/** Can be null if the door does not lead to another room
	 * 
	 * @return
	 */
	public ViewableRoom getRoom2();

}
