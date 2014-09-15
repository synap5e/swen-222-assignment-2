package space.gui.pipeline.mock;

import java.util.Arrays;
import java.util.List;

import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWord;
import space.util.Vec2;

public class MockWorld implements ViewableWord {

	private MockRoom room = new MockRoom();

	@Override
	public ViewableRoom getRoomAt(Vec2 pos) {
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
