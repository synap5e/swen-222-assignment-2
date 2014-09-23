package space.gui.application;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Widget;

public class IngameController extends NestedWidget{
	public IngameController(GameApplication gameApplication){
		super(gameApplication);
	}
	
    @Override
    protected boolean handleEvent(Event evt) {
        if(super.handleEvent(evt)) {
            return true;
        }
        if (evt.getType() == Event.Type.KEY_PRESSED) {
		    switch (evt.getKeyCode()) {
	           case Event.KEY_ESCAPE:
	        	   gameApplication.captureMouse(false);
	               return true;
	           default:
	        	   break;
		    }
        } else if (evt.getType() == Event.Type.MOUSE_BTNDOWN) {
        	switch(evt.getMouseButton()){
            	case Event.MOUSE_RBUTTON:
            		gameApplication.openPopup(evt);
            		return true;
            	case Event.MOUSE_LBUTTON:
            		gameApplication.captureMouse(true);
            		return true;
            	default:
	        	   break;
        	}
            
        }
        //return evt.isMouseEventNoWheel();
        return false;
    }
}
