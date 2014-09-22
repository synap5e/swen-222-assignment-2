package space.world;

import space.gui.pipeline.viewable.ViewableDoor;
import space.math.Vector2D;

public class Exit extends NonStationary implements ViewableDoor{
	private Room room1;
	private Room room2;
	private boolean oneWay; //if oneWay, can only get from room1 to room2
	private boolean locked;
	private float amtOpen = 0; 
	
	public Exit(Vector2D pos, int i, String desc,Room r1, Room r2, boolean ow, boolean l){
		super(pos, i, desc);
		room1 = r1;
		room2 = r2;
		oneWay = ow;
		locked = l;
	}

	public boolean isLocked() {
		return locked;
	}

	public Room getRoom1() {
		return room1;
	}

	public Room getRoom2() {
		return room2;
	}

	public boolean isOneWay() {
		return oneWay;
	}

	@Override
	public float getAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public Vector2D getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getOpenPercent() {
		return amtOpen;
	}
	
	public boolean canGoThrough(Player p){
		if(!locked){
			return true;
		}
		for(Pickup i : p.getInventory()){
			if(i instanceof Key && ((Key) i).getExit().equals(this)){ //player has key to unlock this exit
				return true;
			}
		}
		return false;
	}
	
}
