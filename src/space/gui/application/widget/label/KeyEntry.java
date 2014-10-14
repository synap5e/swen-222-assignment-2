package space.gui.application.widget.label;

import space.gui.application.GameDisplay;
import space.gui.application.KeyBinding;
import de.matthiasmann.twl.Label;

/**
 * The controller/view of a key binding.
 * 
 * @author Matt Graham 300211545
 */

public class KeyEntry extends Label {

	private GameDisplay gameApplication;
	private KeyBinding keyBinding;

	private String action;
	private int key;

	/**
	 * @param gameDisplay
	 * @param action the action of the binding
	 * @param key the initial key of the binding
	 */
	public KeyEntry(GameDisplay gameDisplay, String action, int key){
		super();

		setTheme("entry");

		this.gameApplication = gameDisplay;
		this.keyBinding = gameDisplay.getKeyBinding();
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
