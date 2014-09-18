package space.world;

import space.math.Vector2D;

public abstract class Pickup extends NonStationary{

	public Pickup(Vector2D pos, int i,String d) {
		super(pos, i, d);
	}
}
