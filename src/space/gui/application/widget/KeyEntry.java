package space.gui.application.widget;

import space.gui.application.GameApplication;
import space.gui.application.KeyBinding;
import de.matthiasmann.twl.Label;

public class KeyEntry extends Label {
	
	
	GameApplication gameApplication;
	KeyBinding keyBinding;
	
	String action;
	int key;
	
	public KeyEntry(GameApplication gameApplication, String action, int key){
		super();
		
		setTheme("entry");
		
		this.gameApplication = gameApplication;
		this.keyBinding = gameApplication.getKeyBinding();
		this.action = action;
		this.key = key;
		
		updateText();
	}
	
	public void setKey(int key){
		this.key = key;
		keyBinding.changeBinding(key, action);
		
		updateText();
	}
	
	public void updateText(){
		setText(keyBinding.getKeyName(key));
	}
	
	@Override
	protected void handleClick(boolean doubleClick){
		gameApplication.captureKey(this);
	}
}
