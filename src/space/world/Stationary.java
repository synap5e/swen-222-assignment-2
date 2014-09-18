package space.world;

import space.math.Vector2D;

public abstract class Stationary extends Entity{
	
	public Stationary(Vector2D pos, int i, String d) {
		super(pos, i, d);
	}

	@Override
	public final boolean canClip(){
		return true;
	}
	
	@Override
	public final void setPosition(Vector2D pos){
	}
}
