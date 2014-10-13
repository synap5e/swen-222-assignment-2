package space.gui.application.widget;

import java.util.List;

import space.gui.application.GameApplication;
import space.gui.application.KeyBinding;
import space.world.Entity;
import space.world.Pickup;
import de.matthiasmann.twl.Event;

public class GameController extends GUIWrapper {

	KeyBinding keyBinding;

	public GameController(GameApplication gameApplication){
		super(gameApplication);

		this.keyBinding = gameApplication.getKeyBinding();
	}

    @Override
    protected void layout() {
    	super.layout();
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
