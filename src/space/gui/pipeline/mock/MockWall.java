package space.gui.pipeline.mock;

import java.util.Map;

import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewableWall;
import space.math.Vector2D;

/**
 * 
 * @author Simon Pinfold
 *
 */
public class MockWall implements ViewableWall{

	private Vector2D v2;
	private Vector2D v1;

	public MockWall(Vector2D v1, Vector2D v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public Vector2D getStart() {
		return v1;
	}

	@Override
	public Vector2D getEnd() {
		return v2;
	}

	@Override
	public Map<Float, ? extends ViewableDoor> getDoors() {
		// TODO Auto-generated method stub
		return null;
	}

}
