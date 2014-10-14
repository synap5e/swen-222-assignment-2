package space.gui.application.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import space.gui.application.GameDisplay;
import space.gui.application.KeyBinding;
import space.gui.application.widget.label.KeyEntry;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

/**
 * The panel which allows for a user to re-bind keys.
 * 
 * @author Matt Graham 300211545
 */

public class ControlsWidget extends NestedWidget{
	
	private final static int COLUMN = 200;
	private final static int SPACING = 10;
	
	private List<Label> actions;
	private List<KeyEntry> keys;
	
	private KeyBinding keyBinding;

	public ControlsWidget(GameDisplay gameDisplay) {
		super(gameDisplay);
		
		setVisible(false);
		
		this.keyBinding = gameDisplay.getKeyBinding();
		
		this.actions = new ArrayList<Label>();
		this.keys = new ArrayList<KeyEntry>();
		
		for(Map.Entry<String, Integer> binding : keyBinding.getActionSet()){
			Label label = new Label();
			label.setText(binding.getKey());
			label.setTheme("label");
			actions.add(label);
			add(label);
			
			KeyEntry entry = new KeyEntry(gameDisplay, binding.getKey(), binding.getValue());
			keys.add(entry);
			add(entry);
		}
	}

    @Override
    protected void layout() {
    	int x = startX;
    	int y = startY;
    	
    	for(int i = 0; i != actions.size(); i++){
    		Label label = actions.get(i);
    		label.adjustSize();
    		label.setPosition(x, y);
    		
    		KeyEntry entry = keys.get(i);
    		entry.adjustSize();
    		entry.setPosition(x + COLUMN, y);
    		
    		y += label.getHeight() + SPACING;
    	}
    }
	
	@Override
	protected boolean handleEvent(Event evt) {
		return false;
	}
}
