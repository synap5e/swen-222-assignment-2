package space.world;

import space.gui.pipeline.viewable.ViewableDoor;
import space.math.Vector2D;

/**
 * Represents a door which connects two rooms together
 * 
 * @author Maria Libunao
 */
public class Door extends NonStationary implements ViewableDoor {
	private Room room1;
	private Room room2;
	private boolean oneWay; // if one way, can only get from room1 to room2
	private boolean locked;

	private float amtOpen = 0; // the amount the door is open. 1 is fully open &
								// 0 is fully closed
	private enum DoorState {OPEN, OPENING, CLOSED, CLOSING};
	private DoorState state = DoorState.CLOSED;

	private static final float LENGTH = 500;

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
	public Door(Vector2D position, int id, String description, Room room1, Room room2, boolean isOneWay,
			boolean isLocked) {
		super(position, id, 0, description);
		this.room1 = room1;
		this.room2 = room2;
		this.oneWay = isOneWay;
		this.locked = isLocked;
	}


	/**
	 * Unlocks the door if the player has the key to unlock it
	 * 
	 * @param p
	 *            The player trying to unlock the door
	 */
	public void unlock(Player p) {
		for (Pickup i : p.getInventory()) {
			if (i instanceof Key && ((Key) i).getDoor().equals(this)) { //player has key to unlock this door
				locked = false;
				return;
			}
		}
	}

	/**
	 * Whether or not something can go through the door. Can only go through it
	 * when it is unlocked and fully open
	 * 
	 * @return
	 */
	public boolean canGoThrough() {
		return !locked && amtOpen == 1;
	}

	/** Closes the door if it is not already closed */
	public void closeDoor() {
		if (state != DoorState.CLOSED) {
			state = DoorState.CLOSING;
		}
	}

	/** Opens the door if it is not already open & is unlocked */
	public void openDoor() {
		if (state != DoorState.OPEN && !locked) {
			state = DoorState.OPENING;
		}
	}

	@Override
	public void update(int delta) {
		if (state == DoorState.CLOSING) {
			amtOpen -= delta / LENGTH;
			if (amtOpen <= 0) {
				amtOpen = 0;
				state = DoorState.CLOSED;
			}
		} else if (state == DoorState.OPENING) {
			amtOpen += delta / LENGTH;
			if (amtOpen >= 1) {
				amtOpen = 1;
				state = DoorState.OPEN;
			}
		}

	}

	@Override
	public float getCollisionRadius() {
		return 0;
	}

	/**
	 * Returns whether or not the door is locked
	 * 
	 * @return
	 */
	public boolean isLocked() {
		return locked;
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
	public float getOpenPercent() {
		return amtOpen;
	}

}
