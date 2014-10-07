package space.world;

import space.math.Vector2D;

public class Key extends NonStationary implements Pickup {
	private static final float COL_RADIUS = 1; // the collision radius

	/**
	 * Creates a new Key entity
	 * 
	 * @param position
	 *            The position of the key
	 * @param id
	 *            The id
	 * @param elevation
	 *            The elevation
	 * @param description
	 *            The description of the key
	 * @param door
	 *            The door it unlocks
	 */
	public Key(Vector2D position, int id, float elevation, Door door, String description, String name) {
		super(position, id, elevation, description,name);
	}

	@Override
	public void update(int delta) {
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public float getAngle() {
		return 0;
	}

	@Override
	public float getCollisionRadius() {
		return COL_RADIUS;
	}

	/**
	 * Returns the door the key unlocks
	 * 
	 * @return
	 */

	@Override
	public boolean canInteract(){
		return true;
	}

	@Override
	public boolean interact(Character c, World w){
		if(c.withinReach(super.getPosition()) && c.getRoom().containsEntity(this)){
			c.pickup(this);
			c.getRoom().removeFromRoom(this);
			return true;
		}
		return false;
	}
	
	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
