package space.world;

import space.gui.pipeline.viewable.ViewablePlayer;
import space.math.Vector2D;
import space.math.Vector3D;

/** Represents a player in the game */
public class Player extends Character implements ViewablePlayer {
	private int points;
	private final float reach = 8; // how far the player can reach
	private float jumpTime = 0;
	private float xRotation = 90;
	private float yRotation = 100;
	private boolean torchOn = true;

	private static final float EYE_HEIGHT = 6;
	private static final float JUMP_HEIGHT = 2;
	private static final float JUMP_LENGTH = 500;
	private static final float COL_RADIUS = 1.5f;

	/**
	 * Constructs a new player
	 * 
	 * @param position
	 *            The position
	 * @param id
	 *            The id
	 * @param name
	 */
	public Player(Vector2D position, int id, String name) {
		super(position, id, name);
	}

	/**
	 * Changes the player's look direction
	 * 
	 * @param mouseDelta
	 *            the difference in look direction
	 */
	public void moveLook(Vector2D mouseDelta) {
		this.xRotation += mouseDelta.getY() / 8f;
		this.yRotation += mouseDelta.getX() / 8f;

		if (xRotation >= 180)
			xRotation = 179;
		if (xRotation <= 0)
			xRotation = 1;
	}

	/** Makes player jump */
	public void jump() {
		if (jumpTime == 0) {
			jumpTime = 1;
		}
	}

	/**
	 * This updates the player's jump status
	 */
	@Override
	public void update(int delta) {
		if (jumpTime > 0) {
			jumpTime -= delta / JUMP_LENGTH;
			if (jumpTime < 0) {
				jumpTime = 0;
			}
		}
	}

	@Override
	public boolean withinReach(Vector2D pos) {
		return getPosition().sub(pos).sqLen() <= Math.pow(reach, 2);
	}

	@Override
	public float getAngle() {
		return yRotation;
	}

	@Override
	public float getElevation() {
		return getEyeHeight() - EYE_HEIGHT;
	}

	/**
	 * Returns the player's points
	 * 
	 * @return
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Sets the player's points to the passed parameter
	 * 
	 * @param points
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public boolean canClip() {
		return false;
	}

	@Override
	public boolean isTorchOn() {
		return torchOn;
	}

	/**
	 * Turns the torch on or off
	 * 
	 * @param t
	 *            whether the torch would be on or off
	 */
	public void setTorch(boolean t) {
		this.torchOn = t;
	}

	/**
	 * Sets the player's x rotation
	 * 
	 * @param x
	 *            the new x rotation
	 */
	public void setXRotation(float x) {
		xRotation = x;
	}

	/**
	 * Sets the player's y rotation
	 * 
	 * @param x
	 *            the new y rotation
	 */
	public void setYRotation(float y) {
		yRotation = y;
	}
	
	/**Returns the player's x rotation
	 * @return
	 */
	public float getXRotation(){
		return xRotation;
	}
	
	/**Returns the player's y rotation
	 * @return
	 */
	public float getYRotation(){
		return yRotation;
	}

	@Override
	public float getCollisionRadius() {
		return COL_RADIUS;
	}

	@Override
	public float getEyeHeight() {
		return (float) ((EYE_HEIGHT + JUMP_HEIGHT) - JUMP_HEIGHT
				* Math.pow(jumpTime * 2 - 1, 2));
	}
	
	@Override
	public Vector3D getLookDirection() {
		float x_circ = (float) (Math.cos(DEGREES_TO_RADIANS(yRotation)) * Math.sin(DEGREES_TO_RADIANS(xRotation + 180)));
		float y_circ = (float) (  										  Math.cos(DEGREES_TO_RADIANS(xRotation + 180)));
		float z_circ = (float) (Math.sin(DEGREES_TO_RADIANS(yRotation)) * Math.sin(DEGREES_TO_RADIANS(xRotation + 180)));
		return new Vector3D(x_circ, y_circ, z_circ);
	}
	
	/**
	 * Converts the angle from degrees to radians
	 * 
	 * @param degrees
	 *            The angle in degrees that will be converted
	 * @return
	 */
	private static float DEGREES_TO_RADIANS(float degrees) {
		return (float) ((degrees) * (Math.PI / 180.0));
	}

	@Override
	public float getHeight() {
		return 10;
	}

	@Override
	public String getType() {
		return "Player";
	}

}
