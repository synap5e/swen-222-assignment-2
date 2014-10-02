package space.serialization;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import space.math.ConcaveHull;
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

	MyJsonObject fileobject = new MyJsonObject();
	MyJsonList listOfRooms = new MyJsonList();
	MyJsonList listOfPlayers = new MyJsonList();

	@Override
	public void saveWorld(String savePath, World world, List<Player> players) {


		Map<Integer, Room> rooms = world.getRooms();
		for (Entry<Integer, Room> entry : rooms.entrySet()) {
			listOfRooms.add(addRoom(entry.getKey(), entry.getValue()));
		}
		fileobject.put("rooms", listOfRooms);
		
		for(Player p : players){
			listOfPlayers.add(addPlayer(p));
		}
		fileobject.put("players", listOfPlayers);
		
		FileWriter file = null;
		try {
			file = new FileWriter(savePath+".txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
            file.write(fileobject.toString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + fileobject);
 
        } catch (IOException e) {
            e.printStackTrace();
 
        } finally {
            try {
				file.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
		
	}

	private MyJsonObject addRoom(Integer roomId, Room room) {
		MyJsonObject r = new MyJsonObject();
		r.put("id", roomId);
		r.put("Light Mode", room.getLightMode().toString());
		r.put("description", room.getDescription());
		r.put("Room Shape", addRoomShape(room.getRoomShape()));
		r.put("contains", addContains(room));
		r.put("walls", addWall(room.getDoors()));
		return r;
	}

	private MyJsonObject addRoomShape(ConcaveHull roomShape) {
		MyJsonObject hull = new MyJsonObject();
		//TO DO ASK SIMON ABOOUT CONCAVE HULL
		return null;
	}

	private MyJsonList addWall(Map<Integer, List<Door>> doors) {
		MyJsonList walls = new MyJsonList();
		for (Entry<Integer, List<Door>> entry : doors.entrySet()) {
			MyJsonObject wall = new MyJsonObject();
			wall.put(entry.getKey().toString(),addDoors(entry.getValue()));
			walls.add(wall);
		}
		return walls;
	}

	private MyJsonObject addDoors(List<Door> value) {
		MyJsonList doors = new MyJsonList();
		MyJsonObject d = new MyJsonObject();
		for(Door dr: value){
			doors.add(dr.getID());
		}
		d.put("doors id", doors);
		return d;
	}
	

	private MyJsonList addContains(Room room) {
		Set<Entity> entities = room.getEntities();
		MyJsonList entitiesInRoom = new MyJsonList();
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

	private MyJsonObject addNonStationary(Entity e) {
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

	private MyJsonObject AddCharacter(Character e) {
		//this case should never happen
		if(e instanceof Player){
			return null;
		}
		//no other kind of character in game yet
		else{
			return null;
		}
	}

	private MyJsonObject addPlayer(Player e) {
		MyJsonObject player = new MyJsonObject();
		player.put("points", e.getPoints());
		player.put("x rotation", e.getXRotation());
		player.put("y rotation", e.getYRotation());
		player.put("id", e.getID());
		MyJsonList positionints = new MyJsonList();
		positionints.add(e.getPosition().getX());
		positionints.add(e.getPosition().getY());
		player.put("position",positionints);
		return player;
	}

	private MyJsonObject AddPickup(Pickup e) {
		if(e instanceof Key){
			return AddKey((Key) e);
		}
		else{
			//no other type of pickup in game yet
			return null;
		}
	}

	private MyJsonObject AddKey(Key e) {
		MyJsonObject key = new MyJsonObject();
		key.put("doorid", e.getExit().getID());
		key.put("id", e.geti());
		key.put("description", e.getDescription());
		MyJsonList positionints = new MyJsonList();
		positionints.add(e.getPosition().getX());
		positionints.add(e.getPosition().getY());
		key.put("position",positionints);
		return key;
	}

	private MyJsonObject AddDoor(Door e) {
		MyJsonObject door = new MyJsonObject();
		door.put("room1id",e.getRoom1().getID());
		door.put("room2id", e.getRoom2().getID());
		door.put("is locked",e.isLocked());
		door.put("one way", e.isOneWay());
		door.put("amount open", e.getOpenPercent());
		door.put("id", e.getID());
		door.put("description", e.getDescription());
		MyJsonList positionints = new MyJsonList();
		positionints.add(e.getPosition().getX());
		positionints.add(e.getPosition().getY());
		door.put("position",positionints);
		return door;
	}

	private MyJsonObject addStationary(Entity e) {
		// I don't think we have anything that implements a stationary yet? 
		return null;
	}

}
