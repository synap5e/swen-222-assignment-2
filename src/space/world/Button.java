package space.world;

import space.math.Vector2D;

/**Represents a button which lets Players interact with otherwise un-interactable entities.
 * This means a button could open a door or activate the teleporter*/
public class Button extends Stationary {
	private Entity entity; //The entity which will be interacted with
	
	/**Constructs a new Button
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name
	 * @param entity The entity that will be interacted with if button is pressed*/
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

	/**@return the entity which will be interacted with the push of the button*/
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
