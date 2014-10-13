package space.gui.application.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;
import space.gui.application.GameApplication;
import space.world.Container;
import space.world.Entity;
import space.world.Pickup;
import space.world.Player;

public class InventoryWidget extends NestedWidget {

	public final static String SUBMIT = "Close";

	Label submit;

	List<Label> items;
	List<Label> descriptions;

	Set<Entity> selection;

	public InventoryWidget(GameApplication gameApplication) {
		super(gameApplication);

		this.items = new ArrayList<Label>();
		this.descriptions = new ArrayList<Label>();
		this.selection = new HashSet<Entity>();

		setVisible(false);

		submit = new Label(){
			@Override
			protected void handleClick(boolean doubleClick){
				submitChanges();
			}
		};
		submit.setText(SUBMIT);
		submit.setTheme("item");
		add(submit);

	}

	@Override
	public void layout(){

	}

	@Override
    protected boolean handleEvent(Event evt) {
		if(super.handleEvent(evt)){
			return true;
		}

		if(evt.getKeyCode() == Event.KEY_ESCAPE){
			System.out.println("sdf");
			gameApplication.setInventoryVisible(false);
		}

		return true;
	}

	public void update(Container from){
		update(from.getItemsContained());
	}

	private void update(List<Pickup> pickups){
		for(Label item : items){
			removeChild(item);
		}

		items.clear();
		selection.clear();

		for(Pickup pickup : pickups){
			Entity entity = (Entity) pickup;

			Label item = new Label();
			items.add(item);
			add(item);

			Label description = new Label();
			description.setText(entity.getDescription());
			item.setTheme("description");
			descriptions.add(description);
			add(description);
		}
	}

	public void update(Entity entity) {
		if(entity == null || !(entity instanceof Container)){
			return;
		}

		update((Container) entity);
	}

	public void addSelected(Entity entity) {
		selection.add(entity);
	}

	public void removeSelected(Entity entity) {
		selection.remove(entity);
	}

	public void submitChanges(){
		gameApplication.setInventoryVisible(false);
	}
}
