package space.world.items;

import space.gui.pipeline.viewable.ViewableObject;
import space.util.Vec2;
import space.world.Entity;

public abstract class Item implements Entity,ViewableObject {
	private Vec2 position;
	private int id;
	private String description;
	
	public Item(Vec2 pos,int i,String d){
		position = pos;
		id = i;
		description = d;
	}
	
	public Vec2 getPosition() {
		return position;
	}
	
	public int getID() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setPosition(Vec2 position) {
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
