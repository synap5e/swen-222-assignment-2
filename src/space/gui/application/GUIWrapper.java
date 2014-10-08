package space.gui.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.matthiasmann.twl.Event;

public class GUIWrapper extends NestedWidget{
	private final static Map<Integer, String> DEFAULT_MAP;
	static {
        Map<Integer, String> temp = new HashMap<Integer, String>();
        temp.put(Event.KEY_ESCAPE, "menu");
        temp.put(Event.KEY_E, "interact");
        temp.put(Event.KEY_R, "rifle");
        DEFAULT_MAP = Collections.unmodifiableMap(temp);
    }

	private Map<Integer, String> controlMap;

	public GUIWrapper(GameApplication gameApplication){
		super(gameApplication);

		controlMap = new HashMap<Integer, String>(DEFAULT_MAP);

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
    		int keyCode = evt.getKeyCode();
    		if(controlMap.containsKey(keyCode)){
    			fireAction(controlMap.get(keyCode));
    			return returnFlag;
    		} else {
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

    private void fireAction(String action){
	    switch (action) {
	        case "menu":
	     	   gameApplication.setMenuVisible(!gameApplication.isMenuVisible());
	           break;
	        default:
	     	   break;
	    }
    }
}
