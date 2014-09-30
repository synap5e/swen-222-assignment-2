package space.gui.application;

import de.matthiasmann.twl.Widget;

public class NestedWidget extends Widget {
	
	GameApplication gameApplication;
	
	public NestedWidget(GameApplication gameApplication){
		this.gameApplication = gameApplication;
		setSize(gameApplication.getWidth(), gameApplication.getHeight());
	}
}
