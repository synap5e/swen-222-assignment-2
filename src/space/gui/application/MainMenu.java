package space.gui.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;

public class MainMenu extends NestedWidget {
	private static final int SPACING = 15;

	private List<Label> title;
	private List<Label> menuItems;

	private List<Integer> timers;

	InstructionsWidget instructionsWidget;
	ControlsWidget controlsWidget;

	MultiplayerWidget multiplayerWidget;

	public MainMenu(final GameApplication gameApplication){
		super(gameApplication);

		this.menuItems = new ArrayList<Label>();
		this.title = new ArrayList<Label>();
		this.timers = new ArrayList<Integer>();

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
				boolean isVisible = multiplayerWidget.isVisible();
				
				hideWidgets();
				
				multiplayerWidget.setVisible(!isVisible);
			}
		};
		menuItem.setText("Play Multiplayer");
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

		menuItem = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				boolean isVisible = controlsWidget.isVisible();
				
				hideWidgets();
				
				controlsWidget.setVisible(!isVisible);
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
		
		multiplayerWidget = new MultiplayerWidget(gameApplication);
		add(multiplayerWidget);

		instructionsWidget = new InstructionsWidget(gameApplication);
		add(instructionsWidget);

		controlsWidget = new ControlsWidget(gameApplication);
		add(controlsWidget);
	}

    @Override
    protected void layout() {
        int x = 150;
        int y = getHeight() - 200;
        int midY;

        Collections.reverse(menuItems);
        for(Label item : menuItems) {
            item.setPosition(x, y);
            item.adjustSize();
            y -= item.getHeight() + SPACING;
        }

        midY = y + SPACING * 3;

        y -= 112;
        for(Label letter : title){
        	letter.adjustSize();
        	letter.setPosition(x, y);
        	x += letter.getWidth();
        }

        instructionsWidget.updatePositions((int) (getWidth() * 0.4), midY);
        controlsWidget.updatePositions((int) (getWidth() * 0.4), midY);
        multiplayerWidget.updatePositions((int) (getWidth() * 0.4), midY);

    }

    @Override
    protected boolean handleEvent(Event evt) {
    	if(super.handleEvent(evt)){
    		return true;
    	}

    	return evt.isMouseEventNoWheel();
    }
    
    private void hideWidgets(){
		controlsWidget.setVisible(false);
		multiplayerWidget.setVisible(false);
		instructionsWidget.setVisible(false);
    }

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
    
    private int getVisibleTimer(){
    	return 30 + (int) (Math.floor(Math.random() * 120));
    }

    private int getHiddenTimer(){
    	return 5;
    }
}
