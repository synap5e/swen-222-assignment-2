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
	
	public final static int SPACING = 15;
	public final static int PADDING = 5;

	Label submit;

	List<ItemWidget> items;
	List<Label> descriptions;

	Set<Entity> selection;

	public InventoryWidget(GameApplication gameApplication) {
		super(gameApplication);

		this.items = new ArrayList<ItemWidget>();
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
		
		updatePositions(gameApplication.getWidth() / 3, gameApplication.getHeight() / 2);

	}

	@Override
	protected void layout(){
		super.layout();
		
		int x = startX;
		int y = startY;
		
		for(int i = 0; i != items.size(); i++){
			Label item = items.get(i);
			item.adjustSize();
			item.setPosition(x, y);
			
			y += item.getHeight() + PADDING;
			
			Label description = descriptions.get(i);
			description.adjustSize();
			description.setPosition(x, y);
			
			y += description.getHeight() + SPACING;
		}
		
		submit.adjustSize();
		submit.setPosition(x, y);
	}

	@Override
    protected boolean handleEvent(Event evt) {
		if(super.handleEvent(evt)){
			return true;
		}

		if(isVisible() && evt.getKeyCode() == Event.KEY_ESCAPE){
			gameApplication.setInventoryVisible(false);
		}

		return true;
	}
	
	public boolean update(Entity entity) {
		if(entity == null || !(entity instanceof Container)){
			return false;
		}

		update((Container) entity);
		
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

			ItemWidget item = new ItemWidget(entity, this);
			items.add(item);
			add(item);

			Label description = new Label();
			description.setText(entity.getDescription());
			description.setTheme("description");
			descriptions.add(description);
			add(description);
		}
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
	
	@Override
	public void setVisible(boolean flag){
		if(flag){
			layout();
		}
		super.setVisible(flag);
	}
}
