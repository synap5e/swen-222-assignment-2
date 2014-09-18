package space.gui.pipeline.viewable;

import java.util.Map;

import space.math.Vector2D;

/**
 * 
 * @author Simon Pinfold
 *
 */
public interface ViewableWall {

	public Vector2D getStart();

	public Vector2D getEnd();
	
	/** Get a mapping of 'distance along wall' to door.
	 * distance along wall is defined from the start of the wall
	 * 
	 * @return the viewable doors at their respective distances along the wall
	 */
	public Map<Float, ? extends ViewableDoor> getDoors();
}
