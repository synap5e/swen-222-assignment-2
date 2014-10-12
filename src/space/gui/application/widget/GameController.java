package space.gui.application.widget;

import java.util.List;

import space.gui.application.GameApplication;
import space.gui.application.KeyBinding;
import space.world.Entity;
import space.world.Pickup;
import de.matthiasmann.twl.Event;

public class GameController extends NestedWidget{
	
	KeyBinding keyBinding;

	public GameController(GameApplication gameApplication){
		super(gameApplication);
		
		this.keyBinding = gameApplication.getKeyBinding();
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
	     	   	gameApplication.setMenuVisible(!gameApplication.isMenuVisible());
	        	break;
	        case KeyBinding.ACTION_INTERACT:
	        	gameApplication.getClient().interactWith(gameApplication.getClient().getViewedEntity());
	        	break;
	           
	        case KeyBinding.ACTION_RIFLE:
	        	break;
	        	
	        case KeyBinding.ACTION_DROP:
				List<Pickup> inv = gameApplication.getClient().getLocalPlayer().getInventory();
				if (inv.size() > 0){
					for (Pickup p : inv){
						gameApplication.getClient().drop((Entity) p);
						break;
					}
				}
	        	break;
	
	        case KeyBinding.ACTION_TORCH:
	        	gameApplication.getClient().getLocalPlayer().setTorch(!gameApplication.getClient().getLocalPlayer().isTorchOn());
	        	break;
	        	
	        default:
	     	   break;
	    }
    }
}
