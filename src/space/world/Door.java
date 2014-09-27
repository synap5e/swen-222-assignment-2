package space.world;

import space.gui.pipeline.viewable.ViewableDoor;
import space.math.Vector2D;

public class Door extends NonStationary implements ViewableDoor{
	private Room room1;
	private Room room2;
	private boolean oneWay; //if oneWay, can only get from room1 to room2
	private boolean locked;
	private float amtOpen = 0; 
	private String description;
	private int i;
	private Vector2D position;
	
	public Door(Vector2D pos, int i, String desc,Room r1, Room r2, boolean ow, boolean l){
		super(pos, i, desc);
		room1 = r1;
		room2 = r2;
		oneWay = ow;
		locked = l;
		description = desc;
		this.i=i;
		position = pos;
	}
	
	public Vector2D getPosition(){
		return position;
	}
	public int geti(){
		return i;
	}
	public String getDescription(){
		return description;
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
	public boolean canClip() {
		return true;
	}

	@Override
	public float getOpenPercent() {
		return amtOpen;
	}
	
	public void unlock(Player p){
		for(Pickup i : p.getInventory()){
			if(i instanceof Key && ((Key) i).getExit().equals(this)){ //player has key to unlock this exit
				locked = false;
				return;
			}
		}
	}
	
	public boolean canGoThrough(Character c){
		return !locked && amtOpen == 1 ;
	}

	@Override
	public void update(int delta) {
		// TODO opening door stuff
		
	}

	@Override
	public float getCollisionRadius() {
		// TODO
		return 0;
	}
	
}
