package space.gui.application.widget;

import space.gui.application.GameDisplay;
import space.gui.application.KeyBinding;
import space.gui.pipeline.viewable.ViewableOpenable;
import space.network.Client;
import space.world.Entity;
import space.world.Lockable;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.Label;

/**
 * The panel which displays live information for the user.
 * 
 * @author Matt Graham 300211545
 */

public class HeadsUpDisplay extends NestedWidget {
	
	private static final int SPACING = 5;
	private static final int OFFSET = 40;
    
    private FPSCounter fpsCounter;
    private Label roomLabel;
    
    private Label viewDescription;
    
    private Label viewPrompt;
	
	public HeadsUpDisplay(GameDisplay gameDisplay){
		super(gameDisplay);

        fpsCounter = new FPSCounter();
        add(fpsCounter);

        roomLabel = new Label();
        roomLabel.setTheme("infolabel");
        add(roomLabel);
        
        viewDescription = new Label();
        viewDescription.setTheme("infolabel");
        viewDescription.setVisible(false);
        add(viewDescription);
        
        viewPrompt = new Label();
        viewPrompt.setTheme("infolabel");
        viewPrompt.setVisible(false);
        add(viewPrompt);
    }

    @Override
    protected void layout() {
        fpsCounter.adjustSize();
        fpsCounter.setPosition(getWidth() - fpsCounter.getWidth(), 0);


        roomLabel.adjustSize();
        roomLabel.setPosition((getWidth() - roomLabel.getWidth()) / 2, getHeight() - roomLabel.getHeight() - 20);
        
        
        int y = getHeight() / 2 + OFFSET;
        viewDescription.adjustSize();
        viewDescription.setPosition((getWidth() - viewDescription.getWidth()) / 2, y);
        
        y += viewDescription.getHeight() + SPACING;
        viewPrompt.adjustSize();
        viewPrompt.setPosition((getWidth() - viewPrompt.getWidth()) / 2, y);
    }

	/**
	 * Updates the various components of the Heads Up Display.
	 * 
	 * @param client
	 */
	public void update(Client client) {
		if(roomLabel.getText() != client.getLocalPlayer().getRoom().getDescription()){
			roomLabel.setText(client.getLocalPlayer().getRoom().getDescription());
		}
		
		Entity entity = client.getViewedEntity();
		if(entity == null && (viewDescription.isVisible() || viewPrompt.isVisible())){
			viewDescription.setText("");
			viewDescription.setVisible(false);
			
			viewPrompt.setText("");
			viewPrompt.setVisible(false);
			
		} else if(entity != null){
			if(entity.getDescription() != viewDescription.getText()){
				viewDescription.setText(entity.getDescription());
				viewDescription.setVisible(true);
			}
			
			if(entity.canInteract()){
				String message = gameDisplay.getKeyBinding().getKeyName(KeyBinding.ACTION_INTERACT) + " - Interact";
				
				if(entity instanceof Lockable){
					if(((Lockable) entity).isLocked()){
						message += " (Locked)";
					}
				}
				
				if(entity instanceof ViewableOpenable){
					if(((ViewableOpenable) entity).isOpen()){
						message += "\n" + gameDisplay.getKeyBinding().getKeyName(KeyBinding.ACTION_RIFLE) + " - Rifle Contents";
					}
				}
				
				if(viewPrompt.getText() != message){
					viewPrompt.setText(message);
					viewPrompt.setVisible(true);
				}
			}
		}
	}
	
	
}
