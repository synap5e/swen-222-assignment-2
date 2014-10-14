package space.gui.pipeline.viewable;

import java.util.List;

import space.math.Vector2D;
import space.math.Vector3D;

/** A room that can be viewed. Has all the usual walls, floor and ceiling and also holds ViewableDoors and ViewableObjects
 * 
 * @author Simon Pinfold (300280028)
 *
 */
public interface ViewableRoom {

	public enum LightMode{ BASIC_LIGHT, DARK };

	/** Gets the center coordinate of this room
	 *
	 * @return the center of the room
	 */
	public Vector2D getCentre();

	/**
	 * Gets all the ViewableWalls of this room
	 * @return the room's walls
	 */
	public List<? extends ViewableWall> getWalls();

	/**
	 * Gets all the ViewableObjects contained in the room.
	 * @return the objects in the room.
	 */
	public List<? extends ViewableObject> getContainedObjects();

	/**
	 * Check whether the room contains a point
	 * @param point the point to check
	 * @return whether the room contains point
	 */
	public boolean contains(Vector2D point);

	/**
	 *  Get all viewable doors of this room
	 * @return all the doors
	 */
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

	/**
	 * Get all ViewableBeams created from this room
	 * @return
	 */
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
