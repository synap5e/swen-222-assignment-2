package space.gui.application.widget;

import space.world.Entity;
import de.matthiasmann.twl.Label;

public class ItemWidget extends Label{

	InventoryWidget inventoryWidget;
	Entity entity;

	boolean isSelected;

	public ItemWidget(Entity entity, InventoryWidget inventoryWidget){
		super();

		this.entity = entity;
		this.inventoryWidget = inventoryWidget;

		setText(entity.getName());
		setTheme("item");
	}

	@Override
	protected void handleClick(boolean doubleClick){
		isSelected = !isSelected;

		if(isSelected){
			inventoryWidget.addSelected(entity);
		} else {
			inventoryWidget.removeSelected(entity);
		}
	}
}
