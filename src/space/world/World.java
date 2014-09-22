package space.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWorld;
import space.math.Vector2D;

public class World implements ViewableWorld{
	private Map<Integer,Entity> entities = new HashMap<Integer,Entity>();
	private Map<Integer,Room> rooms = new HashMap<Integer,Room>();
	
	public World(){
	}
	
	public World(Set<Entity> ent, Set<Room> room){
		for(Entity e: ent){
			addEntity(e);
		}
		for(Room r: room){
			addRoom(r);
		}
	}

	public void movePlayer(Player p, Vector2D newPos){
		Room room = p.getRoom();
		if(room.contains(newPos, /* TODO: p.getCollisionRadius() */0)){//moving around same room
			if(room.isPositionVacant(newPos, /* TODO: p.getCollisionRadius() */0)){
				p.setPosition(newPos);
			}
		}else{
			for(Map.Entry<Room, Exit> entry : room.getExits().entrySet()){//see if player trying to move to adjacent room
				if(entry.getKey().contains(newPos, /* TODO: p.getCollisionRadius() */0)){//found room player is trying to move to
					if(entry.getValue().canGoThrough(p)&&entry.getKey().isPositionVacant(newPos, /* TODO: p.getCollisionRadius() */ 0)){
					//check that player can go through exit and nothing is on the new position
						p.setPosition(newPos);
						room.removeFromRoom(p);
						entry.getKey().putInRoom(p);
						p.setRoom(entry.getKey());
					}
					return;
				}
			}
		}
	}

	public void pickUpEntity(Player player, Entity entity){
		if(!(entity instanceof Pickup)){return;}
		if(player.withinReach(entity.getPosition()) && player.getRoom().containsEntity(entity)){
			player.pickupItem((Pickup) entity);
			player.getRoom().removeFromRoom(entity);
		}
	}
	
	public void dropEntity(Player player, Entity entity, Vector2D dropSpot){
		if(player.withinReach(dropSpot) && player.getRoom().contains(dropSpot)){
			player.dropItem(entity);
			entity.setPosition(dropSpot);
			player.getRoom().putInRoom(entity);
		}
	}
	@Override
	public Room getRoomAt(Vector2D pos) {
		for(Room r : rooms.values()){
			if(r.contains(pos)){
				return r;
			}
		}
		return null;
	}
	
	public Room getRoom(int id){
		return rooms.get(id);
	}
	
	public Entity getEntity(int id){
		return entities.get(id);
	}
	
	public void addRoom(Room r){
		rooms.put(r.getID(),r);
	}
	
	public void addEntity(Entity e){
		entities.put(e.getID(), e);
	}

	@Override
	public List<ViewableRoom> getViewableRooms() {
		return new ArrayList<ViewableRoom>(rooms.values());
	}
	
}
