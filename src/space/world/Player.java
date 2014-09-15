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
	private Vector3D lookDir; 
	private float eyeHeight;
	private int id;
	private int points;
	
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
	@Override
	public Vector2D getPosition() {
		return position;
	}
	
	public void setPosition(Vector2D position) {
		this.position = position;
	}
	
	@Override
	public Vector3D getLookDirection() {
		return lookDir;
	}
	
	public void setLookDirection(Vector3D lookDir) {
		this.lookDir = lookDir;
	}
	
	@Override
	public float getEyeHeight() {
		return eyeHeight;
	}
	
	public void setEyeHeight(float eyeHeight) {
		this.eyeHeight = eyeHeight;
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
	@Override
	public float getAngle() {
		// TODO do this properly
		return 0;
	}

	@Override
	public float getElevation() {
		// TODO do this properly
		return 0;
	}

	@Override
	public boolean canMove() {
		return true;
	}

	
}
