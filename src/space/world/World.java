package space.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWorld;
import space.math.Segment2D;
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

	public void moveCharacter(Character c, Vector2D newPos){
		Room room = c.getRoom();
		if(room.contains(newPos, c.getCollisionRadius())){//moving around same room
			if(room.isPositionVacant(newPos, c.getCollisionRadius())){
				c.setPosition(newPos);
			}
		}else{
			for(Door door : room.getAllDoors()){
				Segment2D path = new Segment2D(c.getPosition(),newPos);
				if(path.onLine(door.getPosition()) && door.canGoThrough()){
					Room otherRoom = door.otherRoom(room);
					if(otherRoom.contains(newPos, c.getCollisionRadius())&& otherRoom.isPositionVacant(newPos, c.getCollisionRadius())){
						c.setPosition(newPos);
						room.removeFromRoom(c);
						otherRoom.putInRoom(c);
						c.setRoom(otherRoom);
					}
				}
			}
		}
	}

	public void pickUpEntity(Character character, Entity entity){
		if(!(entity instanceof Pickup)){return;}
		if(character.withinReach(entity.getPosition()) && character.getRoom().containsEntity(entity)){
			character.pickupItem((Pickup) entity);
			character.getRoom().removeFromRoom(entity);
		}
	}
	
	public void dropEntity(Character character, Entity entity, Vector2D dropSpot){
		if(character.withinReach(dropSpot) && character.getRoom().contains(dropSpot)){
			character.dropItem(entity);
			entity.setPosition(dropSpot);
			character.getRoom().putInRoom(entity);
		}
	}
	
	public void update(int delta){
		for(Room r : rooms.values()){
			r.update(delta);
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
