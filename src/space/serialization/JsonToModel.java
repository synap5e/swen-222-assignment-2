package space.serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
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
import space.network.storage.WorldLoader;
import space.world.Button;
import space.world.Container;
import space.world.Door;
import space.world.Entity;
import space.world.Key;
import space.world.Light;
import space.world.Player;
import space.world.Room;
import space.world.Teleporter;
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
		double room1wall = d.getNumber("room1 wall");
		double room2wall = d.getNumber("room2 wall");
		String description = d.getString("description");
		String name = d.getString("name");
		String state = d.getString("state");
		boolean isOneWay = d.getBoolean("is oneway");
		float amtOpen = (float) d.getNumber("amt open");
		boolean isLocked = d.getBoolean("isLocked");
		Key key=null;
		for(Entity e : entities){
			if(e instanceof Key){
				if(e.getID()==d.getNumber("key")){
					key=(Key) e;
				}
			}
		}
		Door doorObject = new Door(position, (int) id, description,name,room1, room2, isOneWay, isLocked,key,state,amtOpen);
		room1.addDoor((int) room1wall, doorObject);
		room2.addDoor((int) room2wall, doorObject);
		return doorObject;
		
	}
	
	private Room loadRoom(MyJsonObject r) {
		LightMode lightmode = LightMode.valueOf(r.getString("Light Mode"));
		int id = (int)r.getNumber("id");
		String description = r.getString("description");
		List<Vector2D> points = new ArrayList<Vector2D>();
		MyJsonObject RoomShape = r.getMyJsonObject("Room Shape");
		MyJsonList pointFloats = RoomShape.getMyJsonList("points");
		for(int i = 0; i<pointFloats.getSize();i++){
			points.add(new Vector2D(loadPoint(pointFloats.getMyJsonList(i))));
		}
		
		Room rm = new Room(lightmode, id, description, points);
		Set<Entity>entitiesInRoom = loadEntities(r.getMyJsonList("contains"));
		for (Entity e: entitiesInRoom){
			rm.putInRoom(e);
		}
		return rm;
	}
	
	private Set<Entity> loadEntities(MyJsonList contains) {
		Set<Entity> contained = new HashSet<Entity>();
		for(int i=0;i<contains.getSize();i++){
			MyJsonObject e = contains.getMyJsonObject(i);
			 if(e.getString("name")=="Key"){
				 Key key = loadKey(e);
				 contained.add(key);
				 entities.add(key);
			 }
			 else if(e.getString("name")=="Light"){
				 Light light = loadLight(e);
				 contained.add(light);
				 entities.add(light);
			 }
			 else if(e.getString("name")=="Button"){
				 Button button = loadButton(e);
				 contained.add(button);
				 entities.add(button);
			 }
			 else if(e.getString("name")=="Teleporter"){
				 Teleporter teleporter = loadTeleporter(e);
				 contained.add(teleporter);
				 entities.add(teleporter);
			 }
		 }
		return contained;
	}
	
	private Teleporter loadTeleporter(MyJsonObject e) {
		Vector2D position = loadPoint(e.getMyJsonList("position"));
		Vector2D teleportstoPos = loadPoint(e.getMyJsonList("teleportstoPos"));
		int id = (int) e.getNumber("id");
		String description = e.getString("description");
		float elevation = (float) e.getNumber("elevation");
		String name = e.getString("name");
		
		return new Teleporter(position,teleportstoPos, id, elevation, description, name);
	}

	private Button loadButton(MyJsonObject e) {
		Vector2D position = loadPoint(e.getMyJsonList("position"));
		int id = (int) e.getNumber("id");
		String description = e.getString("description");
		float elevation = (float) e.getNumber("elevation");
		String name = e.getString("name");
		
		return new Button(position, id, elevation, description, name);
	}

	private Light loadLight(MyJsonObject e) {
		Vector2D position = loadPoint(e.getMyJsonList("position"));
		int id = (int) e.getNumber("id");
		String description = e.getString("description");
		float elevation = (float) e.getNumber("elevation");
		String name = e.getString("name");
		
		return new Light(position, id, elevation, description, name);
	}

	private Key loadKey(MyJsonObject e) {
		Vector2D position = loadPoint(e.getMyJsonList("position"));
		int id = (int) e.getNumber("id");
		String description = e.getString("description");
		float elevation = (float) e.getNumber("elevation");
		String name = e.getString("name");
		
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