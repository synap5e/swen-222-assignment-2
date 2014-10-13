package space.world;

import space.math.Vector2D;

/**Represents a Table which is furniture
 * @author Maria Libunao*/
public class Table extends Stationary {

	/**Constructs a new Table
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name*/
	public Table(Vector2D position, int id, float elevation,
			String description, String name) {
		super(position, id, elevation, description, name);
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public float getCollisionRadius() {
		return 4;
	}

	@Override
	public float getHeight() {
		return 3;
	}

	@Override
	public String getType() {
		return "Table";
	}

}
