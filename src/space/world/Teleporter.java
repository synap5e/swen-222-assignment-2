package space.world;

import space.math.Vector2D;

public class Teleporter extends Stationary{
	private Vector2D teleportsToPos; //player teleport destination
	private static final float HEIGHT = 11;
	public Teleporter(Vector2D teleporterPos, Vector2D teleportsToPos, int id, float elevation,
			String description, String name) {
		super(teleporterPos, id, elevation, description, name);
		this.teleportsToPos = teleportsToPos;
	}
	
	@Override
	public boolean canInteract(){
		return true;
	}
	
	@Override
	public boolean interact(Character c, World world){
		c.setPosition(teleportsToPos);
		Room room = world.getRoomAt(teleportsToPos);
		if(room.isPositionVacant(teleportsToPos, c.getCollisionRadius())){
			c.setPosition(teleportsToPos);
			c.getRoom().removeFromRoom(c);
			room.putInRoom(c);
			c.setRoom(room);
		}
		return true;
	}
	
	@Override
	public float getAngle() {
		return 0;
	}
	
	@Override
	public boolean canClip() {
		return false;
	}
	
	@Override
	public float getCollisionRadius() {
		return 0;
	}
	
	@Override
	public float getHeight() {
		return HEIGHT;
	}
	
	public Vector2D getTeleportsTo() {
		return teleportsToPos;
	}

}
