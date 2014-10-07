package space.world;

import space.gui.pipeline.viewable.ViewableDoor;
import space.math.Vector2D;

/**
 * Represents a door which connects two rooms together
 * 
 * @author Maria Libunao
 */
public class Door extends Openable implements ViewableDoor {
	private Room room1;
	private Room room2;
	private boolean oneWay; // if one way, can only get from room1 to room2

	private static final float COL_RADIUS = 3; // the collision radius

	/**
	 * Creates new Door
	 * 
	 * @param position
	 *            The door's position
	 * @param id
	 *            The id
	 * @param description
	 *            The door's description
	 * @param room1
	 *            ,r2 The rooms the door is connected to
	 * @param isOneWay
	 *            Whether or not the door can only be used from r1 to r2 or both
	 *            ways
	 * @param isLocked
	 *            Whether or not the door is locked
	 */
	public Door(Vector2D position, int id, String description, String name,Room room1, Room room2, boolean isOneWay,
			boolean isLocked, Key key) {
		super(position, id, 0, description,name,500, isLocked, key);
		this.room1 = room1;
		this.room2 = room2;
		this.oneWay = isOneWay;
	}
	
	public boolean canInteract(){
		return true;
	}
	
	public boolean interact(Character c){
		return false;
	}

	/**
	 * Whether or not something can go through the door. Can only go through it
	 * when it is unlocked and fully open
	 * 
	 * @return
	 */
	public boolean canGoThrough() {
		return !super.isLocked() && super.getOpenPercent() == 1;
	}

	@Override
	public float getCollisionRadius() {
		return COL_RADIUS;
	}

	@Override
	public Room getRoom1() {
		return room1;
	}

	@Override
	public Room getRoom2() {
		return room2;
	}

	/**
	 * Returns the other room the door is connected to. Will return null if the
	 * door is one way & so cannot get from the room provided to the other room.
	 * Returns null if the room provided does not match any of the rooms the
	 * door is connected to
	 * 
	 * @param r
	 * @return The other room
	 */
	public Room otherRoom(Room r) {
		if (r.equals(room1)) {
			return room2;
		} else if (r.equals(room2) && !oneWay) {
			return room1;
		}
		return null;
	}

	/**
	 * Returns whether or not the door is one way
	 * 
	 * @return
	 */
	public boolean isOneWay() {
		return oneWay;
	}

	@Override
	public float getAngle() {
		return 0;
	}

	@Override
	public boolean canClip() {
		return true;
	}


	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
