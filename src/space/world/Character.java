package space.world;

import java.util.HashSet;
import java.util.Set;

import space.math.Vector2D;

public abstract class Character extends NonStationary {
	private Set<Pickup> inventory = new HashSet<Pickup>(); //items the character is carrying
	private Room room;
	public Character(Vector2D pos, int i/*, Room r*/) {
		super(pos, i, "");
		//room = r;
	}

	public void pickupItem(Pickup p){
		inventory.add(p);
	}

	public void dropItem(Object p){
		inventory.remove(p);
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public Set<Pickup> getInventory(){
		return inventory;
	}
	
	public abstract boolean withinReach(Vector2D pos);
	
}
