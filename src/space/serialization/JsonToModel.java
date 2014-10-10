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
		MyJsonList roomJsonObjects = json.get("rooms");
		for(int i = 0; i<roomJsonObjects.getSize(); i++){
			MyJsonObject r = roomJsonObjects.get(i);
			rooms.add(loadRoom(r));
		}

		MyJsonList doorJsonObjects = json.get("doors");
		for(int i = 0; i<doorJsonObjects.getSize(); i++){
			MyJsonObject d = doorJsonObjects.get(i);
			doors.add(loadDoor(d));
		}

		for(int k=0;k<roomJsonObjects.getSize();k++){
			MyJsonObject r=roomJsonObjects.get(k);
			MyJsonList walls = r.getMyJsonList("walls and door ids");
			Room toPlaceDoor = null;
			for(Room rm : rooms){
				if (rm.getID()==r.getint("id")){
					toPlaceDoor = rm;
				}
			}

			for(int i=0; i<walls.getSize(); i++){
				MyJsonObject wall = walls.get(i);
				String key = wall.getName();
				MyJsonList drs = wall.get(key);
				for(int j = 0; j<drs.getSize(); j++){
					int doorid = drs.get(j);
					Door doorToAdd = null;
					for(Door d : doors){
						if(d.getID()==doorid){
							doorToAdd = d;
						}
					}
					toPlaceDoor.addDoor(Integer.parseInt(key), doorToAdd);


				}


			}


		}

		world = new World(entities, rooms);
	}

	private Door loadDoor(MyJsonObject d) {
		int id = Integer.parseInt(d.get("id").toString());
		Vector2D position =loadPoint(d.getMyJsonList("position"));
		Room room1 = null;
		Room room2 = null;
		for(Room r: rooms){
			if(d.getint("room1")==r.getID()){
				room1=r;
			}
			if(d.getint("room2")==r.getID()){
				room2=r;
			}
		}
		String description = d.getString("description");
		String name = d.getString("name");
		String state = d.getString("state");
		boolean isOneWay = d.getboolean("is oneway");
		float amtOpen = d.getfloat("amt open");
		boolean isLocked = d.getboolean("isLocked");
		Key key=null;
		for(Entity e : entities){
			if(e instanceof Key){
				if(e.getID()==d.getint("key")){
					key=(Key) e;
				}
			}
		}
		return new Door(position, id, description,name,room1, room2, isOneWay, isLocked,key,state,amtOpen);
	}

	private Room loadRoom(MyJsonObject r) {
		LightMode lightmode = LightMode.valueOf(r.getString("Light Mode"));
		int id = Integer.parseInt(r.get("id").toString());//why this? find out how to fix this not in a round about way
		String description = r.getString("description");
		List<Vector2D> points = new ArrayList<Vector2D>();
		MyJsonObject RoomShape = r.get("Room Shape");
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
			MyJsonObject e = contains.get(i);
			 if(e.get("name")=="Key"){
				 Key key = loadKey(e);
				 contained.add(key);
				 entities.add(key);
			 }
			 else if(e.get("name")=="Light"){
				 Light light = loadLight(e);
				 contained.add(light);
				 entities.add(light);
			 }
			 else if(e.get("name")=="Button"){
				 Button button = loadButton(e);
				 contained.add(button);
				 entities.add(button);
			 }
			 else if(e.get("name")=="Teleporter"){
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
		int id = e.getint("id");
		String description = e.getString("description");
		float elevation = e.getfloat("elevation");
		String name = e.getString("name");
		
		return new Teleporter(position,teleportstoPos, id, elevation, description, name);
	}
	
	public Vector2D loadPoint(MyJsonList e){
		return new Vector2D((float)(e.getdouble(0)),(float)(e.getdouble(1)));
	}
	
	private Button loadButton(MyJsonObject e) {
		Vector2D position = loadPoint(e.getMyJsonList("position"));
		int id = e.getint("id");
		String description = e.getString("description");
		float elevation = e.getfloat("elevation");
		String name = e.getString("name");
		
		return new Button(position, id, elevation, description, name);
	}

	private Light loadLight(MyJsonObject e) {
		Vector2D position = loadPoint(e.getMyJsonList("position"));
		int id = e.getint("id");
		String description = e.getString("description");
		float elevation = e.getfloat("elevation");
		String name = e.getString("name");
		
		return new Light(position, id, elevation, description, name);
	}

	private Key loadKey(MyJsonObject e) {
		Vector2D position = loadPoint(e.getMyJsonList("position"));
		int id = e.getint("id");
		String description = e.getString("description");
		float elevation = e.getfloat("elevation");
		String name = e.getString("name");
		
		return new Key(position, id, elevation, description, name);
	}
	
	private Player loadPlayer(MyJsonObject o){
		Vector2D position = loadPoint(o.getMyJsonList("position"));
		int id = o.getint("id");
		String name = o.getString("name");
		Player p = new Player(position,id,name);
		p.setPoints(o.getint("points"));
		p.setXRotation(o.getfloat("x rotation"));
		p.setYRotation(o.getfloat("y rotation"));
		return p;
	}

	@Override
	public List<Player> getPlayers() {
		ArrayList<Player> ps = new ArrayList<Player>();
		MyJsonList players = json.get("players");
		//for(JSONObject o : players){
		for(int i=0;i<players.getSize();i++){
			MyJsonObject o = players.get(i);
			ps.add(loadPlayer(o));
		}

		return ps;

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
