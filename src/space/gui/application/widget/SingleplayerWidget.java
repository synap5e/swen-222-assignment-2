package space.gui.application.widget;

import space.gui.application.GameApplication;
import space.gui.application.GameDisplay;
import space.network.Server;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

/**
 * The panel displayed to show the options of single-player mode.
 * 
 * @author Matt Graham 300211545
 */

public class SingleplayerWidget extends NestedWidget {
	private final static int SPACING = 10;

	private Label saveLabel;
	private EditField saveField;
	
	private Label idLabel;
	private EditField idField;

	private Label submitButton;

	public SingleplayerWidget(final GameApplication gameApplication, final GameDisplay gameDisplay) {
		super(gameDisplay);
		
		setVisible(false);
		
		saveLabel = new Label("Save File:");
		saveLabel.setTheme("label");
		add(saveLabel);

		saveField = new EditField();
		saveField.setEnabled(true);
		saveField.setTheme("editfield");
		saveField.setMultiLine(false);
		saveField.setText(Server.DEFAULT_SAVE);
		add(saveField);
		
		idLabel = new Label("ID (Optional):");
		idLabel.setTheme("label");
		add(idLabel);

		idField = new EditField();
		idField.setEnabled(true);
		idField.setTheme("editfield");
		idField.setMultiLine(false);
		add(idField);
		
		submitButton = new Label("Launch"){
			@Override
			protected void handleClick(boolean doubleClick){

				gameApplication.setupSingleplayer(saveField.getText());
				gameApplication.setupMultiplayer(idField.getText());
				
				gameApplication.setGameState(GameApplication.SINGLEPLAYER);
			}
		};
		submitButton.setTheme("submit");
		add(submitButton);
	}

    @Override
    protected void layout() {
    	int x = startX;
    	int y = startY;
    	
    	saveLabel.adjustSize();
    	idLabel.adjustSize();
    	
    	x += Math.max(saveLabel.getWidth(), idLabel.getWidth());

    	saveLabel.setPosition(x - saveLabel.getWidth(), y);
    	
    	saveField.setPosition(x + SPACING, y - 2);
    	saveField.setSize(200, 20);
    	
    	y += saveField.getHeight() + SPACING;
    	
    	idLabel.setPosition(x - idLabel.getWidth(), y);
    	
    	idField.setPosition(x + SPACING, y - 2);
    	idField.setSize(200, 20);
    	
    	y += idLabel.getHeight() + SPACING * 2;
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
