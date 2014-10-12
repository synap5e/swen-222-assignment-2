package space.world;

import java.util.Collection;
import java.util.List;

import space.math.Vector2D;

public class Wallet extends Container implements Pickup {

	public Wallet(Vector2D position, int id, float elevation,
			String description, String name) {
		super(position, id, elevation, description, name, false, null);
	}
	
	public Wallet(Vector2D position, int id, float elevation,
			String description, String name, boolean isOpen, Collection<Pickup> itemsContained) {
		super(position, id, elevation, description, name, false,isOpen, null,itemsContained);
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public void update(int delta) {
	}
	
	@Override
	public boolean canInteract(){
		return true;
	}
	
	@Override
	public boolean interact(Character c, World w){
		if(c.withinReach(this.getPosition()) && c.getRoom().containsEntity(this)){
			c.pickup(this);
			c.getRoom().removeFromRoom(this);
			return true;
		}
		return false;
	}
	
	public boolean rummage(Character c){
		return super.openClose(c);
	}

	@Override
	public float getCollisionRadius() {
		return 0;
	}

	@Override
	public float getHeight() {
		return 1;
	}

	@Override
	public String getType() {
		return "Wallet";
	}

}
