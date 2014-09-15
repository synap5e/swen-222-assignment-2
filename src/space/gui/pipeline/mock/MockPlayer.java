package space.gui.pipeline.mock;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import space.gui.pipeline.viewable.ViewablePlayer;
import space.math.Vector2D;
import space.math.Vector3D;

public class MockPlayer implements ViewablePlayer {

	private Vector2D pos = new Vector2D(0,0);
	private static final float EYE_HEIGHT = 6;
	private static final float JUMP_HEIGHT = 2;

	private float jumpTime = 0;

	private float xRotation = 90;
	private float yRotation = 100;

	public Vector2D getPosition() {
		return pos;
	}

	private static float DEGREES_TO_RADIANS(float degrees){
		return (float) ((degrees) * (Math.PI / 180.0));
	}

	@Override
	public Vector3D getLookDirection() {
		float x_circ = (float) (Math.cos(DEGREES_TO_RADIANS(yRotation)) * Math.sin(DEGREES_TO_RADIANS(xRotation + 180)));
		float y_circ = (float) (  										  Math.cos(DEGREES_TO_RADIANS(xRotation + 180)));
		float z_circ = (float) (Math.sin(DEGREES_TO_RADIANS(yRotation)) * Math.sin(DEGREES_TO_RADIANS(xRotation + 180)));
		return new Vector3D(x_circ, y_circ, z_circ);
	}

	@Override
	public float getEyeHeight() {
		return (float) ((EYE_HEIGHT+JUMP_HEIGHT) - JUMP_HEIGHT * Math.pow(jumpTime*2 - 1, 2));
	}

	private void moveLook(Vector2D mouseDelta) {
		this.xRotation += mouseDelta.getY()/8f;
		this.yRotation += mouseDelta.getX()/8f;

		if (xRotation >= 360) xRotation = 360f;
		if (xRotation <= 0) xRotation = 0f;
	}

	@Override
	public float getAngle() {
		return yRotation;
	}

	@Override
	public float getElevation() {
		return getEyeHeight()-EYE_HEIGHT;
	}

	@Override
	public boolean canMove() {
		return true;
	}






	// BEGIN CONTROLLER CODE - THIS SHOULDN'T BE IN THIS CLASS

	private int lastx = -1;
	private int lasty = -1;

	public void update(int delta) {

		int x = Mouse.getX();
		int y = Mouse.getY();


		Mouse.setGrabbed(true);
		Mouse.setClipMouseCoordinatesToWindow(false);

		if (lastx != -1){
			Vector2D mouseDelta = new Vector2D(x-lastx,y-lasty);
			this.moveLook(mouseDelta);
		}

		Vector3D moveDirection = this.getLookDirection();
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

}
