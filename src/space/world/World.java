package space.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWord;
import space.util.Vec2;

public class World implements ViewableWord{
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
	
	@Override
	public ViewableRoom getRoomAt(Vec2 pos) {
		for(Room r : rooms.values()){
			if(r.pointInRoom(pos)){
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
	
}
