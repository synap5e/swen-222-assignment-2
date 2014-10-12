package space.gui.application.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import space.gui.application.GameApplication;
import space.gui.application.KeyBinding;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

public class ControlsWidget extends NestedWidget{
	
	public final static int COLUMN = 200;
	public final static int SPACING = 10;
	
	List<Label> actions;
	List<KeyEntry> keys;
	
	KeyBinding keyBinding;

	public ControlsWidget(GameApplication gameApplication) {
		super(gameApplication);
		
		setVisible(false);
		
		this.keyBinding = gameApplication.getKeyBinding();
		
		this.actions = new ArrayList<Label>();
		this.keys = new ArrayList<KeyEntry>();
		
		for(Map.Entry<String, Integer> binding : keyBinding.getActionSet()){
			Label label = new Label();
			label.setText(binding.getKey());
			label.setTheme("label");
			actions.add(label);
			add(label);
			
			KeyEntry entry = new KeyEntry(gameApplication, binding.getKey(), binding.getValue());
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
