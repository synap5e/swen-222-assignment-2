package space.gui.pipeline.viewable;

import java.util.List;

import space.math.Vector2D;

/** A world consisting of viewable rooms
 * 
 * @author Simon Pinfold (300280028)
 *
 */
public interface ViewableWorld {

	/** Gets the room at a given location, or null if there is no room at that position
	 * 
	 * @param pos the position
	 * @return the room at the position
	 */
	public ViewableRoom getRoomAt(Vector2D pos);

	/** Gets a list of all viewable rooms in the world
	 * 
	 * @return all viewable rooms in the world
	 */
	public List<? extends ViewableRoom> getViewableRooms();

}
