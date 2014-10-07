package space.world;

import space.math.Vector2D;

public abstract class Openable extends NonStationary {
	private enum OpeningState {OPEN, OPENING, CLOSED, CLOSING};
	private boolean locked;
	private float amtOpen = 0; // the amount the door is open. 1 is fully open &
								// 0 is fully closed
	private OpeningState state = OpeningState.CLOSED;
	private Key key;
	private final float openDuration;
	
	public Openable(Vector2D position, int id, float elevation,
			String description, String name, float openTime, boolean isLocked, Key key) {
		super(position, id, elevation, description, name);
		this.openDuration = openTime;
		this.locked = isLocked;
		this.key = key;
	}

	@Override
	public void update(int delta) {
		if (state == OpeningState.CLOSING) {
			amtOpen -= delta / openDuration;
			if (amtOpen <= 0) {
				amtOpen = 0;
				state = OpeningState.CLOSED;
			}
		} else if (state == OpeningState.OPENING) {
			amtOpen += delta / openDuration;
			if (amtOpen >= 1) {
				amtOpen = 1;
				state = OpeningState.OPEN;
			}
		}

	}
	
	@Override
	public boolean canInteract(){
		return true;
	}
	
	@Override
	public boolean interact(Character c,World w){
		if(locked){
			unlock(c);
		}
		if(state == OpeningState.CLOSED){
			return close();
		} else {
			return open();
		}
	}
	
	/** Opens this if it is not already open & is unlocked 
	 * @return if this has been opened*/
	public boolean open() {
		if (state != OpeningState.OPEN && !locked) {
			state = OpeningState.OPENING;
			return true;
		}
		return false;
	}
	
	/** Closes this if it is not already closed 
	 * @return if this has been closed*/
	public boolean close() {
		if (state != OpeningState.CLOSED) {
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
		for (Pickup i : c.getInventory()) {
			if (i instanceof Key && key.equals(i)) { //player has key to unlock this
				locked = false;
			}
		}
	}
	
	public float getOpenPercent() {
		return amtOpen;
	}
	
	/**
	 * Returns whether or not this is locked
	 * 
	 * @return
	 */
	public boolean isLocked() {
		return locked;
	}
	
	

}
