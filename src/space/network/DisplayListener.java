package space.network;

import java.util.List;

import space.world.Container;
import space.world.Pickup;

/**
 * DisplayListener enables the display of the inventory screen when rifling.
 * 
 * @author Matt Graham 300211545
 */
public interface DisplayListener {
	
	/**
	 * 
	 * 
	 * @param container the container being rifled.
	 * @param playerPickups 
	 */
	public void onInventoryExchange(Container container, List<Pickup> playerPickups);
}
