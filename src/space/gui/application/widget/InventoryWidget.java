package space.gui.application.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import space.gui.application.GameApplication;
import space.gui.application.widget.label.ItemDescriptionLabel;
import space.gui.application.widget.label.ItemLabel;
import space.world.Container;
import space.world.Entity;
import space.world.Pickup;
import de.matthiasmann.twl.Label;

public class InventoryWidget extends NestedWidget {

	public final static String ACCEPT = "Drop";
	public final static String CLOSE = "Cancel";

	public final static int SPACING = 10;
	public final static int PADDING = 30;
	
	public final static int COLUMN = 100;
	public final static int PANEL = 300;
	
	Label accept;
	Label cancel;
	
	Label playerName;

	List<ItemLabel> playerItems;
	List<Label> playerDescriptions;

	ItemLabel selection;

	public InventoryWidget(final GameApplication gameApplication) {
		super(gameApplication);

		playerItems = new ArrayList<ItemLabel>();
		playerDescriptions = new ArrayList<Label>();

		selection = null;

		setVisible(false);
		
		playerName = new Label("Player");
		playerName.setTheme("title");
		add(playerName);

		accept = new Label(ACCEPT){
			@Override
			protected void handleClick(boolean doubleClick){
				submitChanges();
			}
		};
		accept.setTheme("item");
		add(accept);
		
		cancel = new Label(CLOSE){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setInventoryVisible(false);
			}
		};
		cancel.setTheme("item");
		add(cancel);

		updatePositions(gameApplication.getWidth() / 3, gameApplication.getHeight() / 2);
		
	}

	@Override
	protected void layout(){
		super.layout();
		
		int x = startX;
		int y = startY;
		
		playerName.adjustSize();
		playerName.setPosition(x, y);
		
		y += playerName.getHeight() + SPACING;
		x += PADDING;
		
		for(int i = 0; i != playerItems.size(); i++){
			Label item = playerItems.get(i);
			item.adjustSize();
			item.setPosition(x, y);
			
			Label description = playerDescriptions.get(i);
			description.adjustSize();
			description.setPosition(x + COLUMN, y);
			
			y += item.getHeight() + SPACING;
		}
		
		y += SPACING * 2;
		x = startX;
		
		accept.adjustSize();
		accept.setPosition(x, y);
		
		y += accept.getHeight() + SPACING;
		
		cancel.adjustSize();
		cancel.setPosition(x, y);
	}


	public void update(){
		resetGUI();
		resetLists();
		
		accept.setEnabled(false);
		
		for(Pickup pickup : gameApplication.getClient().getLocalPlayer().getInventory()){
			generatePlayerLabels((Entity) pickup);
		}
		
	}
	
	private void resetGUI(){
		for(Label item : playerItems){
			removeChild(item);
		}
		
		for(Label item : playerDescriptions){
			removeChild(item);
		}
	}
	
	private void resetLists(){
		playerItems.clear();
		playerDescriptions.clear();
		
		selection = null;
	}
	
	private void generatePlayerLabels(Entity entity){
		Label description = new ItemDescriptionLabel(entity.getDescription());
		playerDescriptions.add(description);
		add(description);
		
		ItemLabel item = new ItemLabel(entity, description){
			@Override
			protected void handleClick(boolean doubleClick){
				setSelected(!isSelected());

				select(this);
				
				updateAccept();
				reapplyTheme();
			}

		};
		playerItems.add(item);
		add(item);
	}

	public void updateAccept() {
		accept.setEnabled(selection != null);
	}

	public void submitChanges(){
		gameApplication.getClient().drop(selection.getEntity());
		
		gameApplication.setInventoryVisible(false);
	}
	

	private void select(ItemLabel item) {
		if(item == selection){
			selection = null;
		} else {
			if(selection != null){
				selection.setSelected(false);
			}
			selection = item;
		}
		
		updateAccept();
	}
}
