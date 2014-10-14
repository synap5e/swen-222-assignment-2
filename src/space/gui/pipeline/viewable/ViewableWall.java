package space.gui.pipeline.viewable;

import java.util.List;
import java.util.Map;

import space.math.Vector2D;

/**
 * A wall that can be viewed. A wall is a line segment (line between two points) with any number of doors (inc 0) through it.
 * A wall is only visible from one side, defined as the inside of the room this is a wall for. 
 * The wall's line segment must be defined from left to right when facing the wall with the wall visible.
 * @author Simon Pinfold (300280028)
 *
 */
public interface ViewableWall {

	/**
	 * Get the start of the line segment defining the wall.
	 * @return the start of the wall
	 */
	public Vector2D getStart();

	/**
	 * Get the end of the line segment defining the wall.
	 * @return the end of the wall
	 */
	public Vector2D getEnd();
	
	/**
	 * Get all ViewableDoors on this particular wall
	 * @return all the doors on this wall
	 */
	public List<? extends ViewableDoor> getDoors();
}
