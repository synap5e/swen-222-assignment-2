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
	private Map<Integer,Entity> entities = new HashMap<Integer,Entity>(); //mapping from id to entity
	private Map<Integer,Room> rooms = new HashMap<Integer,Room>(); //mapping from id to room
	
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

	/**Moves character to the new position but only if:<ul>
	 * <li>character moves to a vacant spot in the room it's currently in
	 * <li>moves to an adjacent room through an open door & the spot it is moving to is vacant
	 * <ul>
	 * @param c The character that will be moved
	 * @param newPos Where the character will be moved to if move is valid*/
	public void moveCharacter(Character c, Vector2D newPos){
		Room room = c.getRoom();
		boolean byOpenDoor = false;

		for(Door door : room.getAllDoors()){
			Room otherRoom = door.otherRoom(room);

			if (door.getPosition().sub(newPos).sqLen() < 5 && door.canGoThrough()){
				byOpenDoor = true;
			}

			if(byOpenDoor && otherRoom.contains(newPos) && otherRoom.isPositionVacant(newPos, c.getCollisionRadius())){
				c.setPosition(newPos);
				room.removeFromRoom(c);
				otherRoom.putInRoom(c);
				c.setRoom(otherRoom);
			}
		}

		if(byOpenDoor){
			c.setPosition(newPos);
		} else if(room.contains(newPos, c.getCollisionRadius())){//moving around same room
			if(room.isPositionVacant(newPos, c.getCollisionRadius())){
				c.setPosition(newPos);
			}
		}
	}

	/**Puts the entity in the character's inventory if it is within reach of the character & is pickup-able
	 * @param character the Character picking up the entity
	 * @param entity the entity being picked up*/
	public void pickUpEntity(Character character, Entity entity){
		if(!(entity instanceof Pickup)){return;}
		if(character.withinReach(entity.getPosition()) && character.getRoom().containsEntity(entity)){
			character.pickup((Pickup) entity);
			character.getRoom().removeFromRoom(entity);
		}
	}
	
	/**Lets the character drop the entity, provided that:<ul>
	 * <li>the character is dropping it in the room the character is in
	 * <li> it is dropping it within reach
	 * <li> character is actually has the entity in their inventory
	 * <ul>
	 * @param character The character dropping the entity
	 * @param entity The entity being removed from character's inventory
	 * @param dropSpot where the entity will be placed
	 */
	public void dropEntity(Character character, Entity entity, Vector2D dropSpot){
		if(character.withinReach(dropSpot) && character.getInventory().contains(entity) 
			&& character.getRoom().contains(dropSpot) 
			&& character.getRoom().isPositionVacant(dropSpot, entity.getCollisionRadius())){
				character.drop(entity);
				entity.setPosition(dropSpot);
				character.getRoom().putInRoom(entity);
		}
		
	}
	
	/**Updates all rooms
	 * @param delta the amount of time since the previous update*/
	public void update(int delta){
		for(Room r : rooms.values()){
			r.update(delta);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Room getRoomAt(Vector2D pos) {
		for(Room r : rooms.values()){
			if(r.contains(pos)){
				return r;
			}
		}
		return null;
	}
	
	/**Returns the room with the given id.
	 * Null if invalid room id
	 * @param id the room id
	 * @return*/
	public Room getRoom(int id){
		return rooms.get(id);
	}
	
	/**Returns the entity with the given id
	 * Null if invalid id
	 * @param id the entity id
	 * @return*/
	public Entity getEntity(int id){
		return entities.get(id);
	}
	
	/**Adds room to world
	 * @param r room to be added*/
	public void addRoom(Room r){
		rooms.put(r.getID(),r);
	}
	
	/**Adds entity to world
	 * @param e entity to be added*/
	public void addEntity(Entity e){
		entities.put(e.getID(), e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ViewableRoom> getViewableRooms() {
		return new ArrayList<ViewableRoom>(rooms.values());
	}
	
	public Map<Integer, Room> getRooms(){
		return  rooms;
		
	}
}
