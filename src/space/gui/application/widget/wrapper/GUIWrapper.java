package space.gui.application.widget.wrapper;

import space.gui.application.GameDisplay;
import space.gui.application.widget.NestedWidget;
import space.gui.application.widget.label.KeyEntry;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

/**
 * GUIWrappers provide functionality for the user input to be intercepted,
 * which is used for creatng key bindings.
 * 
 * @author Matt Graham 300211545
 */

public class GUIWrapper extends NestedWidget {

	private Label captureLabel;

	private boolean captureFlag;

	private KeyEntry keyEntry;

	public GUIWrapper(GameDisplay gameApplication) {
		super(gameApplication);

		captureFlag = false;

		captureLabel = new Label();
		captureLabel.setText("Press key to bind.\nPress Esc to cancel.");
		captureLabel.setTheme("label");
		add(captureLabel);

		reset();
	}

	@Override
	protected void layout(){
		super.layout();

		captureLabel.adjustSize();
		captureLabel.setPosition((getWidth() - captureLabel.getWidth()) / 2, (getHeight() - captureLabel.getHeight()) / 2);
	}

	@Override
	protected boolean handleEvent(Event evt){
		if(isCapture() && evt.isKeyEvent()){
			if(evt.getKeyCode() != Event.KEY_ESCAPE){
				setKey(evt.getKeyCode());
			}
			reset();
			return true;
		}

		return super.handleEvent(evt);
	}

	/**
	 * @param keyEntry
	 */
	public void captureKey(KeyEntry keyEntry){
		this.keyEntry = keyEntry;

		captureLabel.setVisible(true);
		captureFlag = true;
	}

	/**
	 * Gets whether the key presses being captured.
	 * 
	 * @return
	 */
	public boolean isCapture(){
		return captureFlag;
	}

	/**
	 * Sets the key binder to the new key.
	 * 
	 * @param keyCode
	 */
	private void setKey(int keyCode){
		keyEntry.setKey(keyCode);
	}

	
	/**
	 * Releases the key interception.
	 */
	private void reset(){
		captureFlag = false;
		captureLabel.setVisible(false);
	}
}
