package space.world;

import java.util.Collection;
import space.math.Vector2D;

/**Represents a Wallet which can be picked up by a player.
 * Can also be put inside another container, so long as it fits inside.
 * Cannot be locked or unlocked
 * @author Maria Libunao*/
public class Wallet extends Container implements Pickup {

	/**Constructs a new Wallet
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name
	 */
	public Wallet(Vector2D position, int id, float elevation,
			String description, String name) {
		super(position, id, elevation, description, name, false, null);
	}
	
	/**Constructs a new Wallet
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name
	 * @param isOpen if the wallet is open
	 * @param itemsContained the entities in the wallet
	 */
	public Wallet(Vector2D position, int id, float elevation,
			String description, String name, boolean isOpen, Collection<Pickup> itemsContained) {
		super(position, id, elevation, description, name, false,isOpen, null,itemsContained);
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public void update(int delta) {
	}
	
	@Override
	public boolean canInteract(){
		return true;
	}
	
	@Override
	public boolean interact(Character c, World w){
		if(c.withinReach(this.getPosition()) && c.getRoom().containsEntity(this)){
			c.pickup(this);
			c.getRoom().removeFromRoom(this);
			return true;
		}
		return false;
	}
	
	/**Opens or closes the wallet.
	 * @return whether the change in state has been successful*/
	public boolean rummage(Character c){
		return super.openClose(c);
	}

	@Override
	public float getCollisionRadius() {
		return 2;
	}

	@Override
	public float getHeight() {
		return 1;
	}

	@Override
	public String getType() {
		return "Wallet";
	}

}
