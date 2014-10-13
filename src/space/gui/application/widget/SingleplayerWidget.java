package space.gui.application.widget;

import space.gui.application.GameApplication;
import space.network.Server;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

/**
 * The panel displayed to show the options of single-player mode.
 * 
 * @author Matt Graham
 */

public class SingleplayerWidget extends NestedWidget {
	private final static int SPACING = 10;

	private Label saveLabel;
	private EditField saveField;

	private Label submitButton;

	public SingleplayerWidget(final GameApplication gameApplication) {
		super(gameApplication);
		
		setVisible(false);
		
		saveLabel = new Label();
		saveLabel.setText("Save File:");
		saveLabel.setTheme("label");
		add(saveLabel);

		saveField = new EditField();
		saveField.setEnabled(true);
		saveField.setTheme("editfield");
		saveField.setMultiLine(false);
		saveField.setText(Server.DEFAULT_SAVE);
		add(saveField);
		
		submitButton = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setupSingleplayer(saveField.getText());
				gameApplication.setGameState(GameApplication.SINGLEPLAYER);
			}
		};
		submitButton.setText("Launch");
		submitButton.setTheme("submit");
		add(submitButton);
	}

    @Override
    protected void layout() {
    	int x = startX;
    	int y = startY;
    	
    	saveLabel.adjustSize();
    	
    	x += Math.max(saveLabel.getWidth(), saveLabel.getWidth());

    	saveLabel.setPosition(x - saveLabel.getWidth(), y);
    	
    	saveField.setPosition(x + SPACING, y - 2);
    	saveField.setSize(200, 20);
    	
    	y += saveLabel.getHeight() + SPACING * 2;
    	x += SPACING;
    	
    	submitButton.adjustSize();
    	submitButton.setPosition(x, y);

    }
    
    @Override
    public void setVisible(boolean flag) {
    	super.setVisible(flag);
    	if(flag){
    		saveField.requestKeyboardFocus();
    	}
    }
    
    @Override
	protected boolean handleEvent(Event evt) {
    	if(super.handleEvent(evt)){
    		return true;
    	}
		return false;
	}
}
