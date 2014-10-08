package space.gui.application;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

public class InstructionsWidget extends NestedWidget {

	public final static String INSTRUCTIONS = "To infinity and beyond...\nGo to outerspace and battle a rogue AI for control of your spaceship.\nComplete all the puzzles to win.";

	Label text;


	public InstructionsWidget(GameApplication gameApplication) {
		super(gameApplication);

		setVisible(false);

		text = new Label();
		text.isMultilineText();
		text.setMaxSize(200, 500);
		text.setText(INSTRUCTIONS);
		text.setTheme("instructions");
		add(text);


	}

	@Override
	protected void layout() {
		int x = startX;
		int y = startY;
		
		text.setPosition(x, y);
	}

	@Override
	protected boolean handleEvent(Event evt) {
		return false;
	}
}
