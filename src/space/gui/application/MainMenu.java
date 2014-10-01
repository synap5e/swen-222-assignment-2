package space.gui.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

public class MainMenu extends NestedWidget {
	private static final int SPACING = 5;

	private List<Label> menuItems;
	
	public MainMenu(final GameApplication gameApplication){
		super(gameApplication);
		
		this.menuItems = new ArrayList<Label>();
		
		Label menuItem = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setGameState(GameApplication.SINGLEPLAYER);
			}
		};
		menuItem.setText("Play Singleplayer");
		menuItems.add(menuItem);
		
		menuItem = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setGameState(GameApplication.MULTIPLAYER);
			}
		};
		menuItem.setText("Play Multiplayer");
		menuItems.add(menuItem);
		
		menuItem = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setGameState(GameApplication.SINGLEPLAYER);
			}
		};
		menuItem.setText("Instructions");
		menuItems.add(menuItem);
		
		menuItem = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setGameState(GameApplication.SINGLEPLAYER);
			}
		};
		menuItem.setText("Controls");
		menuItems.add(menuItem);
		
		menuItem = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setGameState(GameApplication.QUIT);
			}
		};
		menuItem.setText("Exit");
		menuItems.add(menuItem);
		
		
		for(Label item : menuItems) {
			item.setTheme("menuitem");
            add(item);
        }
	}
	
    @Override
    protected void layout() {
        int x = 80;
        int y = getHeight() - 50;
        
        Collections.reverse(menuItems);
        for(Label item : menuItems) {
            item.setPosition(x, y);
            item.adjustSize();
            y -= item.getHeight() + SPACING;
        }
    }
	
    @Override
    protected boolean handleEvent(Event evt) {
    	return false;
    }
}
