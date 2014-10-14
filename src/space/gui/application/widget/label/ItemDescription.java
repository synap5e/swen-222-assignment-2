package space.gui.application.widget.label;

import de.matthiasmann.twl.Label;

/**
 * An item's description label.
 * 
 * @author Matt Graham 300211545
 */

public class ItemDescription extends Label{
	public ItemDescription(String text){
		super(text);
		setTheme("description");
	}

}
