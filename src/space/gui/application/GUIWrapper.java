package space.gui.application;

import de.matthiasmann.twl.Event;

public class GUIWrapper extends NestedWidget{
	
	public GUIWrapper(GameApplication gameApplication){
		super(gameApplication);
	}
	
    @Override
    protected void layout() {

    }
    
    @Override
    protected boolean handleEvent(Event evt) {
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
    	return evt.isMouseEventNoWheel();
    }
}
