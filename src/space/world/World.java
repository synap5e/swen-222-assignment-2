package space.world;

import java.util.HashMap;
import java.util.Map;

import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWord;
import space.util.Vec2;

public class World implements ViewableWord{
	private Map<Integer,Entity> entities = new HashMap<Integer,Entity>();
	private Map<Integer,Room> rooms = new HashMap<Integer,Room>();
	
	public World(){
		
	}
	
	@Override
	public ViewableRoom getRoomAt(Vec2 pos) {
		for(Room r : rooms.values()){
			
		}
		return null;
	}
	
	public Room getRoom(int id){
		return rooms.get(id);
	}
	
	public Entity getEntity(int id){
		return entities.get(id);
	}
	
}
