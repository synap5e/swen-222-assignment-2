package space.gui.application.widget;

import space.gui.application.GameApplication;
import space.network.Client;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

/**
 * The panel displayed to show the multi-player options.
 * 
 * @author Matt Graham 300211545
 */

public class MultiplayerWidget extends NestedWidget {
	private final static int SPACING = 10;

	private Label serverLabel;
	private EditField serverField;

	private Label idLabel;
	private EditField idField;

	private Label submitButton;

	public MultiplayerWidget(final GameApplication gameApplication) {
		super(gameApplication);
		
		setVisible(false);
		
		serverLabel = new Label();
		serverLabel.setText("Server Hostname:");
		serverLabel.setTheme("label");
		add(serverLabel);

		serverField = new EditField();
		serverField.setEnabled(true);
		serverField.setTheme("editfield");
		serverField.setMultiLine(false);
		serverField.setText(Client.DEFAULT_HOST);
		add(serverField);

		idLabel = new Label();
		idLabel.setText("ID (Optional):");
		idLabel.setTheme("label");
		add(idLabel);

		idField = new EditField();
		idField.setEnabled(true);
		idField.setTheme("editfield");
		idField.setMultiLine(false);
		add(idField);

		submitButton = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setupMultiplayer(serverField.getText(), idField.getText());
				gameApplication.setGameState(GameApplication.MULTIPLAYER);
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
    	
    	serverLabel.adjustSize();
    	idLabel.adjustSize();
    	
    	x += Math.max(serverLabel.getWidth(), idLabel.getWidth());

    	serverLabel.setPosition(x - serverLabel.getWidth(), y);
    	
    	serverField.setPosition(x + SPACING, y - 2);
    	serverField.setSize(200, 20);
    	
    	y += serverLabel.getHeight() + SPACING;
    	
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
    		serverField.requestKeyboardFocus();
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
