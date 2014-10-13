package space.gui.application.widget.label;

import space.world.Entity;
import de.matthiasmann.twl.Label;

/**
 * 
 * 
 * @author Matt Graham
 */

public class ItemLabel extends Label{

	private Entity entity;
	private Label description;

	private boolean isSelected;

	/**
	 * Creates a new label for an item.
	 * 
	 * @param entity the entity which is being viewed
	 * @param description the label of the description
	 */
	public ItemLabel(Entity entity, Label description){
		super();

		this.entity = entity;
		this.description = description;

		setText(entity.getName());
		setTheme("item");
	}

	/**
	 * Gets the label of the description.
	 * 
	 * @return
	 */
	public Label getDescription() {
		return description;
	}

	/**
	 * Gets the entity backing the item.
	 * 
	 * @return
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * Sets if the item has been selected in the view.
	 * 
	 * @param flag
	 */
	public void setSelected(boolean flag){
		isSelected = flag;
		if(isSelected){
			setTheme("item-selected");
		} else {
			setTheme("item");
		}
	}
	
	/**
	 * Gets whether the item has been selected.
	 * 
	 * @return
	 */
	public boolean isSelected(){
		return isSelected;
	}
}
