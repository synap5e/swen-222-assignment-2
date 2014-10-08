package space.gui.application;

import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

public class MultiplayerWidget extends NestedWidget {
	private final static int SPACING = 10;

	Label serverLabel;
	EditField serverField;

	Label idLabel;
	EditField idField;

	Label submitButton;

	public MultiplayerWidget(GameApplication gameApplication) {
		super(gameApplication);

		serverLabel = new Label();
		serverLabel.setText("Server Hostname:");
		serverLabel.setTheme("label");
		add(serverLabel);

		serverField = new EditField();
		add(serverField);

		idLabel = new Label();
		idLabel.setText("ID (Optional):");
		idLabel.setTheme("label");
		add(idLabel);

		idField = new EditField();
		add(idField);

		submitButton = new Label();
		submitButton.setText("Launch");
		submitButton.setTheme("submit");
		add(submitButton);
	}

    @Override
    protected void layout() {
    	int x = 0;
    	int y = 0;

    	serverLabel.setPosition(x, y);
    	serverLabel.adjustSize();

    	idLabel.setPosition(x, serverLabel.getHeight() + SPACING);
    	idLabel.adjustSize();

    	x = Math.max(serverLabel.getWidth(), idLabel.getWidth());

    	serverField.setPosition(x, y);
    	serverField.adjustSize();

    	idField.setPosition(x, y);
    	idField.adjustSize();



    }

	@Override
	protected boolean handleEvent(Event evt) {
		return false;
	}
}
