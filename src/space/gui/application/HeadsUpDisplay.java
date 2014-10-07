package space.gui.application;

import java.util.ArrayList;
import java.util.List;

import space.world.Player;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.RadialPopupMenu;
import de.matthiasmann.twl.ToggleButton;

public class HeadsUpDisplay extends NestedWidget {
	
	private static final int PADDING = 10;
	private static final int SPACING = 5;
	
    private static final String[] BUTTON_ITEM_THEMES = {
        "item-placeholder",
        "item-placeholder",
        "item-placeholder",
        "item-placeholder",
        "item-placeholder",
        "item-placeholder",
        "item-placeholder",
        "item-placeholder"
    };
    
	private List<ToggleButton> actionButtons;
    
    private FPSCounter fpsCounter;
    private Label labelExample;
	
	public HeadsUpDisplay(GameApplication gameApplication){
		super(gameApplication);
		
        this.actionButtons = new ArrayList<ToggleButton>();
        
        for(int i=0 ; i<BUTTON_ITEM_THEMES.length ; i++) {
        	ToggleButton actionButton = new ToggleButton();
            actionButton.setTheme(BUTTON_ITEM_THEMES[i]);
            actionButtons.add(actionButton);
            add(actionButton);
        }

        fpsCounter = new FPSCounter();
        add(fpsCounter);

        labelExample = new Label();
        labelExample.setText("Lorem Ipsum, Lorem Ipsum, Lorem Ipsum.");
        add(labelExample);
    }

    @Override
    protected void layout() {
        layoutItems();

        layoutFPS();
        
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
        
        for(ToggleButton button : actionButtons) {
            button.setPosition(x, y);
            button.adjustSize();
            y += button.getHeight() + SPACING;
        }
    }
    
    private void layoutFPS(){
        fpsCounter.adjustSize();
        fpsCounter.setPosition(getInnerWidth() - fpsCounter.getWidth(), 0);
    }
    
    private void layoutMisc(){
        labelExample.adjustSize();
        labelExample.setPosition(getInnerWidth() / 2 - labelExample.getWidth() / 2, getInnerBottom() - labelExample.getHeight());
    }

	RadialPopupMenu createRadialMenu() {
        RadialPopupMenu rpm = new RadialPopupMenu(this);
        for(int i=0 ; i<10 ; i++) {
            final int idx = i;
            rpm.addButton("star", new Runnable() {
                public void run() {
                    labelExample.setText("Selected " + idx);
                }
            });
        }
        return rpm;
    }

	public void update(Player localPlayer) {
		// TODO Auto-generated method stub
		
	}
}
