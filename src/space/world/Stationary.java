package space.world;

import space.gui.pipeline.viewable.ViewableStationary;
import space.math.Vector2D;

public abstract class Stationary extends Entity implements ViewableStationary{
	
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
	
	public final void update(int delta){
	}
}
