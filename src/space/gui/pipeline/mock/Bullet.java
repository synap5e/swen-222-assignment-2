package space.gui.pipeline.mock;

import space.gui.pipeline.viewable.ViewableNonStationary;
import space.gui.pipeline.viewable.ViewableObject;
import space.math.Vector2D;
import space.math.Vector3D;

public class Bullet implements ViewableObject, ViewableNonStationary{

	Vector3D pos;
	private Vector3D vel;
	
	public Bullet(Vector3D pos, Vector3D vel) {
		this.pos = pos;
		this.vel = vel;
	}
	
	@Override
	public Vector2D getPosition() {
		return new Vector2D(pos.getX(), pos.getZ());
	}

	@Override
	public float getAngle() {
		return (float) Math.toDegrees(new Vector2D(vel.getX(), vel.getZ()).getPolarAngle());
	}

	@Override
	public float getElevation() {
		return pos.getY();
	}

	public void update(int delta) {
		pos.addLocal(vel.mul(1f/delta));
	}

}
