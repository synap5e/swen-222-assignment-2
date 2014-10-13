package space.gui.application.widget.label;

import space.world.Entity;
import de.matthiasmann.twl.Label;

public class ItemLabel extends Label{

	private Entity entity;
	private Label description;

	private boolean isSelected;

	public ItemLabel(Entity entity, Label description){
		super();

		this.entity = entity;
		this.description = description;

		setText(entity.getName());
		setTheme("item");
	}

	public Label getDescription() {
		return description;
	}

	public Entity getEntity() {
		return entity;
	}
	
	public void setSelected(boolean flag){
		isSelected = flag;
		if(isSelected){
			setTheme("item-selected");
		} else {
			setTheme("item");
		}
	}
	
	public boolean isSelected(){
		return isSelected;
	}
}
