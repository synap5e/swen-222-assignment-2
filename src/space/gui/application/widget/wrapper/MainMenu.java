package space.gui.application.widget.wrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import space.gui.application.GameApplication;
import space.gui.application.GameDisplay;
import space.gui.application.widget.ControlsWidget;
import space.gui.application.widget.InstructionsWidget;
import space.gui.application.widget.MultiplayerWidget;
import space.gui.application.widget.SingleplayerWidget;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

/**
 * The root pane of the main menu screen.
 * 
 * @author Matt Graham 300211545
 */

public class MainMenu extends GUIWrapper {
	private static final int SPACING = 15;

	private List<Label> title;
	private List<Label> menuItems;

	private List<Integer> timers;

	private InstructionsWidget instructionsWidget;
	private ControlsWidget controlsWidget;

	private SingleplayerWidget singleplayerWidget;
	private MultiplayerWidget multiplayerWidget;
	
	private Label errorMessage;

	public MainMenu(final GameApplication gameApplication, final GameDisplay gameDisplay){
		super(gameDisplay);

		this.menuItems = new ArrayList<Label>();
		this.title = new ArrayList<Label>();
		this.timers = new ArrayList<Integer>();
		
		errorMessage = new Label();
		errorMessage.setTheme("label");
		add(errorMessage);

		String titleString = "SPACE";

		for(int i = 0; i != titleString.length(); i++){
			Label letter = new Label();
			letter.setText(String.valueOf(titleString.charAt(i)));
			title.add(letter);
		}

		for(Label label : title){
			label.setTheme("title");
			add(label);

			int timer = getVisibleTimer();
			timers.add(timer);
		}

		Label menuItem = new Label("Play Singleplayer"){
			@Override
			protected void handleClick(boolean doubleClick){
				boolean isVisible = singleplayerWidget.isVisible();

				hideWidgets();

				singleplayerWidget.setVisible(!isVisible);
			}
		};
		menuItems.add(menuItem);

		menuItem = new Label("Play Multiplayer"){
			@Override
			protected void handleClick(boolean doubleClick){
				boolean isVisible = multiplayerWidget.isVisible();

				hideWidgets();

				multiplayerWidget.setVisible(!isVisible);
			}
		};
		menuItems.add(menuItem);

		menuItem = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				boolean isVisible = instructionsWidget.isVisible();

				hideWidgets();

				instructionsWidget.setVisible(!isVisible);
			}
		};
		menuItem.setText("Instructions");
		menuItems.add(menuItem);

		menuItem = new Label("Controls"){
			@Override
			protected void handleClick(boolean doubleClick){
				boolean isVisible = controlsWidget.isVisible();

				hideWidgets();

				controlsWidget.setVisible(!isVisible);
			}
		};
		menuItems.add(menuItem);

		menuItem = new Label("Exit"){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setGameState(GameApplication.QUIT);
			}
		};
		menuItems.add(menuItem);


		for(Label item : menuItems) {
			item.setTheme("menuitem");
            add(item);
        }

		singleplayerWidget = new SingleplayerWidget(gameApplication, gameDisplay);
		add(singleplayerWidget);

		multiplayerWidget = new MultiplayerWidget(gameApplication, gameDisplay);
		add(multiplayerWidget);

		instructionsWidget = new InstructionsWidget(gameDisplay);
		add(instructionsWidget);

		controlsWidget = new ControlsWidget(gameDisplay);
		add(controlsWidget);
	}

    @Override
    protected void layout() {
    	super.layout();

        int x = 150;
        int y = getHeight() - 200;
        int midY;

        Collections.reverse(menuItems);

        for(Label item : menuItems) {
            item.setPosition(x, y);
            item.adjustSize();
            y -= item.getHeight() + SPACING;
        }

        midY = y + SPACING * 2;

        y -= 112;
        for(Label letter : title){
        	letter.adjustSize();
        	letter.setPosition(x, y);
        	x += letter.getWidth();
        }

        Collections.reverse(menuItems);

        instructionsWidget.updatePositions((int) (getWidth() * 0.4), midY);
        controlsWidget.updatePositions((int) (getWidth() * 0.4), midY);
        multiplayerWidget.updatePositions((int) (getWidth() * 0.4), midY);
        singleplayerWidget.updatePositions((int) (getWidth() * 0.4), midY);

    }

    @Override
    protected boolean handleEvent(Event evt) {
    	if(super.handleEvent(evt)){
    		return true;
    	}

    	return evt.isMouseEventNoWheel();
    }

    /**
     * Hides the various additional panels.
     */
    private void hideWidgets(){
    	singleplayerWidget.setVisible(false);
		controlsWidget.setVisible(false);
		multiplayerWidget.setVisible(false);
		instructionsWidget.setVisible(false);
		
		errorMessage.setVisible(false);
    }

    /**
     * Controls the flashing letters in the menu.
     */
    public void update(){
    	for(int i = 0; i != title.size(); i++){
    		Label letter = title.get(i);
    		int timer = timers.get(i);
	    	if(timer == 0){
	    		if(letter.isVisible()){
	    			letter.setVisible(false);
	    			timer = getHiddenTimer();
	    		} else {
	    			letter.setVisible(true);
	    			timer = getVisibleTimer();
	    		}
	    	}
	    	timer--;
	    	timers.set(i, timer);
    	}
    }

    /**
     * Provides the timing for how long a letter should be visible.
     * 
     * @return
     */
    private int getVisibleTimer(){
    	return 30 + (int) (Math.floor(Math.random() * 120));
    }

    /**
     * Provides the timing for how long a letter should be hidden.
     * 
     * @return
     */
    private int getHiddenTimer(){
    	return 5;
    }
    
    /**
     * Displays an error to the user.
     * 
     * @param text the error message
     */
    public void showError(String text){
    	hideWidgets();
    	
    	errorMessage.setText(text);
    	errorMessage.setVisible(true);
    	
    	errorMessage.adjustSize();
    	errorMessage.setPosition((getWidth() - errorMessage.getWidth()) / 2, getHeight() - errorMessage.getHeight() - 20);
    }

}
