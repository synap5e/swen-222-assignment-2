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

public class InventoryExchangeWidget extends NestedWidget {

	public final static String ACCEPT = "Accept";
	public final static String CLOSE = "Cancel";

	public final static int SPACING = 10;
	public final static int PADDING = 30;
	
	public final static int COLUMN = 100;
	public final static int PANEL = 300;
	
	Label accept;
	Label cancel;
	
	Label playerName;
	Label containerName;

	List<ItemLabel> playerItems;
	List<Label> playerDescriptions;
	
	List<ItemLabel> containerItems;
	List<Label> containerDescriptions;

	Set<Entity> takeSelection;
	Set<Entity> giveSelection;
	
	Container container;

	public InventoryExchangeWidget(final GameApplication gameApplication) {
		super(gameApplication);

		playerItems = new ArrayList<ItemLabel>();
		playerDescriptions = new ArrayList<Label>();
		
		containerItems = new ArrayList<ItemLabel>();
		containerDescriptions = new ArrayList<Label>();
		
		takeSelection = new HashSet<Entity>();
		giveSelection = new HashSet<Entity>();

		setVisible(false);
		
		playerName = new Label();
		playerName.setText("Player");
		playerName.setTheme("title");
		add(playerName);	
		
		containerName = new Label();
		containerName.setTheme("title");
		add(containerName);	

		accept = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				submitChanges();
			}
		};
		accept.setText(ACCEPT);
		accept.setTheme("item");
		add(accept);
		
		cancel = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				gameApplication.setInventoryExchangeVisible(false);
			}
		};
		cancel.setText(CLOSE);
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
		
		int midY = y;
		
		x += PANEL;
		y = startY;
		
		containerName.adjustSize();
		containerName.setPosition(x, y);
		
		y += containerName.getHeight() + SPACING;
		x += PADDING;
		
		for(int i = 0; i != containerItems.size(); i++){
			Label item = containerItems.get(i);
			item.adjustSize();
			item.setPosition(x, y);
			
			Label description = containerDescriptions.get(i);
			description.adjustSize();
			description.setPosition(x + COLUMN, y);
			
			y += item.getHeight() + SPACING;
		}
		
		y = Math.max(midY, y) + SPACING * 2;
		x = startX;
		
		accept.adjustSize();
		accept.setPosition(x, y);
		
		y += accept.getHeight() + SPACING;
		
		cancel.adjustSize();
		cancel.setPosition(x, y);
	}
	
	public boolean update(Entity entity) {
		if(entity == null || !(entity instanceof Container)){
			return false;
		}

		update((Container) entity);
		
		return true;
	}

	public void update(Container container){
		this.container = container;
		
		update(container.getItemsContained());
		
		containerName.setText(container.getName());
	}

	private void update(List<Pickup> pickups){
		resetGUI();
		resetLists();
		
		accept.setEnabled(false);

		for(Pickup pickup : pickups){
			generateContainerLabels((Entity) pickup);
		}
		
		for(Pickup pickup : gameApplication.getClient().getLocalPlayer().getInventory()){
			generatePlayerLabels((Entity) pickup);
		}
		
	}
	
	private void resetGUI(){
		for(Label item : playerItems){
			removeChild(item);
		}
		
		for(Label item : containerItems){
			removeChild(item);
		}
		
		for(Label item : playerDescriptions){
			removeChild(item);
		}
		
		for(Label item : containerDescriptions){
			removeChild(item);
		}
	}
	
	private void resetLists(){
		playerItems.clear();
		playerDescriptions.clear();
		
		containerItems.clear();
		containerDescriptions.clear();
		
		takeSelection.clear();
		giveSelection.clear();
	}
	
	private void generateContainerLabels(Entity entity){
		Label description = new ItemDescriptionLabel(entity.getDescription());
		containerDescriptions.add(description);
		add(description);
		
		ItemLabel item = new ItemLabel(entity, description){
			@Override
			protected void handleClick(boolean doubleClick){
				setSelected(!isSelected());

				if(isSelected()){
					moveToPlayer(this);
					takeSelection.add(getEntity());

				} else {
					moveToContainer(this);
					takeSelection.remove(getEntity());
				}
				
				updateAccept();
				reapplyTheme();
			}
		};
		containerItems.add(item);
		add(item);
	}
	
	private void generatePlayerLabels(Entity entity){
		Label description = new ItemDescriptionLabel(entity.getDescription());
		playerDescriptions.add(description);
		add(description);
		
		ItemLabel item = new ItemLabel(entity, description){
			@Override
			protected void handleClick(boolean doubleClick){
				setSelected(!isSelected());

				if(isSelected()){
					moveToContainer(this);
					giveSelection.add(getEntity());
					
				} else {
					moveToPlayer(this);
					giveSelection.remove(getEntity());
				}
				
				updateAccept();
				reapplyTheme();
			}
		};
		playerItems.add(item);
		add(item);
	}

	public void updateAccept() {
		accept.setEnabled(!takeSelection.isEmpty() || !giveSelection.isEmpty());
	}

	public void submitChanges(){
		for(Entity entity : takeSelection){
			gameApplication.getClient().transfer(entity, container, gameApplication.getClient().getLocalPlayer());
		}
		for(Entity entity : giveSelection){
			gameApplication.getClient().transfer(entity, gameApplication.getClient().getLocalPlayer(), container);
		}
		
		gameApplication.setInventoryExchangeVisible(false);
	}
	
	public void moveToPlayer(ItemLabel item){
		playerItems.add(item);
		playerDescriptions.add(item.getDescription());
		
		containerItems.remove(item);
		containerDescriptions.remove(item.getDescription());
	}
	
	public void moveToContainer(ItemLabel item){
		playerItems.remove(item);
		playerDescriptions.remove(item.getDescription());
		
		containerItems.add(item);
		containerDescriptions.add(item.getDescription());
	}
}
