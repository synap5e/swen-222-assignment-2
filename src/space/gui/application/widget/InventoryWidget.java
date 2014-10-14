package space.gui.application.widget;

import java.util.ArrayList;
import java.util.List;

import space.gui.application.GameApplication;
import space.gui.application.GameDisplay;
import space.gui.application.widget.label.ItemDescription;
import space.gui.application.widget.label.ItemLabel;
import space.world.Entity;
import space.world.Pickup;
import de.matthiasmann.twl.Label;

/**
 * The interface used to view/drop the current player's items.
 *
 * @author Matt Graham 300211545
 */

public class InventoryWidget extends NestedWidget {

	private final static String ACCEPT = "Drop";
	private final static String CLOSE = "Cancel";

	private final static int SPACING = 10;
	private final static int PADDING = 30;

	private final static int COLUMN = 150;

	private final static int PANEL = 350;

	private Label accept;
	private Label cancel;

	private Label playerName;

	private List<ItemLabel> playerItems;
	private List<Label> playerDescriptions;

	private ItemLabel selection;

	GameApplication gameApplication;

	public InventoryWidget(final GameApplication gameApplication, final GameDisplay gameDisplay) {
		super(gameDisplay);

		this.gameApplication = gameApplication;

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
				gameDisplay.setInventoryVisible(false);
			}
		};
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

		y += SPACING * 2;
		x = startX;

		accept.adjustSize();
		accept.setPosition(x, y);

		y += accept.getHeight() + SPACING;

		cancel.adjustSize();
		cancel.setPosition(x, y);
	}


	/**
	 * Resets and re-populates the panel in preparation of displaying the inventory.
	 */
	public void update(List<Pickup> pickups){
		resetGUI();
		resetLists();

		accept.setEnabled(false);

		for(Pickup pickup : pickups){
			generateLabel((Entity) pickup);
		}

	}

	/**
	 * Removes all item views.
	 */
	private void resetGUI(){
		for(Label item : playerItems){
			removeChild(item);
		}

		for(Label item : playerDescriptions){
			removeChild(item);
		}
	}

	/**
	 * Resets the various item lists.
	 */
	private void resetLists(){
		playerItems.clear();
		playerDescriptions.clear();

		selection = null;
	}

	/**
	 * Create the interactive items which are part of the interface and adds them to the various lists.
	 *
	 * @param entity
	 */
	private void generateLabel(Entity entity){
		Label description = new ItemDescription(entity.getDescription());
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

	/**
	 * Controls whether the accept button is enabled.
	 */
	private void updateAccept() {
		accept.setEnabled(selection != null);
	}


	/**
	 * Submits the changes made by the user in the interface.
	 */
	private void submitChanges(){
		gameApplication.getClient().drop(selection.getEntity());

		gameDisplay.setInventoryVisible(false);
	}


	/**
	 * Selects the given item view.
	 *
	 * @param item
	 */
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
