package space.serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.math.ConcaveHull;
import space.math.Vector2D;
import space.math.Vector3D;
import space.network.storage.WorldLoader;
import space.world.Button;
import space.world.Chest;
import space.world.Container;
import space.world.Door;
import space.world.Entity;
import space.world.Key;
import space.world.Light;
import space.world.Pickup;
import space.world.Player;
import space.world.Room;
import space.world.Teleporter;
import space.world.Wallet;
import space.world.World;

public class JsonToModel implements WorldLoader{

	private World world;
	MyJsonObject json;
	Set<Entity> entities = new HashSet<Entity>();
	Set<Room> rooms = new HashSet<Room>();
	List<Door> doors = new ArrayList<Door>();

	@Override
	public void loadWorld(String savePath) {

		JSONParser  p = new JSONParser();
		try {
			json = new MyJsonObject((JSONObject) p.parse(new FileReader(new File(savePath+".json"))));
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (ParseException e) {
			System.out.println(e);
		}
		
		MyJsonList roomJsonObjects = json.getMyJsonList("rooms");
		for(int i = 0; i<roomJsonObjects.getSize(); i++){
			MyJsonObject r = roomJsonObjects.getMyJsonObject(i);
			rooms.add(loadRoom(r));
		}
		
		MyJsonList doorJsonObjects = json.getMyJsonList("doors");
		for(int i = 0; i<doorJsonObjects.getSize(); i++){
			MyJsonObject d = doorJsonObjects.getMyJsonObject(i);
			doors.add(loadDoor(d));
		}
		

		world = new World(entities, rooms);
	}
	
	private Door loadDoor(MyJsonObject d) {
		
		double id = d.getNumber("id");
		Vector2D position =loadPoint(d.getMyJsonList("position"));
		Room room1 = null;
		Room room2 = null;
		for(Room r: rooms){
			
			if(d.getNumber("room1")==(double)r.getID()){
				room1=r;
			}
			if(d.getNumber("room2")==(double)r.getID()){
				room2=r;
			}
			
		}
		double room1wall = d.getNumber("room1Wall");
		double room2wall = d.getNumber("room2Wall");
		String description = d.getString("description");
		String name = d.getString("name");
		String state = d.getString("state");
		boolean isOneWay = d.getBoolean("isOneway");
		float amtOpen = (float) d.getNumber("amtOpen");
		boolean isLocked = d.getBoolean("isLocked");
		boolean canInteract = d.getBoolean("canInteract");
		Key key=null;
		for(Entity e : entities){
			if(e instanceof Key){
				if(e.getID()==d.getNumber("key")){
					key=(Key) e;
				}
			}
		}
		//THIS IS GOING TO BREAK
		Door doorObject = new Door(position, (int) id, description,name,room1, room2, isOneWay, isLocked,key,canInteract, state,amtOpen);
		room1.addDoor((int) room1wall, doorObject);
		room2.addDoor((int) room2wall, doorObject);
		return doorObject;
		
	}
	
	private Room loadRoom(MyJsonObject r) {
		Vector3D light = loadVector3D(r.getMyJsonList("Light"));
		int id = (int)r.getNumber("id");
		String description = r.getString("description");
		List<Vector2D> points = new ArrayList<Vector2D>();
		MyJsonObject RoomShape = r.getMyJsonObject("Room Shape");
		MyJsonList pointFloats = RoomShape.getMyJsonList("points");
		for(int i = 0; i<pointFloats.getSize();i++){
			points.add(new Vector2D(loadPoint(pointFloats.getMyJsonList(i))));
		}
		
		Room rm = new Room(light, id, description, points);
		Set<Entity>entitiesInRoom = loadEntities(r.getMyJsonList("contains"));
		for (Entity e: entitiesInRoom){
			rm.putInRoom(e);
		}
		return rm;
	}
	
	private Vector3D loadVector3D(MyJsonList e) {
		return new Vector3D((float)(e.getNumber(0)),(float)(e.getNumber(1)),(float)(e.getNumber(2)));
	}

	private Set<Entity> loadEntities(MyJsonList contains) {
		Set<Entity> contained = new HashSet<Entity>();
		for(int i=0;i<contains.getSize();i++){
			MyJsonObject e = contains.getMyJsonObject(i);
			int id = (int) e.getNumber("id");
			Vector2D position = loadPoint(e.getMyJsonList("position"));
			float elevation = (float) e.getNumber("elevation");
			String description = e.getString("description");
			String name = e.getString("name");
			 if(e.getString("name").equals("Key")){
				 Key key = loadKey(id,position,elevation,description,name);
				 contained.add(key);
				 entities.add(key);
			 }
			 //GONNA HAVE TO LOAD KEYS FIRST
			 else if(e.getString("name").equals("Light")){
				 Light light = loadLight(id,position,elevation,description,name);
				 contained.add(light);
				 entities.add(light);
			 }
			 else if(e.getString("name").equals("Button")){
				 Button button = loadButton(e,id,position,elevation,description,name);
				 contained.add(button);
				 entities.add(button);
			 }
			 //GONNA HAVE TO LOAD BUTTONS LATER
			 else if(e.getString("name").equals("Teleporter")){
				 Teleporter teleporter = loadTeleporter(e,id,position,elevation,description,name);
				 contained.add(teleporter);
				 entities.add(teleporter);
			 }
			 else if (e.getString("name").equals("Wallet")||e.getString("name").equals("Chest")){
				 Container container = loadContainer(e, id,position,elevation,description,name);
				 contained.add(container);
				 entities.add(container);
			 }
		 }
		return contained;
	}
	
	private Container loadContainer(MyJsonObject o, int id, Vector2D position, float elevation,String description, String name) {
		Set<Entity> items = loadEntities(o.getMyJsonList("itemsContained"));
		List<Pickup> contained = new ArrayList<Pickup>();
			for(Entity e:items){
				contained.add((Pickup) e);
			}
		boolean isOpen = o.getBoolean("isOpen");
		boolean isLocked = o.getBoolean("isLocked");
		if(o.getString("name").equals("Wallet")){
			return new Wallet(position, id, elevation, name, name);
		}
		else if(o.getString("name").equals("Chest")){
			return new Chest(position, id, elevation, description, name, isLocked, null);
		}
		else return null;
		
	}

	
	private Teleporter loadTeleporter(MyJsonObject e, int id, Vector2D position, float elevation, String description, String name) {
		Vector2D teleportstoPos = loadPoint(e.getMyJsonList("teleportstoPos"));
		boolean canInteract = e.getBoolean("canInteract");
		
		return new Teleporter(position,teleportstoPos, id, elevation, description, name, canInteract);
	}

	private Button loadButton(MyJsonObject e, int id, Vector2D position, float elevation, String description, String name) {
		double entityId = e.getNumber("entity");
		return new Button(position, id, elevation, description, name, null);
	}

	private Light loadLight(int id, Vector2D position, float elevation, String description, String name) {
		
		return new Light(position, id, elevation, description, name);
	}

	private Key loadKey(int id, Vector2D position, float elevation, String description, String name) {	
		return new Key(position, id, elevation, description, name);
	}

	
	@Override
	public List<Player> getPlayers() {
		ArrayList<Player> ps = new ArrayList<Player>();
		MyJsonList players = json.getMyJsonList("players");
		for(int i=0;i<players.getSize();i++){
			MyJsonObject o = players.getMyJsonObject(i);
			ps.add(loadPlayer(o));
		}
		for(Player p:ps){
			System.out.println(p.getID());
		}
		return ps;

	}
	
	private Player loadPlayer(MyJsonObject o){
		Vector2D position = loadPoint(o.getMyJsonList("position"));
		int id = (int) o.getNumber("id");
		String name = o.getString("name");
		Player p = new Player(position,id,name);
		p.setPoints((int) o.getNumber("points"));
		p.setXRotation((float) o.getNumber("x rotation"));
		p.setYRotation((float) o.getNumber("y rotation"));
		return p;
	}
	
	public Vector2D loadPoint(MyJsonList e){
		return new Vector2D((float)(e.getNumber(0)),(float)(e.getNumber(1)));
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public void loadWorldFromString(String world) {
		// TODO Auto-generated method stub
		
	}

}