package space.world;

import java.util.ArrayList;
import java.util.List;

import space.math.Vector2D;
import space.world.NonStationary.OpeningState;

/**Containers like bags, backpacks, boxes, chests, or suitcases that can have other objects inside
them. The player should be able to open and close containers, put objects inside them or get
them out. You must be able to put one container (like a wallet) inside another container (like a
suitcase).*/
public abstract class Container extends NonStationary {
	private List<Pickup> itemsContained = new ArrayList<Pickup>(); //the object inside this container
	private float amtOpen = 0;
	private OpeningState state = OpeningState.CLOSED;
	private static final float OPEN_DURATION = 400;

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
	public Container(Vector2D position, int id, float elevation, String description, String name) {
		super(position, id, elevation, description, name);
	}

	/**Whether or not a Pickup can be placed inside this container
	 * @return*/
	public boolean canPutInside(Entity item){
		return item instanceof Pickup && state == OpeningState.OPEN && item.getHeight() < this.getHeight();
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
		if(state == OpeningState.OPEN){
			return itemsContained.remove(item);
		}
		return false;
	}

	@Override
	public void update(int delta) {
		if (state == OpeningState.CLOSING) {
			amtOpen -= delta / OPEN_DURATION;
			if (amtOpen <= 0) {
				amtOpen = 0;
				state = OpeningState.CLOSED;
			}
		} else if (state == OpeningState.OPENING) {
			amtOpen += delta / OPEN_DURATION;
			if (amtOpen >= 1) {
				amtOpen = 1;
				state = OpeningState.OPEN;
			}
		}

	}

	/**Returns the Pickup which is contained in this
	 * @return*/
	public List<Pickup> getItemsContained() {
		return itemsContained;
	}

	public float getOpenAmount(){
		return amtOpen;
	}

}
