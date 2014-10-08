package space.gui.application;

import de.matthiasmann.twl.Event;

public class ControlsWidget extends NestedWidget{

	public ControlsWidget(GameApplication gameApplication) {
		super(gameApplication);
	}

	@Override
	protected boolean handleEvent(Event evt) {
		return false;
	}
}
