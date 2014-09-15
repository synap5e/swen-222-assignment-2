package space.gui.pipeline.mock;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import space.gui.pipeline.viewable.ViewablePlayer;
import space.util.Vec2;
import space.util.Vec3;

public class MockPlayer implements ViewablePlayer {

	private Vec2 pos = new Vec2(0,0);

	public Vec2 getPosition() {
		return pos;
	}

	@Override
	public Vec3 getLookDirection() {
		return getLook();
	}

	@Override
	public float getEyeHeight() {
		return (float) (8 - 2 * Math.pow(jumpTime*2 - 1, 2));
	}



	float xRotation = 300f;
	float yRotation = 90f;


	// this code is only here because this is a mock object for
	// developing the renderer
	private int lastx = -1;
	private int lasty = -1;
	private static float DEGREES_TO_RADIANS(float degrees){
		return (float) ((degrees) * (Math.PI / 180.0));
	}
	private Vec3 getLook(){
		float x_circ = (float) (Math.cos(DEGREES_TO_RADIANS(yRotation)) * Math.sin(DEGREES_TO_RADIANS(xRotation)));
		float y_circ = (float) (  										  Math.cos(DEGREES_TO_RADIANS(xRotation)));
		float z_circ = (float) (Math.sin(DEGREES_TO_RADIANS(yRotation)) * Math.sin(DEGREES_TO_RADIANS(xRotation)));
		return new Vec3(x_circ, y_circ, z_circ);
	}

	float jumpTime = 0;

	public void update(int delta) {

		int x = Mouse.getX();
		int y = Mouse.getY();


		Mouse.setGrabbed(true);
		Mouse.setClipMouseCoordinatesToWindow(false);

		if (lastx != -1){
			Vec2 mouseDelta = new Vec2(x-lastx,y-lasty);
			this.moveLook(mouseDelta);
		}

		Vec3 moveDirection = this.getLook();
		Vec3 moveDelta = new Vec3(0, 0, 0);

		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			moveDelta.addLocal(moveDirection);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			moveDelta.subLocal(moveDirection.cross(new Vec3(0,1,0)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			moveDelta.addLocal(moveDirection.cross(new Vec3(0,1,0)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			moveDelta.subLocal(moveDirection);
		}
		if (moveDelta.sqLen() != 0){
			moveDelta = moveDelta.normalized().mul(delta/75f);
			pos.addLocal(new Vec2(moveDelta.getX(), moveDelta.getZ()));
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && jumpTime == 0){
			jumpTime = 1;
		}
		if (jumpTime > 0){
			jumpTime -= delta/500f;
		} else {
			jumpTime = 0;
		}

		lastx = x;
		lasty = y;
	}

	private void moveLook(Vec2 mouseDelta) {
		this.xRotation += mouseDelta.getX();
		this.yRotation += mouseDelta.getY();

		if (xRotation >= 360) xRotation = 359.9f;
		if (xRotation <= 180) xRotation = 180.1f;
	}

	public Vec2 getFacing() {
		return new Vec2(getLook().getX(), getLook().getZ());
	}

	@Override
	public float getAngle() {
		// TODO
		return 0;
	}

	@Override
	public float getElevation() {
		return getEyeHeight()-6;
	}

	@Override
	public boolean canMove() {
		return true;
	}
}
