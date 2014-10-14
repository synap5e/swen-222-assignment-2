package space.world;

import space.gui.pipeline.viewable.ViewableDoor;
import space.math.Vector2D;
/**
 * Represents a door which connects two rooms together
 * 
 * @author Maria Libunao
 */
public class Door extends NonStationary implements ViewableDoor, Lockable {
	private enum OpeningState {OPEN, OPENING, CLOSED, CLOSING};
	private boolean locked;	
	private Key key;
	private OpeningState state;
	private Room room1;
	private Room room2;
	private boolean oneWay; // if one way, can only get from room1 to room2
	private boolean canInteract; //true if can interact directly, false if player must press on button to interact
	private float amtOpen = 0; // the amount the door is open. 1 is fully open 0 is fully closed
	
	private static final float OPEN_DURATION = 500;

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
			boolean isLocked, Key key,boolean canInteract) {
		super(position, id, 0, description,name);
		this.room1 = room1;
		this.room2 = room2;
		this.oneWay = isOneWay;
		this.locked = isLocked;
		this.key = key;
		this.canInteract = canInteract;
		this.state = OpeningState.CLOSED;
	}
	
	public Door(Vector2D position, int id, String description, String name,Room room1, Room room2, boolean isOneWay,
			boolean isLocked, Key key,boolean canInteract, String state, float amtOpened) {
		super(position, id, 0, description,name);
		this.room1 = room1;
		this.room2 = room2;
		this.oneWay = isOneWay;
		this.locked = isLocked;
		this.key = key;
		this.canInteract = canInteract;
		this.state = OpeningState.valueOf(state);
		this.amtOpen = amtOpened;
	}
	
	@Override
	public boolean interact(Character c,World w){
		if(locked){
			unlock(c);
		}
		if(open()){
			return true;
		} else{
			return close();
		}
	}

	@Override
	public void update(int delta) {
		if (state == OpeningState.CLOSING) {
			amtOpen -= delta / OPEN_DURATION;
			if (amtOpen <= 0) {
				amtOpen = 0;
				state = OpeningState.CLOSED;
			}
		} else if (state == OpeningState.OPENING) {
			amtOpen += delta / OPEN_DURATION;
			if (amtOpen >= 1) {
				amtOpen = 1;
				state = OpeningState.OPEN;
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
	
	/** Opens this if it is not already open & is unlocked
	 * @return if this has been opened*/
	public boolean open() {
		if (state != OpeningState.OPEN && state != OpeningState.OPENING && !locked) {
			state = OpeningState.OPENING;
			return true;
		}
		return false;
	}
	
	/** Closes this if it is not already closed
	 * @return if this has been closed*/
	public boolean close() {
		if (state != OpeningState.CLOSED && state != OpeningState.CLOSING) {
			state = OpeningState.CLOSING;
			return true;
		}
		return false;
	}
	
	/**
	 * Unlocks this if the player has the key to unlock it
	 *
	 * @param c
	 *            The player trying to unlock
	 */
	public void unlock(Character c) {
		if(!locked){return;}
		for (Pickup i : c.getInventory()) {
			if (i instanceof Key && key.equals(i)) { //player has key to unlock this
				locked = false;
			}
		}
	}

	@Override
	public float getOpenPercent() {
		return amtOpen;
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public float getHeight() {
		return 10;
	}

	@Override
	public float getCollisionRadius() {
		return 3;
	}

	@Override
	public Room getRoom1() {
		return room1;
	}

	@Override
	public Room getRoom2() {
		return room2;
	}
	
	@Override
	public boolean canInteract(){
		return canInteract;
	}
	
	@Override
	public String getType() {
		return "Door";
	}
	
	/**Should not be able to setPosition of door
	 * @param*/
	@Override
	public void setPosition(Vector2D pos){
	}

	/**
	 * Returns whether or not this is locked
	 *
	 * @return
	 */
	public boolean isLocked() {
		return locked;
	}

	/**@return the current state of the door*/
	public String getState(){
		return state.toString();
	}

	/**@return the key that unlocks the door*/
	public Key getKey() {
		return key;
	}
	

	/**
	 * Returns whether or not the door is one way
	 * 
	 * @return
	 */
	public boolean isOneWay() {
		return oneWay;
	}
}
