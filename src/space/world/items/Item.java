package space.world.items;

import space.gui.pipeline.viewable.ViewableObject;
import space.math.Vector2D;
import space.world.Entity;

public abstract class Item implements Entity,ViewableObject {
	private Vector2D position;
	private int id;
	private String description;
	
	public Item(Vector2D pos,int i,String d){
		position = pos;
		id = i;
		description = d;
	}
	
	public Vector2D getPosition() {
		return position;
	}
	
	public int getID() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setPosition(Vector2D position) {
		if(this instanceof Movable){
			this.position = position;
		}
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


}
