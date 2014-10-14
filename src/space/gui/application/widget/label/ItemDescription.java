package space.gui.application.widget.label;

import de.matthiasmann.twl.Label;

/**
 * An item's description label.
 *
 * @author Matt Graham 300211545
 */

public class ItemDescription extends Label{
	public ItemDescription(String description){
		super();

		String text = description.substring(0, Math.min(description.length(), 26));

		if(description.length() > 26){
			text += "...";
		}

		setText(text);
		setTheme("description");
	}

}
