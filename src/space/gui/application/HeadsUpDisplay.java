package space.gui.application;

import java.util.ArrayList;
import java.util.List;

import space.network.Client;
import space.world.Entity;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.RadialPopupMenu;

public class HeadsUpDisplay extends NestedWidget {
	
	private static final int PADDING = 10;
	private static final int SPACING = 5;
    
	private List<Label> items;
    
    private FPSCounter fpsCounter;
    private Label roomLabel;
    
    private Label viewDescription;
	
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
    }

    @Override
    protected void layout() {
        layoutFPS();
    	
        layoutItems();
        
        layoutMisc();
    }
    
    @Override
    protected boolean handleEvent(Event evt) {
        if(super.handleEvent(evt)) {
            return evt.isMouseEventNoWheel();
        }
        return false;
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
        roomLabel.adjustSize();
        roomLabel.setPosition((getWidth() - roomLabel.getWidth()) / 2, getHeight() - roomLabel.getHeight() - 20);
        
        viewDescription.adjustSize();
        viewDescription.setPosition((getWidth() - viewDescription.getWidth()) / 2, getHeight() / 2 + 40);
    }

	RadialPopupMenu createRadialMenu() {
        RadialPopupMenu rpm = new RadialPopupMenu(this);
        for(int i=0 ; i<10 ; i++) {
            final int idx = i;
            rpm.addButton("star", new Runnable() {
                public void run() {
                    roomLabel.setText("Selected " + idx);
                }
            });
        }
        return rpm;
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
		if(entity == null && viewDescription.isVisible()){
			viewDescription.setVisible(false);
			viewDescription.setText("");
		} else if(entity != null && entity.getDescription() != viewDescription.getText()){
			viewDescription.setVisible(true);
			viewDescription.setText(entity.getDescription());
		}
	}
	
	
}
