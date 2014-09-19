package space.gui.pipeline.viewable;

import java.util.List;

import space.math.Vector2D;

/**
 * 
 * @author Simon Pinfold
 *
 */
public interface ViewableRoom {

	public enum LightMode{ BASIC_LIGHT, DARK };

	/** Gets the lighting mode for a room. Currently this can only return BASIC_LIGHT
	 *
	 * @return LightMode.BASIC_LIGHT
	 */
	public LightMode getLightMode();

	/** Gets the center coordinate of this room
	 *
	 * @return the center of the room
	 */
	public Vector2D getCentre();

	public List<? extends ViewableWall> getWalls();

	public List<? extends ViewableObject> getContainedObjects();

	public boolean contains(Vector2D point);

	public List<? extends ViewableDoor> getAllDoors();

	/** Get the top left corner of this hull's axis
	 * aligned bounding box
	 * 
	 * @return
	 */
}
