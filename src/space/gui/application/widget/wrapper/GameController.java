package space.gui.application.widget.wrapper;

import space.gui.application.GameApplication;
import space.gui.application.KeyBinding;
import de.matthiasmann.twl.Event;

/**
 * The GameController is the root pane of the TWL GUI.
 * All user input (in-game) must go through this class.
 * 
 * @author Matt Graham 300211545
 */

public class GameController extends GUIWrapper {

	private KeyBinding keyBinding;

	public GameController(GameApplication gameApplication){
		super(gameApplication);

		this.keyBinding = gameApplication.getKeyBinding();
	}

    @Override
    protected boolean handleEvent(Event evt) {
    	boolean returnFlag = evt.isMouseEventNoWheel();

        if(super.handleEvent(evt)) {
            return true;
        }

    	if (evt.getType() == Event.Type.KEY_PRESSED) {
    		
    		int keyCode = evt.getKeyCode();
            
            if(gameApplication.isInventoryExchangeVisible()){
            	if(keyCode == Event.KEY_ESCAPE){
    	        	gameApplication.setInventoryExchangeVisible(false);
            	}
            	return true;
            }
            
            if(gameApplication.isInventoryVisible()){
            	if(keyCode == Event.KEY_ESCAPE){
    	        	gameApplication.setInventoryVisible(false);
            	}
            	return true;
            }
            
            if(gameApplication.isMenuVisible()){
            	if(keyCode == Event.KEY_ESCAPE){
    	        	gameApplication.setMenuVisible(false);
            	}
            	return true;
            }
    		
    		if(keyBinding.containsKey(keyCode)){
    			fireAction(keyBinding.getAction(keyCode));
    			return returnFlag;
    		} else {
    			return false;
    		}

        } else if (evt.getType() == Event.Type.MOUSE_BTNDOWN) {
        	switch(evt.getMouseButton()){
            	case Event.MOUSE_RBUTTON:
            		break;
            	default:
            		return false;
        	}
    	}
    	return false;
    }

    /**
     * Calls the functions mapped to the actions
     * 
     * @param action
     */
    private void fireAction(String action){
	    switch (action) {
	        case KeyBinding.ACTION_MENU:
	            gameApplication.setMenuVisible(true);
	        	break;
	        case KeyBinding.ACTION_INTERACT:
	        	gameApplication.getClient().interactWith(gameApplication.getClient().getViewedEntity());
	        	break;

	        case KeyBinding.ACTION_RIFLE:
	        	gameApplication.setInventoryExchangeVisible(true);
	        	break;

	        case KeyBinding.ACTION_INVENTORY:
	        	gameApplication.setInventoryVisible(true);
	        	break;

	        case KeyBinding.ACTION_TORCH:
	        	gameApplication.getClient().getLocalPlayer().setTorch(!gameApplication.getClient().getLocalPlayer().isTorchOn());
	        	break;

	        default:
	     	   break;
	    }
    }
}
