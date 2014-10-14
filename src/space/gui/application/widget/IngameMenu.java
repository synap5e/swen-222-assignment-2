package space.gui.application.widget;

import java.util.ArrayList;
import java.util.List;

import space.gui.application.GameApplication;
import space.gui.application.GameDisplay;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

/**
 * Displays the in-game pause menu.
 * 
 * @author Matt Graham 300211545
 */

public class IngameMenu extends NestedWidget{

	private static final int PADDING_LEFT = 150;
	private static final int PADDING_TOP = 180;
	private static final int SPACING = 15;

	private List<Label> menuItems;
	private Label title;
	
	private Label playerId;
	
	private ControlsWidget controlsWidget;

	public IngameMenu(final GameApplication gameApplication, final GameDisplay gameDisplay) {
		super(gameDisplay);

		controlsWidget = null;
		
		setVisible(false);

		this.menuItems = new ArrayList<Label>();

		title = new Label("Paused");
		title.setTheme("title");
		add(title);
		
		playerId = new Label(gameApplication.getClient().getLocalPlayer().getName());
		playerId.setTheme("title");
		add(playerId);

		Label menuItem = new Label("Resume"){
			@Override
			protected void handleClick(boolean doubleClick){
				gameDisplay.setMenuVisible(false);
			}
		};
		menuItems.add(menuItem);

		menuItem = new Label("Controls"){
			@Override
			protected void handleClick(boolean doubleClick){
				controlsWidget.setVisible(!controlsWidget.isVisible());
			}
		};
		menuItems.add(menuItem);

		menuItem = new Label("Disconnect"){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setGameState(GameApplication.MAINMENU);
			}
		};
		menuItems.add(menuItem);

		menuItem = new Label("Exit"){
			@Override
			protected void handleClick(boolean doubleClick){
				gameDisplay.stop();
			}
		};
		menuItems.add(menuItem);

		for(Label item : menuItems) {
			item.setTheme("menuitem");
	        add(item);
	    }
		
		
		controlsWidget = new ControlsWidget(gameDisplay);
		add(controlsWidget);
		
		controlsWidget.updatePositions(400, 200);
	}

	@Override
	protected void layout() {
	    int x = PADDING_LEFT;
	    int y = PADDING_TOP;
	    
	    playerId.setPosition(x, y);
	    playerId.adjustSize();
	    
	    y += playerId.getHeight() + SPACING;

	    title.setPosition(x, y);
	    title.adjustSize();

	    x += 20;
	    y += title.getHeight() + SPACING;

	    for(Label item : menuItems) {
	        item.setPosition(x, y);
	        item.adjustSize();
	        y += item.getHeight() + SPACING;
	    }
	    
	}

	@Override
	protected boolean handleEvent(Event evt) {
		if(isVisible()){
			return evt.isMouseEventNoWheel();
		}
		return super.handleEvent(evt);
	}
	
	@Override
	public void setVisible(boolean flag){
		super.setVisible(flag);
		if(!flag && controlsWidget != null){
			controlsWidget.setVisible(false);
		}
	}
}
