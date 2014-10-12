package space.world;

import space.math.Vector2D;
import space.world.Room;

public class Turret extends NonStationary{
	private boolean shutDown = false;
	private Room room;
	private TurretStrategy strategy;
	
	public Turret(Vector2D position, int id, float elevation,String description, String name, Room room) {
		super(position, id, elevation, description, name);
		this.room = room;
	}

	public TurretStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(TurretStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public float getAngle() {
		return strategy.getAngle();
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public void update(int delta) {
		if(shutDown){return;}
		strategy.update(delta);
	}
	
	public void shutDown(){
		shutDown = true;
		room.removeFromRoom(this);
	}

	@Override
	public float getCollisionRadius() {
		return 5;
	}

	@Override
	public float getHeight() {
		return 5;
	}

	public boolean isShutDown() {
		return shutDown;
	}

	public Room getRoom() {
		return room;
	}

	@Override
	public String getType() {
		return "Turret";
	}

}