package space.gui.pipeline.mock;

import space.gui.pipeline.viewable.ViewableWall;
import space.util.Vec2;

public class MockWall implements ViewableWall{

	private Vec2 v2;
	private Vec2 v1;

	public MockWall(Vec2 v1, Vec2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public Vec2 getStart() {
		return v1;
	}

	@Override
	public Vec2 getEnd() {
		return v2;
	}

}
