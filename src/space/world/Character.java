package space.world;

import java.util.HashSet;
import java.util.Set;

import space.math.Vector2D;

public abstract class Character extends NonStationary {
	private Set<Pickup> inventory = new HashSet<Pickup>(); //items the character is carrying
	public Character(Vector2D pos, int i) {
		super(pos, i, "");
	}

	public void pickupItem(Pickup p){
		inventory.add(p);
	}

	public void dropItem(Object p){
		inventory.remove(p);
	}
	
	public Set<Pickup> getInventory(){
		return inventory;
	}
	
}
