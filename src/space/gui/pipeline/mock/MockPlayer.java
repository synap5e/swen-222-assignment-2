package space.gui.pipeline.mock;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import space.gui.pipeline.ViewablePlayer;
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

	public void update(int delta) {

		int x = Mouse.getX();
		int y = Mouse.getY();


		Mouse.setGrabbed(true);
		Mouse.setClipMouseCoordinatesToWindow(false);

		if (lastx != -1){
			yRotation += (x-lastx)/8.0;
			xRotation += (y-lasty)/8.0;

			if (xRotation >= 360) xRotation = 359.9f;
			if (xRotation <= 180) xRotation = 180.1f;
		}

		Vec3 moveDirection = getLook().mul(delta/100f);
		Vec3 moveDelta = new Vec3(0, 0, 0);

		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			moveDelta.addLocal(moveDirection);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			moveDelta.subLocal(Vec3.cross(moveDirection, new Vec3(0,1,0)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			moveDelta.addLocal(Vec3.cross(moveDirection, new Vec3(0,1,0)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			moveDelta.subLocal(moveDirection);
		}

		pos.addLocal(new Vec2(moveDelta.getX(), moveDelta.getZ()));

		lastx = x;
		lasty = y;
	}



}
