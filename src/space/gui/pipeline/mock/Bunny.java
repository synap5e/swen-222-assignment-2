package space.gui.pipeline.mock;

import org.lwjgl.input.Keyboard;

import space.gui.pipeline.viewable.ViewableObject;
import space.math.Vector2D;

public class Bunny implements ViewableObject {

	private Vector2D pos;
	
	// units per second
	public float angle;
	private float anglevel = 0;

	private float jumpTime;

	public Bunny(Vector2D pos) {
		this.pos = pos;
		
		angle = (float) (Math.random() * Math.PI*2);
		//anglevel = 0.001f;
	}

	/*private void changeDirection() {
		if (Math.random() < 0.7){
			anglevel = 0;
		} else {
			anglevel = (float) ((Math.random() - 0.5) / 100);
		}
	}*/

	@Override
	public Vector2D getPosition() {
		return pos;
	}
	
	public void update(int delta) {
		pos.addLocal(getFacing().mul(delta/250f));
		
		if (pos.sqLen() > 250){
			angle += Math.PI + ((Math.random()-0.5) * Math.PI/2);
		}
		
		if (Math.random() < 0.005 && jumpTime == 0){
			jumpTime = 1;
		}
		if (jumpTime > 0){
			jumpTime -= delta/700f;
		} else {
			jumpTime = 0;
		}
		/*if (Math.random() < 0.1){
			changeDirection();
		}*/
	}

	public Vector2D getFacing() {
		return new Vector2D((float)Math.cos(angle), (float) Math.sin(angle));
	}

	@Override
	public float getAngle() {
		return (float) (Math.toDegrees(angle));
	}

	@Override
	public float getElevation() {
		return (float) (1 - 1 * Math.pow(jumpTime*2 - 1, 2));
	}

	@Override
	public boolean canMove() {
		return true;
	}

}
