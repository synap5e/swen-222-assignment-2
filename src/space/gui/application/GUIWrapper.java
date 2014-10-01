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
    	boolean returnFlag = evt.isMouseEventNoWheel();
    	
        if(super.handleEvent(evt)) {
            return returnFlag;
        }
        
    	if (evt.getType() == Event.Type.KEY_PRESSED) {
		    switch (evt.getKeyCode()) {
	           case Event.KEY_ESCAPE:
	        	   gameApplication.setMenuVisible(!gameApplication.isMenuVisible());
	               return returnFlag;
	           default:
	        	   return false;
		    }
        } else if (evt.getType() == Event.Type.MOUSE_BTNDOWN) {
        	switch(evt.getMouseButton()){
            	case Event.MOUSE_RBUTTON:
            		gameApplication.openPopup(evt);
            		return returnFlag;
            	//case Event.MOUSE_LBUTTON:
            	//	gameApplication.captureMouse(true);
            	//	return returnFlag;
            	default:
            		return false;
        	}
    	}
    	return false;
    }
}
