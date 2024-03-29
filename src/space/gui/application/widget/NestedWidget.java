package space.gui.application.widget;

import space.gui.application.GameDisplay;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Widget;

/**
 * Widgets which are part of the widget tree displayed from the Game Application should extend NestedWidget.
 * A NestedWodget will expand to the size of the Game Application and provides additional methods of layout purposes.
 * 
 * @author Matt Graham 300211545
 */
public class NestedWidget extends Widget {
	
	protected GameDisplay gameDisplay;
	
	protected int startX;
	protected int startY;
	
	public NestedWidget(GameDisplay gameDisplay){
		this.gameDisplay = gameDisplay;
		setSize(gameDisplay.getWidth(), gameDisplay.getHeight());
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
