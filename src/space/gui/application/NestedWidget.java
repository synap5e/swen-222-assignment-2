package space.gui.application;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Widget;

public class NestedWidget extends Widget {
	
	GameApplication gameApplication;
	int startX;
	int startY;
	
	public NestedWidget(GameApplication gameApplication){
		this.gameApplication = gameApplication;
		setSize(gameApplication.getWidth(), gameApplication.getHeight());
	}
	
	@Override
	protected boolean handleEvent(Event evt) {
		return super.handleEvent(evt);
	}
	
	
    public void updatePositions(int x, int y){
    	startX = x;
    	startY = y;
    	layout();
    }
}
