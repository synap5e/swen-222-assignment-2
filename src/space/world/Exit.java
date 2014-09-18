package space.world;

import space.math.Vector2D;

public class Exit extends NonStationary{
	private Room room1;
	private Room room2;
	private boolean oneWay; //if oneWay, can only get from room1 to room2
	private boolean locked;
	
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
	public float getElevation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canMove() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canClip() {
		return true;
	}
	
	
}
