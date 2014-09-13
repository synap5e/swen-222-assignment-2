package space.world;

public class Exit {
	private Room room1;
	private Room room2;
	private boolean oneWay; //if oneWay, can only get from room1 to room2
	private boolean locked;
	
	public Exit(Room r1, Room r2, boolean ow, boolean l){
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
	
	
}
