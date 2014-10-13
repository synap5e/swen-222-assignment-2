package space.world;

import space.math.Vector2D;
import space.math.Vector3D;

/**Represents a Bullet. If it collides with a player in the room, it teleports the 
 * player back to a specified place.*/
public class Bullet extends NonStationary {
	private Room room; //the room this has been fired out of
	private Vector3D velocity;
	private Vector2D teleportTo;
	private Room roomTeleportTo; //the room it teleports the player to
	
	/**Constructs a new Bullet
	 * @param position
	 * @param id
	 * @param elevation
	 * @param room The room this has been shot out of
	 * @param velocity*/
	public Bullet(Vector2D position, int id, float elevation,Room room,Vector3D velocity, Vector2D teleportTo, Room roomTeleportTo) {
		super(position, id, elevation, "", "");
		this.room = room;
		this.velocity = velocity;
		this.teleportTo = teleportTo;
		this.roomTeleportTo = roomTeleportTo;
	}

	@Override
	public void update(int delta) {
		Vector3D addTo = velocity.mul(1f/delta);
		Vector2D newPos = getPosition().add(new Vector2D(addTo.getX(),addTo.getY()));
		setPosition(newPos);
		if(!room.contains(newPos)){//bullet has traveled outside of the room so remove it
			room.removeFromRoom(this);
			return;
		}
		setElevation(getElevation() + addTo.getY());
		checkCollidedWithPlayer();
	}
	
	@Override
	public float getCollisionRadius() {
		return 0.5f;
	}

	@Override
	public float getHeight() {
		return 0;
	}
	
	@Override
	public float getAngle() {
		return (float) Math.toDegrees(new Vector2D(velocity.getX(), velocity.getZ()).getPolarAngle());
	}

	@Override
	public boolean canClip() {
		return false;
	}
	
	@Override
	public String getType() {
		return "Bullet";
	}
	
	/**@return the room the bullet has been fired out of */
	public Room getRoom() {
		return room;
	}

	/**@return the velocity of the bullet*/
	public Vector3D getVelocity() {
		return velocity;
	}
	
	/**@return the position the player is teleported to if hit*/
	public Vector2D getTeleportTo() {
		return teleportTo;
	}
	
	/**@return the room the player is teleported to if hit*/
	public Room getRoomTeleportTo() {
		return roomTeleportTo;
	}

	/**Checks whether the bullet has collided with any players. If so, move that player back to a position*/
	private void checkCollidedWithPlayer(){
		Player p = room.collidedWithPlayer(this);
		if(p != null /*&& p.getElevation() <= this.getElevation() && this.getElevation() <= p.getHeight() + p.getElevation()*/){
			p.setPosition(teleportTo); //change to in front of door
			p.setRoom(roomTeleportTo);
		}
	}

}
