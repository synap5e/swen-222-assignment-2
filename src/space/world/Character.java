package space.world;

import java.util.HashSet;
import java.util.Set;

import space.math.Vector2D;

public abstract class Character extends NonStationary {
	private Set<Pickup> inventory = new HashSet<Pickup>(); // items the
															// character is
															// carrying
	private Room room; // the room the character is in

	/**
	 * Creates the character
	 * 
	 * @param pos
	 *            The character's position
	 * @param i
	 *            The id of the character
	 * @param r
	 *            The room the character is in
	 */
	public Character(Vector2D pos, int i/* , Room r */) {
		super(pos, i, 0, "");
		// room = r;
	}

	/**
	 * Adds the pickup-able entity into the character's inventory
	 * 
	 * @param p
	 *            The entity that will be added
	 */
	public void pickup(Pickup p) {
		inventory.add(p);
	}

	/**
	 * Removes the object from the character's inventory if it is there.
	 * 
	 * @param p
	 *            The object that will be removed
	 * @return if the object has been successfully removed
	 */
	public boolean drop(Object p) {
		return inventory.remove(p);
	}

	/**
	 * Returns what room the character is in
	 * 
	 * @return
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * Changes the room the character is in
	 * 
	 * @param room
	 *            The room the character would be in
	 */
	public void setRoom(Room room) {
		this.room = room;
	}

	/**
	 * Returns the character's inventory
	 * 
	 * @return
	 */
	public Set<Pickup> getInventory() {
		return inventory;
	}

	/**
	 * Returns whether or not a particular position is reachable from where the
	 * character is currently standing
	 * 
	 * @param pos
	 *            The position the character is trying to reach for
	 * @return
	 */
	public abstract boolean withinReach(Vector2D pos);

}
