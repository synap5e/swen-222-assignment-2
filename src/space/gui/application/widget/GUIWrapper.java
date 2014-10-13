package space.gui.application.widget;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;
import space.gui.application.GameApplication;

public class GUIWrapper extends NestedWidget {

	Label captureLabel;

	boolean captureFlag;

	KeyEntry keyEntry;

	public GUIWrapper(GameApplication gameApplication) {
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

	public void captureKey(KeyEntry keyEntry){
		this.keyEntry = keyEntry;

		captureLabel.setVisible(true);
		captureFlag = true;
	}

	public boolean isCapture(){
		return captureFlag;
	}

	public void setKey(int keyCode){
		keyEntry.setKey(keyCode);
	}

	public void reset(){
		captureFlag = false;
		captureLabel.setVisible(false);
	}
}
