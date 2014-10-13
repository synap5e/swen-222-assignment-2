package space.gui.application.widget;

import java.util.ArrayList;
import java.util.List;

import space.gui.application.GameApplication;
import space.gui.application.KeyBinding;
import space.gui.pipeline.viewable.OpenableContainer;
import space.network.Client;
import space.world.Entity;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.Label;

public class HeadsUpDisplay extends NestedWidget {
	
	private static final int PADDING = 10;
	private static final int SPACING = 5;
	
	private static final int OFFSET = 40;
    
	private List<Label> items;
    
    private FPSCounter fpsCounter;
    private Label roomLabel;
    
    private Label viewDescription;
    
    private Label viewPrompt;
	
	public HeadsUpDisplay(GameApplication gameApplication){
		super(gameApplication);
		
        this.items = new ArrayList<Label>();

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
        layoutFPS();
    	
        layoutItems();
        
        layoutMisc();
    }
    
    private void layoutItems(){
        int x = PADDING;
        int y = 40;
        
        for(Label item : items) {
        	item.setPosition(x, y);
        	item.adjustSize();
            y += item.getHeight() + SPACING;
        }
    }
    
    private void layoutFPS(){
        fpsCounter.adjustSize();
        fpsCounter.setPosition(getWidth() - fpsCounter.getWidth(), 0);
    }
    
    private void layoutMisc(){
    	int y;
    	
        roomLabel.adjustSize();
        roomLabel.setPosition((getWidth() - roomLabel.getWidth()) / 2, getHeight() - roomLabel.getHeight() - 20);
        
        
        y = getHeight() / 2 + OFFSET;
        viewDescription.adjustSize();
        viewDescription.setPosition((getWidth() - viewDescription.getWidth()) / 2, y);
        
        y += viewDescription.getHeight() + SPACING;
        viewPrompt.adjustSize();
        viewPrompt.setPosition((getWidth() - viewPrompt.getWidth()) / 2, y);
    }

	public void update(Client client) {
		updateLabels(client);
		
		updateInventory(client);

	}

	private void updateInventory(Client client) {
		// TODO Auto-generated method stub
		
	}
	
	private void updateLabels(Client client) {
		if(roomLabel.getText() != client.getLocalPlayer().getRoom().getDescription()){
			roomLabel.setText(client.getLocalPlayer().getRoom().getDescription());
		}
		
		Entity entity = client.getViewedEntity();
		if(entity == null && (viewDescription.isVisible() || viewPrompt.isVisible())){
			viewDescription.setText("");
			viewDescription.setVisible(false);
			
			viewPrompt.setText("");
			viewPrompt.setVisible(false);
			
		} else if(entity != null && entity.getDescription() != viewDescription.getText()){
			viewDescription.setText(entity.getDescription());
			viewDescription.setVisible(true);
			
			if(entity.canInteract()){
				String message = gameApplication.getKeyBinding().getKeyName(KeyBinding.ACTION_INTERACT) + " - Interact";
				
				if(entity instanceof OpenableContainer){
					message += "\n" + gameApplication.getKeyBinding().getKeyName(KeyBinding.ACTION_RIFLE) + " - Rifle Contents";
				}
				viewPrompt.setText(message);
				viewPrompt.setVisible(true);
			}
		}
	}
	
	
}
