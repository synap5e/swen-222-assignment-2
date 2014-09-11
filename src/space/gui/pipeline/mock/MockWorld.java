package space.gui.pipeline.mock;

import space.gui.pipeline.ViewableRoom;
import space.gui.pipeline.ViewableWord;
import space.util.Vec2;

public class MockWorld implements ViewableWord {

	private ViewableRoom room = new MockRoom();

	@Override
	public ViewableRoom getRoomAt(Vec2 pos) {
		return room;
	}

}
