package space.world;

import space.math.Vector2D;
import space.world.Room;

/**Represents a Turret which shoots bullets at the nearest player*/
public class Turret extends NonStationary{
	private boolean shutDown = false;
	private Room room;
	private TurretStrategy strategy;

	/**Constructs a new Turret
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name
	 * @param room The room this turret is in*/
	public Turret(Vector2D position, int id, float elevation,String description, String name, Room room) {
		super(position, id, elevation, description, name);
		this.room = room;
	}

	/**Constructs a new Turret
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name
	 * @param room The room this turret is in
	 * @param */
	public Turret(Vector2D position, int id, float elevation,String description, String name, Room room, boolean isShutDown) {
		super(position, id, elevation, description, name);
		this.room = room;
	}

	/**Stops the turret from updating and removes it from the room*/
	public void shutDown(){
		shutDown = true;
		room.removeFromRoom(this);
	}

	@Override
	public void update(int delta) {
		if(shutDown){return;}
		strategy.update(delta);
	}

	/**@return the strategy the turret uses*/
	public TurretStrategy getStrategy() {
		return strategy;
	}

	/**Sets the strategy of the turret
	 * @param strategy*/
	public void setStrategy(TurretStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public float getAngle() {
		return strategy.getAngle();
	}

	public void setAngle(float angle){
		strategy.setAngle(angle);
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public float getCollisionRadius() {
		return 2;
	}

	@Override
	public float getHeight() {
		return 4.9f;
	}

	/**@return whether or not the turret has been shut down*/
	public boolean isShutDown() {
		return shutDown;
	}

	/**@return the room the turret is in*/
	public Room getRoom() {
		return room;
	}

	@Override
	public String getType() {
		return "Turret";
	}

}