package space.world;

import space.math.Vector2D;

public abstract class NonStationary extends Entity{
	public NonStationary(Vector2D pos, int i, String d) {
		super(pos, i, d);
	}
	
	public abstract void update(float delta);
}