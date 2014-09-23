package space.world;

import space.gui.pipeline.viewable.ViewableNonStationary;
import space.math.Vector2D;

public abstract class NonStationary extends Entity implements ViewableNonStationary{
	public NonStationary(Vector2D pos, int i, String d) {
		super(pos, i, d);
	}
}