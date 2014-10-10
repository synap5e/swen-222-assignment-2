package space.serialization;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import space.math.ConcaveHull;
import space.math.Vector2D;
import space.network.storage.WorldSaver;
import space.world.Container;
import space.world.Door;
import space.world.Entity;
import space.world.Openable;
import space.world.Pickup;
import space.world.Character;
import space.world.Player;
import space.world.Room;
import space.world.Teleporter;
import space.world.World;

/**
 * a class that is used to save the model to a Json file that can be loaded back
 * in the game at a later stage
 * 
 * used to store the game data of the current player - and the state of the
 * world that the current player was in
 * 
 * Written like a parser - Recursive Descent
 * 
 * @author Shweta Barapatre
 *
 */

public class ModelToJson implements WorldSaver {

	MyJsonObject fileobject = new MyJsonObject();
	MyJsonList listOfRooms = new MyJsonList();
	MyJsonList listOfPlayers = new MyJsonList();
	MyJsonList listofDoors = new MyJsonList();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveWorld(String savePath, World world, List<Player> players) {


		Map<Integer, Room> rooms = world.getRooms();
		for (Entry<Integer, Room> entry : rooms.entrySet()) {
			listOfRooms.add(constructRoom(entry.getKey(), entry.getValue()));
		}
		fileobject.put("rooms", listOfRooms);
		
		for(Player p : players){
			listOfPlayers.add(constructEntity(p));
		}

		for(Room r : world.getRooms().values()){
			Collection<List<Door>> doors =  r.getDoors().values();
			for(List<Door> d : doors){
				for(Door dr: d){
					boolean alreadyIn = false;
					if(listofDoors.getSize()!=0){
						//for(JSONObject door : listofDoors){
						for (int i=0;i<listofDoors.getSize();i++){
							MyJsonObject door = listofDoors.getMyJsonObject(i);
							double one = door.getNumber("id");
							int two = dr.getID();
							if(one==two){
								alreadyIn = true;
							}
						}
					}
					if(!alreadyIn){
						listofDoors.add(constructEntity(dr));
					}
				}
			}
		}

		fileobject.put("doors", listofDoors);
		fileobject.put("players", listOfPlayers);
		
		FileWriter file = null;
		try {
			file = new FileWriter(savePath+".json");
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

	public String ModeltoString() {
		return fileobject.toString();
	}

	/**
	 * A method to create and return a MyJsonObject of a room that is passed
	 * into the method
	 * 
	 * @param roomId
	 *            the id of the room
	 * @param room
	 *            the room to be saved
	 * @return MyJsonOnject of room to be saved
	 */
	private MyJsonObject constructRoom(Integer roomId, Room room) {
		MyJsonObject r = new MyJsonObject();
		r.put("id", roomId);
		r.put("Light Mode", room.getLightMode().toString());
		r.put("description", room.getDescription());
		r.put("Room Shape", constructRoomShape(room.getRoomShape()));
		r.put("contains", constructContains(room));
		r.put("walls and door ids", constructWalls(room));
		return r;
	}

	private MyJsonList constructWalls(Room room) {
		MyJsonList walls = new MyJsonList();
		Map<Integer, List<Door>> doors = room.getDoors();
		for (Map.Entry<Integer, List<Door>> entry : doors.entrySet()) {
			MyJsonObject wall = new MyJsonObject();
			wall.put(entry.getKey().toString(),
					constructDoorIds(entry.getValue()));
			walls.add(wall);
		}
		return walls;
	}

	private MyJsonList constructDoorIds(List<Door> doorsList) {
		MyJsonList doorids = new MyJsonList();
		for (Door d : doorsList) {
			doorids.add(d.getID());
		}
		return doorids;
	}

	private MyJsonList constructPoint(Vector2D v) {
		MyJsonList pos = new MyJsonList();
		pos.add(v.getX());
		pos.add(v.getY());
		return pos;
	}

	/**
	 * Method that returns a MyJsonList of the entities with a room
	 * 
	 * @param room
	 *            room that holds the entities
	 * @return MyJsonList that represents all the entities within a room
	 */
	private MyJsonList constructContains(Room room) {
		Set<Entity> entities = room.getEntities();
		MyJsonList entitiesInRoom = new MyJsonList();
		for (Entity e : entities) {
			if ((e instanceof Player)) {
				listOfPlayers.add(constructEntity(e));
			} else {
				entitiesInRoom.add(constructEntity(e));
			}

		}
		return entitiesInRoom;
	}

	private MyJsonObject constructEntity(Entity e) {
		MyJsonObject object = new MyJsonObject();
		object.put("position", constructPoint(e.getPosition()));
		object.put("id",e.getID());
		object.put("elevation", e.getElevation());
		object.put("description", e.getDescription());
		object.put("name", e.getName());
		if (e instanceof Teleporter) {
			addFields((Teleporter) e, object);
		} else if (e instanceof Character) {
			addFields((Character) e, object);
		} else if (e instanceof Openable) {
			addFields((Openable) e, object);
		}
		return object;
	}

	private void addFields(Openable e, MyJsonObject object) {
		object.put("state", e.getState());
		object.put("isLocked", e.isLocked());
		object.put("amt open", e.getOpenPercent());
		if (e instanceof Container) {
			Container cont = (Container) e;
			object.put("items contained",
					constructHeldItems(cont.getItemsContained()));
		} else if (e instanceof Door) {
			Door door = (Door) e;
			object.put("room1", door.getRoom1().getID());
			object.put("room2", door.getRoom2().getID());
			object.put("is oneway", door.isOneWay());
			object.put("key", door.getKey().getID());
		}
	}

	private void addFields(Character e, MyJsonObject object) {
		object.put("inventory", constructHeldItems(e.getInventory()));
		object.put("room", e.getRoom().getID());
		if (e instanceof Player) {
			Player p = (Player) e;
			object.put("points", p.getPoints());
			object.put("x rotation", p.getXRotation());
			object.put("y rotation", p.getYRotation());
			object.put("torch on", p.isTorchOn());
		}

	}

	private MyJsonList constructHeldItems(List<Pickup> itemsHeld) {
		MyJsonList items = new MyJsonList();
		for (Pickup p : itemsHeld) {
			items.add(constructEntity((Entity) p));
		}
		return items;
	}

	private void addFields(Teleporter e, MyJsonObject object) {
		object.put("teleportstoPos", constructPoint(e.getTeleportsTo()));
	}

	/**
	 * Returns MyJsonObject of a player
	 * 
	 * @param e
	 *            player entity to be saved
	 * 
	 * @return MyJsonObject of player
	 */

	/**
	 * A method to create and return a MyJsonObject of a room shape
	 * 
	 * @param roomShape
	 *            the concave hull that is the shape of the room
	 * @return a MyJsonObject representation of the Convave Hull
	 */
	private MyJsonObject constructRoomShape(ConcaveHull roomShape) {
		MyJsonObject hull = new MyJsonObject();
		MyJsonList points = new MyJsonList();
		for (Vector2D v : roomShape.getDefiningPoints()) {
			MyJsonList pos = new MyJsonList();
			pos.add(v.getX());
			pos.add(v.getY());
			points.add(pos);
		}
		hull.put("points", points);
		return hull;
	}

	@Override
	public String representWorldAsString(World world) {
		// TODO Auto-generated method stub
		return null;
	}

}
