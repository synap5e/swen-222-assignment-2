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
import space.math.Vector2D;
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
/**
 * a class that is used to save the model to a Json file that can be loaded back
 * in the game at a later stage
 * 
 * used to store the game data of the current player - and the state of the 
 * world that the current player was in 
 * 
 * Written like a parser
 * 
 * @author Shweta Barapatre
 *
 */

public class ModelToJson implements WorldSaver{

	MyJsonObject fileobject = new MyJsonObject();
	MyJsonList listOfRooms = new MyJsonList();
	MyJsonList listOfPlayers = new MyJsonList();
	
	/**
	 * {@inheritDoc}
	 */
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
	/**
	 * A method to create and return a MyJsonObject of a room that is passed into the method
	 * 
	 * @param roomId the id of the room
	 * @param room the room to be saved
	 * @return MyJsonOnject of room to be saved
	 */
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

	
	/**
	 * A method to create and return a MyJsonObject of a room shape
	 * @param roomShape the concave hull that is the shape of the room
	 * @return a MyJsonObject representation of the Convave Hull
	 */
	private MyJsonObject addRoomShape(ConcaveHull roomShape) {
		MyJsonObject hull = new MyJsonObject();
		MyJsonList points = new MyJsonList();
		for(Vector2D v:roomShape.getDefiningPoints()){
			MyJsonList pos = new MyJsonList();
			pos.add(v.getX());
			pos.add(v.getY());
			points.add(pos);
		}
		hull.put("points", points);
		return hull;
	}
	
	/**
	 * A method to create and return a MyJsonObjec of a wall
	 * 
	 * @param doors the list of doors on the wall
	 * @return MyJsonObject representation of wall
	 */

	private MyJsonList addWall(Map<Integer, List<Door>> doors) {
		MyJsonList walls = new MyJsonList();
		for (Entry<Integer, List<Door>> entry : doors.entrySet()) {
			MyJsonObject wall = new MyJsonObject();
			wall.put(entry.getKey().toString(),addDoors(entry.getValue()));
			walls.add(wall);
		}
		return walls;
	}

	/**
	 * A method that creates and returns a myJsonObject of doors 
	 * @param value list of Doors in room
	 * @return A MyJsonObect consisting of a MyJsonList of each door in a room
	 */
	private MyJsonObject addDoors(List<Door> value) {
		MyJsonList doors = new MyJsonList();
		MyJsonObject d = new MyJsonObject();
		for(Door dr: value){
			doors.add(dr.getID());
		}
		d.put("doors id", doors);
		return d;
	}
	
	/**
	 * Method that returns a MyJsonList of the entities with a room
	 * 
	 * @param room room that holds the entities
	 * @return MyJsonList that represents all the entities within a room
	 */
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

	/**
	 * A Method that creates and returns MyJsonObject of non stationary object
	 * 
	 * Uses helper methods to return depending on type of non stationary
	 * @param e nonstationary object
	 * @return MyJsonObject
	 */
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
	
	/**
	 * Returns a MyJsonObject of a character
	 * 
	 * @param e character entity
	 * @return MyJsonObject represents a character in the game
	 */
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
	
	/**
	 * Returns MyJsonObject of a player
	 * 
	 * @param e player entity to be saved
	 * 
	 * @return MyJsonObject of player
	 */
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
	/**
	 * Returns a MyJsonObject of a entity that can be picked up 
	 * Uses helper methods depending on the object
	 * 
	 * @param e entity to be saved
	 * @return MyJsonObject of entity
	 */
	
	private MyJsonObject AddPickup(Pickup e) {
		if(e instanceof Key){
			return AddKey((Key) e);
		}
		else{
			//no other type of pickup in game yet
			return null;
		}
	}

	/**
	 * Returns a MyJsonObject of a key in the game
	 * 
	 * @param e key to be saved
	 * @return MyJsonOnject of key
	 */
	private MyJsonObject AddKey(Key e) {
		MyJsonObject key = new MyJsonObject();
		key.put("doorid", e.getExit().getID());
		key.put("id", e.getID());
		key.put("description", e.getDescription());
		MyJsonList positionints = new MyJsonList();
		positionints.add(e.getPosition().getX());
		positionints.add(e.getPosition().getY());
		key.put("position",positionints);
		return key;
	}
	
	/**
	 * Returns MyJsonObject that is to be added to list of doors
	 * 
	 * @param e door to be added
	 * @return MyJsonObject of door
	 */
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
	
	/**
	 * Method for returning a MyJsonObject of stationary object
	 * 
	 * @param e Stationary entity
	 * @return MyJsonObject of stationary entity
	 */
	private MyJsonObject addStationary(Entity e) {
		// I don't think we have anything that implements a stationary yet? 
		return null;
	}

}
