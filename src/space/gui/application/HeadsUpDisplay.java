package space.gui.application;

import java.util.ArrayList;
import java.util.List;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.RadialPopupMenu;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;

public class HeadsUpDisplay extends Widget {
	
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
	private List<ToggleButton> menuButtons;
    private ToggleButton buttonPause;
    private ToggleButton buttonMenu;
    
    private FPSCounter fpsCounter;
    private Label labelExample;

	public boolean quit;
	
	
	public HeadsUpDisplay(){
		this.quit = false;
		
        this.actionButtons = new ArrayList<ToggleButton>();
        this.menuButtons = new ArrayList<ToggleButton>();
        
        for(int i=0 ; i<BUTTON_ITEM_THEMES.length ; i++) {
        	ToggleButton actionButton = new ToggleButton();
            actionButton.setTheme(BUTTON_ITEM_THEMES[i]);
            actionButtons.add(actionButton);
            add(actionButton);
        }

        buttonPause = new ToggleButton();
        buttonPause.setTheme("pause");
        menuButtons.add(buttonPause);
        add(buttonPause);
        

        buttonMenu = new ToggleButton();
        buttonMenu.setTheme("menu");
        menuButtons.add(buttonMenu);
        add(buttonMenu);

        fpsCounter = new FPSCounter();
        add(fpsCounter);

        labelExample = new Label();
        labelExample.setText("Lorem Ipsum, Lorem Ipsum, Lorem Ipsum.");
        add(labelExample);
    }

    @Override
    protected void layout() {
        layoutItems();
        
        layoutMenu();

        layoutFPS();
        
        layoutMisc();
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
    
    private void layoutMenu(){
        int x = getInnerWidth() - PADDING - 80;
        int y = PADDING;

        for(ToggleButton button : menuButtons) {
            button.setPosition(x, y);
            button.adjustSize();
            x -= button.getWidth() + SPACING;
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

    @Override
    protected boolean handleEvent(Event evt) {
        if(super.handleEvent(evt)) {
            return true;
        }
        if (evt.getType() == Event.Type.KEY_PRESSED) {
		    switch (evt.getKeyCode()) {
	           case Event.KEY_ESCAPE:
	               quit = true;
	               return true;
	           default:
	        	   break;
		    }
        } else if (evt.getType() == Event.Type.MOUSE_BTNDOWN) {
        	switch(evt.getMouseButton()){
            	case Event.MOUSE_RBUTTON:
                   return createRadialMenu().openPopup(evt);
	           default:
	        	   break;
        	}
            
        }
        return evt.isMouseEventNoWheel();
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
}
