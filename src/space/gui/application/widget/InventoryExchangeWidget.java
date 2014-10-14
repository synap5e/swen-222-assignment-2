package space.gui.application.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import space.gui.application.GameApplication;
import space.gui.application.GameDisplay;
import space.gui.application.widget.label.ItemDescription;
import space.gui.application.widget.label.ItemLabel;
import space.world.Container;
import space.world.Entity;
import space.world.Pickup;
import de.matthiasmann.twl.Label;

/**
 * The interface which appears when using a container to take/give items.
 *
 * @author Matt Graham 300211545
 */

public class InventoryExchangeWidget extends NestedWidget {

	private final static String ACCEPT = "Accept";
	private final static String CLOSE = "Cancel";

	private final static int SPACING = 10;
	private final static int PADDING = 30;

	private final static int COLUMN = 150;
	private final static int PANEL = 350;

	private Label accept;
	private Label cancel;

	private Label playerName;
	private Label containerName;

	private List<ItemLabel> playerItems;
	private List<Label> playerDescriptions;

	private List<ItemLabel> containerItems;
	private List<Label> containerDescriptions;

	private Set<Entity> takeSelection;
	private Set<Entity> giveSelection;

	private Container container;

	private GameApplication gameApplication;

	public InventoryExchangeWidget(final GameApplication gameApplication, final GameDisplay gameDisplay) {
		super(gameDisplay);

		this.gameApplication = gameApplication;

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
				gameDisplay.setInventoryExchangeVisible(false);
			}
		};
		cancel.setText(CLOSE);
		cancel.setTheme("item");
		add(cancel);

		updatePositions((gameDisplay.getWidth() - PANEL * 2) / 2, gameDisplay.getHeight() / 2);

	}

	@Override
	protected void layout(){
		super.layout();

		layout(startX);
	}

	private void layout(int startX){
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

	/**
	 * Determines if the given container can have items exchanged with, and sets up the interface if so.
	 *
	 * @param entity
	 * @return
	 */
	public boolean update(Container container, List<Pickup> playerPickups){
		this.container = container;

		update(container.getItemsContained(), playerPickups);

		containerName.setText(container.getName());

		return true;
	}

	/**
	 * Generates the various lists of item views for the interface.
	 *
	 * @param pickups
	 */
	private void update(List<Pickup> pickups, List<Pickup> playerPickups){
		resetGUI();
		resetLists();

		accept.setEnabled(false);

		for(Pickup pickup : pickups){
			generateContainerItems((Entity) pickup);
		}

		for(Pickup pickup : playerPickups){
			generatePlayerItems((Entity) pickup);
		}

	}

	/**
	 * Removes all item views from the interface.
	 */
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

	/**
	 * Resets all the various lists containing item views and selected items.
	 */
	private void resetLists(){
		playerItems.clear();
		playerDescriptions.clear();

		containerItems.clear();
		containerDescriptions.clear();

		takeSelection.clear();
		giveSelection.clear();
	}

	/**
	 * Creates the item views for the container's items and adds them to the GUI.
	 *
	 * @param entity
	 */
	private void generateContainerItems(Entity entity){
		Label description = new ItemDescription(entity.getDescription());
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

	/**
	 * Creates the item views for the player's items and adds them to the GUI.
	 *
	 * @param entity
	 */
	private void generatePlayerItems(Entity entity){
		Label description = new ItemDescription(entity.getDescription());
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

	/**
	 * Sets if the accept button is enabled.
	 */
	private void updateAccept() {
		accept.setEnabled(!takeSelection.isEmpty() || !giveSelection.isEmpty());
	}

	/**
	 * Informs the client about the changes made using the interface.
	 */
	private void submitChanges(){
		for(Entity entity : takeSelection){
			gameApplication.getClient().transfer(entity, container, gameApplication.getClient().getLocalPlayer());
		}
		for(Entity entity : giveSelection){
			gameApplication.getClient().transfer(entity, gameApplication.getClient().getLocalPlayer(), container);
		}

		gameDisplay.setInventoryExchangeVisible(false);
	}

	/**
	 * Shifts an item view to the player's column.
	 *
	 * @param item
	 */
	private void moveToPlayer(ItemLabel item){
		playerItems.add(item);
		playerDescriptions.add(item.getDescription());

		containerItems.remove(item);
		containerDescriptions.remove(item.getDescription());
	}

	/**
	 * Shifts an item view to the container's column.
	 *
	 * @param item
	 */
	private void moveToContainer(ItemLabel item){
		playerItems.remove(item);
		playerDescriptions.remove(item.getDescription());

		containerItems.add(item);
		containerDescriptions.add(item.getDescription());
	}
}
