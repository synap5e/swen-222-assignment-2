package space.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWord;
import space.util.Vec2;
import space.world.items.*;

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
	
	public void movePlayer(Player p, Vec2 newPosition){//probably need to do something on invalid moves...
		Room prevRoom = getRoomAt(p.getPosition());
		Room newRoom = getRoomAt(newPosition);
		if(prevRoom.equals(newRoom)){
			p.setPosition(newPosition);
		}else{
			Exit exit = prevRoom.getExitTo(newRoom);
			if(exit != null){
				if(exit.isLocked()){//player needs a key that opens the exit
					for(Item i : p.getInventory()){
						if(i instanceof Key && ((Key) i).getExit().equals(exit)){
							p.setPosition(newPosition);
							prevRoom.leaveRoom(p);
							newRoom.enterRoom(p);
						}
					}
				}else{//not locked,player can proceed to go to other room
					p.setPosition(newPosition);
					prevRoom.leaveRoom(p);
					newRoom.enterRoom(p);
				}
			}
		}
	}
	
	@Override
	public Room getRoomAt(Vec2 pos) {
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

	@Override
	public List<ViewableRoom> getViewableRooms() {
		return new ArrayList<ViewableRoom>(rooms.values());
	}
	
}
