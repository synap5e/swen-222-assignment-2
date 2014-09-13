package space.world;

import space.util.Vec2;

public abstract class Item implements Entity {
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


}
