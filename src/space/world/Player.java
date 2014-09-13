package space.world;

import java.util.HashSet;
import java.util.Set;

import space.gui.pipeline.viewable.ViewablePlayer;
import space.util.Vec2;
import space.util.Vec3;

public class Player implements Entity,ViewablePlayer{
	private Set<Item> inventory = new HashSet<Item>(); //items the player is carrying
	private Vec2 position;
	private Vec3 lookDir; 
	private float eyeHeight;
	private int id;
	private int points;
	
	public Player(Vec2 pos,int i){
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
	public Vec2 getPosition() {
		return position;
	}
	
	public void setPosition(Vec2 position) {
		this.position = position;
	}
	
	@Override
	public Vec3 getLookDirection() {
		return lookDir;
	}
	
	public void setLookDirection(Vec3 lookDir) {
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

	
}
