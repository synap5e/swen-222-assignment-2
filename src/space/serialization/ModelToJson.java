package space.serialization;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import space.network.storage.WorldSaver;
import space.world.Door;
import space.world.Entity;
import space.world.Key;
import space.world.NonStationary;
import space.world.Pickup;
import space.world.Character;
import space.world.Player;
import space.world.Room;
import space.world.Stationary;
import space.world.World;

public class ModelToJson implements WorldSaver{

	JSONObject file = new JSONObject();
	JSONArray listOfRooms = new JSONArray();
	JSONArray listOfPlayers = new JSONArray();

	@Override
	public void saveWorld(String savePath, World world, List<Player> players) {


		Map<Integer, Room> rooms = world.getRooms();
		for (Entry<Integer, Room> entry : rooms.entrySet()) {
			listOfRooms.add(addRoom(entry.getKey(), entry.getValue()));
		}
		file.put("rooms", listOfRooms);
		
		for(Player p : players){
			listOfPlayers.add(addPlayer(p));
		}
		
		file.put("players", listOfPlayers);
		
		
	}

	private JSONObject addRoom(Integer roomId, Room room) {
		JSONObject r = new JSONObject();
		r.put("id", roomId);
		r.put("Light Mode", room.getLightMode());
		r.put("description", room.getDescription());
		r.put("Room Shape", room.getRoomShape());
		r.put("contains", addContains(room));
		r.put("walls", addWall(room.getDoors()));
		return r;
	}

	private JSONArray addWall(Map<Integer, List<Door>> doors) {
		JSONArray walls = new JSONArray();
		for (Entry<Integer, List<Door>> entry : doors.entrySet()) {
			JSONObject wall = new JSONObject();
			wall.put(entry.getKey(),addDoors(entry.getValue()));
			walls.add(wall);
		}
		return walls;
	}

	private JSONObject addDoors(List<Door> value) {
		JSONArray doors = new JSONArray();
		JSONObject d = new JSONObject();
		for(Door dr: value){
			doors.add(dr);
		}
		d.put("doors", doors);
		return d;
	}
	

	private JSONArray addContains(Room room) {
		Set<Entity> entities = room.getEntities();
		JSONArray entitiesInRoom = new JSONArray();
		for(Entity e: entities){
			if(e instanceof Stationary){
				entitiesInRoom.add(addStationary(e));
			}
			else if(e instanceof NonStationary){
				if(e instanceof Player){
					listOfPlayers.add(addPlayer((Player) e));
				}
				else{
				entitiesInRoom.add(addNonStationary(e));
				}
			}
		}
		return entitiesInRoom;
	}

	private JSONObject addNonStationary(Entity e) {
		if(e instanceof Door){
			return AddDoor((Door) e);
		}
		if(e instanceof Pickup){
			return AddPickup((Pickup) e);
		}
		else if(e instanceof Character){
			return AddCharacter((Character)e);
		}
		return null;
	}

	private JSONObject AddCharacter(Character e) {
		//this case should never happen
		if(e instanceof Player){
			return null;
		}
		//no other kind of character in game yet
		else{
			return null;
		}
	}

	private JSONObject addPlayer(Player e) {
		JSONObject player = new JSONObject();
		player.put("points", e.getPoints());
		player.put("x rotation", e.getXRotation());
		player.put("y rotation", e.getYRotation());
		player.put("id", e.getID());
		JSONArray positionints = new JSONArray();
		positionints.add(e.getPosition().getX());
		positionints.add(e.getPosition().getY());
		player.put("position",positionints);
		return player;
	}

	private JSONObject AddPickup(Pickup e) {
		if(e instanceof Key){
			return AddKey((Key) e);
		}
		else{
			//no other type of pickup in game yet
			return null;
		}
	}

	private JSONObject AddKey(Key e) {
		JSONObject key = new JSONObject();
		key.put("exitid", e.getExit().getID());
		key.put("id", e.geti());
		key.put("description", e.getDescription());
		JSONArray positionints = new JSONArray();
		positionints.add(e.getPosition().getX());
		positionints.add(e.getPosition().getY());
		key.put("position",positionints);
		return key;
	}

	private JSONObject AddDoor(Door e) {
		JSONObject door = new JSONObject();
		door.put("room1id",e.getRoom1().getID());
		door.put("room2id", e.getRoom2().getID());
		door.put("is locked",e.isLocked());
		door.put("one way", e.isOneWay());
		door.put("amount open", e.getOpenPercent());
		door.put("id", e.geti());
		door.put("description", e.getDescription());
		JSONArray positionints = new JSONArray();
		positionints.add(e.getPosition().getX());
		positionints.add(e.getPosition().getY());
		door.put("position",positionints);
		return door;
	}

	private JSONObject addStationary(Entity e) {
		// I don't think we have anything that implements a stationary yet? 
		return null;
	}

}
