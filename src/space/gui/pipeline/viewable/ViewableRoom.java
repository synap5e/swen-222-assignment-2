package space.gui.pipeline.viewable;

import java.util.List;

import space.math.Vector2D;
import space.math.Vector3D;

/**
 * 
 * @author Simon Pinfold
 *
 */
public interface ViewableRoom {

	public enum LightMode{ BASIC_LIGHT, DARK };

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
	public Vector2D getAABBTopLeft();
	
	/** Get the bottom right corner of this hull's axis
	 * aligned bounding box
	 * 
	 * @return
	 */
	public Vector2D getAABBBottomRight();

	public List<? extends ViewableBeam> getBeams();
	
	/** Get the amount and color of light in a room. 
	 * the x,y and z components are red, green and blue respectively.
	 * 
	 * Note that values of over 0.7 for a color can cause the exposure to look odd
	 * 
	 * @return the light intensity and color of a room
	 */
	public Vector3D getLight();
}
