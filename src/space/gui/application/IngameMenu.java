package space.gui.application;

import java.util.ArrayList;
import java.util.List;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;


public class IngameMenu extends NestedWidget{

	private static final int PADDING_LEFT = 150;
	private static final int PADDING_TOP = 180;
	private static final int SPACING = 15;

	private List<Label> menuItems;
	Label title;

	public IngameMenu(final GameApplication gameApplication) {
		super(gameApplication);

		setVisible(false);

		this.menuItems = new ArrayList<Label>();

		title = new Label();
		title.setText("Paused");
		title.setTheme("title");
		add(title);

		Label menuItem = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setMenuVisible(false);
			}
		};
		menuItem.setText("Resume");
		menuItems.add(menuItem);

		menuItem = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setMenuVisible(false);
			}
		};
		menuItem.setText("Controls");
		menuItems.add(menuItem);

		menuItem = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setGameState(GameApplication.MAINMENU);
			}
		};
		menuItem.setText("Disconnect");
		menuItems.add(menuItem);

		menuItem = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.stop();
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
	    int x = PADDING_LEFT;
	    int y = PADDING_TOP;

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
		//return evt.isMouseEventNoWheel();
		return evt.isMouseEventNoWheel();
	}
}
