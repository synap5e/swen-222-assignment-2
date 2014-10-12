package space.world;

import space.math.Vector2D;
import space.math.Vector3D;

public class Bullet extends NonStationary {
	private Room room;
	private Vector3D velocity;
	
	public Bullet(Vector2D position, int id, float elevation,Room room,Vector3D velocity) {
		super(position, id, elevation, "", "");
		this.room = room;
		this.velocity = velocity;
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
	public void update(int delta) {
		Vector3D addTo = velocity.mul(1f/delta);
		Vector2D newPos = getPosition().add(new Vector2D(addTo.getX(),addTo.getY()));
		setPosition(newPos);
		if(!room.contains(newPos)){
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
	
	private void checkCollidedWithPlayer(){
		Player p = room.collidedWithPlayer(this);
		if(p != null && p.getElevation() <= this.getElevation() && this.getElevation() <= p.getHeight() + p.getElevation()){
			p.setPosition(room.getCentre()); //change to in front of door
		}
	}

	public Room getRoom() {
		return room;
	}

	public Vector3D getVelocity() {
		return velocity;
	}

	@Override
	public String getType() {
		return "Bullet";
	}

}
