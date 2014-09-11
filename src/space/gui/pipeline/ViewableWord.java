package space.gui.pipeline;

import space.util.Vec2;

public interface ViewableWord {

	/** Gets the room at a given location, or null if there is no room at that position
	 * 
	 * @param pos the position
	 * @return the room at the position
	 */
	public ViewableRoom getRoomAt(Vec2 pos);

}
