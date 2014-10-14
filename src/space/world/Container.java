package space.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import space.gui.pipeline.viewable.ViewableOpenable;
import space.math.Vector2D;

/**Represents a container which allows other entities to be placed inside it or take entities out of it. 
 * Can be opened or closed and locked or unlocked. 
 * @author Maria Libunao*/
public abstract class Container extends NonStationary implements ViewableOpenable, Lockable {
	private List<Pickup> itemsContained = new ArrayList<Pickup>(); //the object inside this container
	private boolean isOpen = false;
	private boolean locked;
	private Key key;
	
	/**Constructs a new Container
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name
	 * @param isLocked whether or not this is locked
	 * @param key the key that unlocks this 
	 */
	public Container(Vector2D position, int id, float elevation, String description, String name, boolean isLocked,Key key) {
		super(position, id, elevation, description, name);
		this.locked = isLocked;
		this.key = key;
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
	public Container(Vector2D position, int id, float elevation, String description, String name, boolean isLocked, boolean isOpen,Key key, Collection<Pickup> itemsContained) {
		super(position, id, elevation, description, name);
		this.locked = isLocked;
		this.isOpen = isOpen;
		this.key = key;
		this.itemsContained.addAll(itemsContained);
	}

	/**Whether or not an entity can be placed inside this container.
	 * The height of the pickup must be less than the container's height and the container must be open
	 * @return*/
	public boolean canPutInside(Entity item){
		return item instanceof Pickup && isOpen && item.getHeight() < this.getHeight();
	}

	/**Puts the Pickup inside this container
	 * @param item The Pickup that will be put into this*/
	public void putInside(Entity item){
		if(canPutInside(item)){
			itemsContained.add((Pickup)item);
		}
	}

	/**Removes the specified item from inside the container
	 * @param item the entity to be removed*/
	public boolean removeContainedItem(Entity item){
		if(isOpen){
			return itemsContained.remove(item);
		}
		return false;
	}
	
	@Override
	public boolean interact(Character c,World w){
		return openClose(c);
	}
	
	/**Opens or closes the container.
	 * @return whether the change in state has been successful*/
	public boolean openClose(Character c){
		if(locked){
			unlock(c);
		}
		if(isOpen){
			isOpen = false;
			return true;
		}else if (!locked && !isOpen) {
			isOpen = true;
			return true;
		}
		return false;
	}
	/**Returns the Pickup which is contained in this
	 * @return*/
	public List<Pickup> getItemsContained() {
		return itemsContained;
	}

	/**Unlocks the container if it is locked & the player unlocking it has the key*/
	public void unlock(Character c) {
		if(!locked){return;}
		for (Pickup i : c.getInventory()) {
			if (i instanceof Key && key.equals(i)) { //player has key to unlock this
				locked = false;
			}
		}
	}
	
	@Override 
	public boolean canInteract(){
		return true;
	}
	
	@Override
	public boolean isOpen(){
		return isOpen;
	}
	
	/**@return the key that unlocks or locks this container*/
	public Key getKey() {
		return key;
	}

	/**@return whether the container is locked or not*/
	public boolean isLocked() {
		return locked;
	}


}
