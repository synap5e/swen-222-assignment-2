package space.gui.pipeline.mock;

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

}
