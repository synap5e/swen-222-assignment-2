package space.world;

import space.math.Vector2D;

public class Button extends Stationary {
	private Entity entity;
	public Button(Vector2D position, int id, float elevation,
			String description, String name, Entity entity) {
		super(position, id, elevation, description, name);
		this.entity = entity;
	}

	@Override
	public boolean canInteract(){
		return true;
	}
	
	@Override
	public boolean interact(Character c, World w){
		return entity.interact(c, w);
	}

	@Override
	public boolean canClip() {
		return true;
	}

	@Override
	public float getCollisionRadius() {
		return 0;
	}

	public Entity getEntity() {
		return entity;
	}

	@Override
	public float getHeight() {
		return 0;
	}

	@Override
	public String getType() {
		return "Button";
	}

}
