package space.gui.pipeline.mock;

import space.gui.pipeline.ViewablePlayer;
import space.util.Vec2;
import space.util.Vec3;

public class MockPlayer implements ViewablePlayer {

	public Vec2 getPosition() {
		return new Vec2(0, 0);
	}

	@Override
	public Vec3 getLookDirection() {
		// TODO Auto-generated method stub
		return null;
	}

}
