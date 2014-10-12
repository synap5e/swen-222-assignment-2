package space.serialization;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import space.math.ConcaveHull;
import space.math.Vector2D;
import space.math.Vector3D;
import space.network.storage.WorldSaver;
import space.world.Button;
import space.world.Chest;
import space.world.Container;
import space.world.Door;
import space.world.Entity;
import space.world.Key;
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


	MyJsonList listOfRooms = new MyJsonList();
	MyJsonList listOfPlayers = new MyJsonList();
	MyJsonList listofDoors = new MyJsonList();
	MyJsonList listOfKeys = new MyJsonList();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveWorld(String savePath, World world, List<Player> players) {

		MyJsonObject fileobject = constructJsonObj(world, players);
		FileWriter file = null;
		try {
			file = new FileWriter(savePath + ".json");
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

	
	@Override
	public String representWorldAsString(World world) {
		MyJsonObject stringobject = constructJsonObj(world, new ArrayList<Player>());
		return stringobject.toString();
	}
	
	
	//CONSTRUCT METHODS
	
	private MyJsonObject constructJsonObj(World world, List<Player> players){
		MyJsonObject fileobject = new MyJsonObject();
		Map<Integer, Room> rooms = world.getRooms();
		for (Entry<Integer, Room> entry : rooms.entrySet()) {
			listOfRooms.add(constructRoom(entry.getKey(), entry.getValue()));
		}
		fileobject.put("rooms", listOfRooms);

		for (Player p : players) {
			listOfPlayers.add(constructEntity(p, null));
		}

		for (Room r : world.getRooms().values()) {
			Collection<List<Door>> doors = r.getDoors().values();
			for (List<Door> d : doors) {
				for (Door dr : d) {
					boolean alreadyIn = false;
					if (listofDoors.getSize() != 0) {
						for (int i = 0; i < listofDoors.getSize(); i++) {
							MyJsonObject door = listofDoors.getMyJsonObject(i);
							double one = door.getNumber("id");
							double two = dr.getID();
							if (one == two) {
								alreadyIn = true;
							}
						}
					}
					if (!alreadyIn) {
						listofDoors.add(constructEntity(dr, null));
					}
				}
			}
		}

		fileobject.put("doors", listofDoors);
		fileobject.put("players", listOfPlayers);
		fileobject.put("keys", listOfKeys);
		return fileobject;

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
		r.put("Light", construct3DVector(room.getLight()));
		r.put("description", room.getDescription());
		r.put("Room Shape", constructRoomShape(room.getRoomShape()));
		r.put("contains", constructContains(room));
		r.put("door ids", constructDoorIds(room.getAllDoors()));
		return r;
	}
	
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

	private MyJsonList constructDoorIds(List<Door> doorsList) {
		MyJsonList doorids = new MyJsonList();
		for (Door d : doorsList) {
			doorids.add(d.getID());
		}
		return doorids;
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
			if (e instanceof Player) {
				listOfPlayers.add(constructEntity(e, null));
			}
			else if (e instanceof Key) {
				listOfKeys.add(constructEntity(e, null));
				MyJsonObject key = new MyJsonObject();
				key.put("type", "Key");
				key.put("keyId", e.getID());
			} 
			else {
				entitiesInRoom.add(constructEntity(e, room));
			}

		}
		return entitiesInRoom;
	}

	private MyJsonObject constructEntity(Entity e, Room room) {
		MyJsonObject object = new MyJsonObject();
		object.put("type", e.getClass().getSimpleName());
		object.put("position", constructPoint(e.getPosition()));
		object.put("id", e.getID());
		object.put("elevation", e.getElevation());
		object.put("description", e.getDescription());
		object.put("name", e.getName());
		if (e instanceof Teleporter) {
			addFields((Teleporter) e, object);
		} else if (e instanceof Character) {
			addFields((Character) e, object);
		} else if (e instanceof Container) {
			addFields((Container) e, object);
		} else if (e instanceof Door) {
			addFields((Door) e, object);
		} 
		else if (e instanceof Button) {
			addFields((Button) e, object, room);
		}
		return object;
	}

	private MyJsonList constructHeldItems(List<Pickup> itemsHeld) {
		MyJsonList items = new MyJsonList();
		for (Pickup p : itemsHeld) {
			if (p instanceof Key) {
				listOfKeys.add(constructEntity((Entity) p, null));
				MyJsonObject key = new MyJsonObject();
				key.put("type", "Key");
				key.put("keyId", ((Key) p).getID());
				items.add(key);
			} 
			else
			{
				items.add(constructEntity((Entity) p, null));
			}
		}
		return items;
	}


	
	
	//ADD FIELDS METHODS
	private void addFields(Button e, MyJsonObject object, Room room) {
		object.put("entityId", e.getEntity().getID());
		object.put("roomButtonIsIn", room.getID());

	}

	private void addFields(Door door, MyJsonObject object) {
		object.put("room1", door.getRoom1().getID());
		Room room1 = door.getRoom1();
		int room1Wall = 0;
		Map<Integer, List<Door>> wallstodoors = room1.getDoors();
		for (Entry<Integer, List<Door>> entry : wallstodoors.entrySet()) {

			for (Door d : entry.getValue()) {
				if (d.equals(door)) {
					room1Wall = entry.getKey();
				}
			}
		}
		object.put("room1Wall", room1Wall);
		object.put("room2", door.getRoom2().getID());
		Room room2 = door.getRoom2();
		int room2Wall = 0;
		Map<Integer, List<Door>> wallstodoors2 = room2.getDoors();
		for (Entry<Integer, List<Door>> entry : wallstodoors2.entrySet()) {

			for (Door d : entry.getValue()) {
				if (d.equals(door)) {
					room2Wall = entry.getKey();
				}
			}
		}
		object.put("room2Wall", room2Wall);
		object.put("isOneWay", door.isOneWay());
		if(door.getKey()!=null){
			object.put("key", door.getKey().getID());
		}
		else{
			object.put("key", -1);
		}
		object.put("state", door.getState());
		object.put("locked", door.isLocked());
		object.put("canInteract", door.canInteract());
		object.put("amtOpen", door.getOpenPercent());
	}

	private void addFields(Container e, MyJsonObject object) {
		object.put("isLocked", e.isLocked());
		object.put("isOpen", e.isOpen());
		object.put("itemsContained", constructHeldItems(e.getItemsContained()));
		if (e instanceof Chest) {
			if(e.getKey()!=null){
			object.put("keyId", e.getKey().getID());
			}
			else {
				object.put("keyId", -1);
			}
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
			object.put("torchOn", p.isTorchOn());
		}

	}
	
	private void addFields(Teleporter e, MyJsonObject object) {
		object.put("teleportstoPos", constructPoint(e.getTeleportsTo()));
		object.put("canInteract", e.canInteract());
	}
	
	
	
	
	//UTILITY METHODS
	private MyJsonList constructPoint(Vector2D v) {
		MyJsonList pos = new MyJsonList();
		pos.add(v.getX());
		pos.add(v.getY());
		return pos;
	}

	private MyJsonList construct3DVector(Vector3D v) {
		MyJsonList vector = new MyJsonList();
		vector.add(v.getX());
		vector.add(v.getY());
		vector.add(v.getZ());
		return vector;
	}

}
