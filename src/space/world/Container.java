package space.world;

import java.util.ArrayList;
import java.util.List;

import space.math.Vector2D;

/**Containers like bags, backpacks, boxes, chests, or suitcases that can have other objects inside
them. The player should be able to open and close containers, put objects inside them or get
them out. You must be able to put one container (like a wallet) inside another container (like a
suitcase).*/
public abstract class Container extends Openable {
	private List<Pickup> itemsContained = new ArrayList<Pickup>(); //the object inside this container

	/**
	 * Constructs a new container
	 *
	 * @param position
	 *            The position
	 * @param id
	 *            The id
	 * @param elevation
	 *            The elevation
	 * @param description
	 *            The description
	 */
	public Container(Vector2D position, int id, float elevation, String description, String name, boolean isLocked,Key key) {
		super(position, id, elevation, description, name,400,isLocked,key);
	}

	/**Whether or not a Pickup can be placed inside this container
	 * @return*/
	public boolean canPutInside(Entity item){
		return item instanceof Pickup && super.getOpenPercent() == 1 && item.getHeight() < this.getHeight();
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
		if(super.getOpenPercent() == 1){
			return itemsContained.remove(item);
		}
		return false;
	}

	/**Returns the Pickup which is contained in this
	 * @return*/
	public List<Pickup> getItemsContained() {
		return itemsContained;
	}

}
