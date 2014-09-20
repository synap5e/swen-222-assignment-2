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

	//private MockRoom room = new MockRoom.room1;

	@Override
	public ViewableRoom getRoomAt(Vector2D pos) {
		if (MockRoom.room2.contains(pos)){
			return MockRoom.room2;
		}
		return MockRoom.room1;
	}

	public void update(int delta) {
		MockRoom.room1.update(delta);
		MockRoom.room2.update(delta);
	}

	@Override
	public List<? extends ViewableRoom> getViewableRooms() {
		return Arrays.asList(MockRoom.room1, MockRoom.room2);
	}

}
