package space.gui.pipeline.viewable;

import java.util.List;
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
	
	public List<? extends ViewableDoor> getDoors();
}
