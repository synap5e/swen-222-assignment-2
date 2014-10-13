package space.world;

import space.math.Vector2D;

/**Represents a Light on the ceiling
 * @author Maria Libunao*/
public class Light extends Stationary {
	
	/**Constructs a new Light
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name*/
	public Light(Vector2D position, int id, float elevation,String description, String name) {
		super(position, id, elevation, description, name);
	}

	@Override
	public float getCollisionRadius() {
		return 0;
	}

	@Override
	public float getHeight() {
		return 1;
	}

	@Override
	public boolean canClip() {
		return false;
	}

	@Override
	public String getType() {
		return "Light";
	}

}
