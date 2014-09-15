package space.world;

import java.util.HashSet;
import java.util.Set;

import space.gui.pipeline.viewable.ViewablePlayer;
import space.math.Vector2D;
import space.math.Vector3D;
import space.world.items.Item;

public class Player implements Entity,ViewablePlayer{
	private Set<Item> inventory = new HashSet<Item>(); //items the player is carrying
	private Vector2D position;
	private int id;
	private int points;
	private static final float EYE_HEIGHT = 6;
	private static final float JUMP_HEIGHT = 2;

	private float jumpTime = 0;

	private float xRotation = 90;
	private float yRotation = 100;

	public Vector2D getPosition() {
		return position;
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

	public Player(Vector2D pos,int i){
		position = pos;
		id = i;
	}
	
	public void carryItem(Item i){
		inventory.add(i);
	}

	public void dropItem(Item i){
		inventory.remove(i);
	}
	
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	public int getID() {
		return id;
	}
	
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}

	public Set<Item> getInventory(){
		return inventory;
	}
	
}
