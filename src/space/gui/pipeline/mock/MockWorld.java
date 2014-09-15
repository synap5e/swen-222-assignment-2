package space.gui.pipeline.mock;

import java.util.Arrays;
import java.util.List;

import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWorld;
import space.math.Vector2D;

/**
 * 
 * @author Simon Pinfold
 *
 */
public class MockWorld implements ViewableWorld {

	private MockRoom room = new MockRoom();

	@Override
	public ViewableRoom getRoomAt(Vector2D pos) {
		return room;
	}

	public void update(int delta) {
		room.update(delta);
	}

	@Override
	public List<? extends ViewableRoom> getViewableRooms() {
		return Arrays.asList(room);
	}

}
