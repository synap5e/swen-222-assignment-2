package space.world;

import space.gui.pipeline.viewable.ViewableObject;
import space.math.Vector2D;

public class Key extends Pickup implements ViewableObject {
	private Exit exit; //the exit it opens
	
	public Key(Vector2D pos, int i, String d,Exit e) {
		super(pos, i,d);
		exit = e;
	}

	public Exit getExit() {
		return exit;
	}

	@Override
	public void update(float f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canClip() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getElevation() {
		// TODO Auto-generated method stub
		return 0;
	}	

}
