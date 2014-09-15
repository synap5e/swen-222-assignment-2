package space.gui.pipeline.mock;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import space.gui.pipeline.viewable.ViewablePlayer;
import space.math.Vector2D;
import space.math.Vector3D;

public class MockPlayer implements ViewablePlayer {

	private Vector2D pos = new Vector2D(0,0);

	public Vector2D getPosition() {
		return pos;
	}

	@Override
	public Vector3D getLookDirection() {
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
	private Vector3D getLook(){
		float x_circ = (float) (Math.cos(DEGREES_TO_RADIANS(yRotation)) * Math.sin(DEGREES_TO_RADIANS(xRotation)));
		float y_circ = (float) (  										  Math.cos(DEGREES_TO_RADIANS(xRotation)));
		float z_circ = (float) (Math.sin(DEGREES_TO_RADIANS(yRotation)) * Math.sin(DEGREES_TO_RADIANS(xRotation)));
		return new Vector3D(x_circ, y_circ, z_circ);
	}

	float jumpTime = 0;

	public void update(int delta) {

		int x = Mouse.getX();
		int y = Mouse.getY();


		Mouse.setGrabbed(true);
		Mouse.setClipMouseCoordinatesToWindow(false);

		if (lastx != -1){
			Vector2D mouseDelta = new Vector2D(x-lastx,y-lasty);
			this.moveLook(mouseDelta);
		}

		Vector3D moveDirection = this.getLook();
		Vector3D moveDelta = new Vector3D(0, 0, 0);

		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			moveDelta.addLocal(moveDirection);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			moveDelta.subLocal(moveDirection.cross(new Vector3D(0,1,0)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			moveDelta.addLocal(moveDirection.cross(new Vector3D(0,1,0)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			moveDelta.subLocal(moveDirection);
		}
		if (moveDelta.sqLen() != 0){
			moveDelta = moveDelta.normalized().mul(delta/75f);
			pos.addLocal(new Vector2D(moveDelta.getX(), moveDelta.getZ()));
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

	private void moveLook(Vector2D mouseDelta) {
		this.xRotation += mouseDelta.getY()/8f;
		this.yRotation += mouseDelta.getX()/8f;

		if (xRotation >= 360) xRotation = 359.9f;
		if (xRotation <= 180) xRotation = 180.1f;
	}

	public Vector2D getFacing() {
		return new Vector2D(getLook().getX(), getLook().getZ());
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
