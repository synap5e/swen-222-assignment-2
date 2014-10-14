package space.gui.application.widget.wrapper;

import space.gui.application.GameApplication;
import space.gui.application.GameDisplay;
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
	private GameApplication gameApplication;

	public GameController(GameApplication gameApplication, GameDisplay gameDisplay){
		super(gameDisplay);
		
		this.gameApplication = gameApplication;

		this.keyBinding = gameDisplay.getKeyBinding();
	}

    @Override
    protected boolean handleEvent(Event evt) {
    	boolean returnFlag = evt.isMouseEventNoWheel();

        if(super.handleEvent(evt)) {
            return true;
        }

    	if (evt.getType() == Event.Type.KEY_PRESSED) {
    		
    		int keyCode = evt.getKeyCode();
            
            if(gameDisplay.isInventoryExchangeVisible()){
            	if(keyCode == Event.KEY_ESCAPE){
    	        	gameDisplay.setInventoryExchangeVisible(false);
            	}
            	return true;
            }
            
            if(gameDisplay.isInventoryVisible()){
            	if(keyCode == Event.KEY_ESCAPE){
    	        	gameDisplay.setInventoryVisible(false);
            	}
            	return true;
            }
            
            if(gameDisplay.isMenuVisible()){
            	if(keyCode == Event.KEY_ESCAPE){
    	        	gameDisplay.setMenuVisible(false);
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
	            gameDisplay.setMenuVisible(true);
	        	break;
	        case KeyBinding.ACTION_INTERACT:
	        	gameApplication.getClient().interactWith(gameApplication.getClient().getViewedEntity());
	        	break;

	        case KeyBinding.ACTION_RIFLE:
	        	gameApplication.getClient().rifleContainer(gameApplication.getClient().getViewedEntity());
	        	break;

	        case KeyBinding.ACTION_INVENTORY:
	        	gameDisplay.showInventory(gameApplication.getClient().getLocalPlayer().getInventory());
	        	break;

	        case KeyBinding.ACTION_TORCH:
	        	gameApplication.getClient().getLocalPlayer().setTorch(!gameApplication.getClient().getLocalPlayer().isTorchOn());
	        	break;

	        default:
	     	   break;
	    }
    }
}
