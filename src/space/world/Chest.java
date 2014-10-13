package space.world;

import java.util.Collection;

import space.math.Vector2D;

/**Represents a chest which is a container. Entities can be placed inside the chest.
 * The chest cannot be picked up
 * @author Maria Libunao*/
public class Chest extends Container {

	/**Constructs a new Chest
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name
	 * @param isLocked whether or not this is locked
	 * @param key the key that unlocks this 
	 */
	public Chest(Vector2D position, int id, float elevation,
			String description, String name, boolean isLocked, Key key) {
		super(position, id, elevation, description, name, isLocked, key);
	}
	
	/**Constructs a new Container
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name
	 * @param  isLocked whether or not this is locked
	 * @param isOpen
	 * @param key the key that unlocks this
	 * @param itemsContained the items which will be inside this
	 * */
	public Chest(Vector2D position, int id, float elevation,
			String description, String name, boolean isLocked, Key key,boolean isOpen, Collection<Pickup> itemsContained) {
		super(position, id, elevation, description, name, isLocked,isOpen, key,itemsContained);
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public void update(int delta) {
	}

	@Override
	public float getCollisionRadius() {
		return 1;
	}

	@Override
	public float getHeight() {
		return 5;
	}

	@Override
	public String getType() {
		return "Chest";
	}

}
