package space.gui.application.widget;

import space.gui.application.GameApplication;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

/**
 * Panel displaying the game's instructions.
 * 
 * @author Matt Graham
 */

public class InstructionsWidget extends NestedWidget {
	
	private final static int PADDING = 19;

	private final static String INSTRUCTIONS = "To infinity and beyond...\nGo to outerspace and battle a rogue AI for control of your spaceship.\nComplete all the puzzles to win.";

	private Label text;


	public InstructionsWidget(GameApplication gameApplication) {
		super(gameApplication);

		setVisible(false);

		text = new Label(INSTRUCTIONS);
		text.isMultilineText();
		text.setMaxSize(Math.min(200, gameApplication.getWidth() - startX), 500);
		text.setTheme("instructions");
		add(text);


	}

	@Override
	protected void layout() {
		int x = startX;
		int y = startY + PADDING;
		
		text.setPosition(x, y);
	}

	@Override
	protected boolean handleEvent(Event evt) {
		return false;
	}
}
