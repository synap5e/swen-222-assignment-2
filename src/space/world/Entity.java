package space.world;

import space.gui.pipeline.viewable.ViewableObject;
import space.math.Vector2D;

public abstract class Entity implements ViewableObject{
	private Vector2D position;
	private int id;
	private float elevation;
	private String description;

	public Entity(Vector2D pos, int i,String d){
		position = pos;
		id = i;
		description = d;
	}
	public Vector2D getPosition(){
		return position;
	}

	public int getID(){
		return id;
	}

	public float getElevation(){
		return elevation;
	}

	public String getDescription() {
		return description;
	}
	public void setPosition(Vector2D pos){
		position = pos;
	}

	public abstract boolean canClip();
	public abstract void update(int delta);
	public abstract float getCollisionRadius();
}
