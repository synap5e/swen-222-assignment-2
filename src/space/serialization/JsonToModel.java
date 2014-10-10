package space.serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
			json = new MyJsonObject((JSONObject) p.parse(new FileReader(new File(savePath))));
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (ParseException e) {
			System.out.println(e);
		}
		MyJsonList roomJsonObjects = new MyJsonList((JSONArray) json.get("rooms"));
		for(JSONObject r : roomJsonObjects){
			rooms.add(loadRoom(r));
		}

		MyJsonList doorJsonObjects = new MyJsonList( (JSONArray) json.get("doors"));
		for(JSONObject d:doorJsonObjects){
			doors.add(loadDoor(d));
		}

		for(JSONObject r:roomJsonObjects){
			MyJsonList walls = (MyJsonList) r.get("walls and door ids");
			Room toPlaceDoor = null;
			for(Room rm : rooms){
				if (rm.getID()==(int)r.get("id")){
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

	private Door loadDoor(JSONObject d) {
		int id = (int) d.get("id");
		Vector2D position =loadPoint((MyJsonList) d.get("position"));
		Room room1 = null;
		Room room2 = null;
		for(Room r: rooms){
			if((int)d.get("room1")==r.getID()){
				room1=r;
			}
			if((int)d.get("room2")==r.getID()){
				room2=r;
			}
		}
		String description = (String) d.get("description");
		String name = (String) d.get("name");
		String state = (String) d.get("state");
		boolean isOneWay = (boolean) d.get("is oneway");
		float amtOpen = (float)d.get("amt open");
		boolean isLocked = (boolean) d.get("isLocked");
		Key key=null;
		for(Entity e : entities){
			if(e instanceof Key){
				if(e.getID()==(int)d.get("key")){
					key=(Key) e;
				}
			}
		}
		return new Door(position, id, description,name,room1, room2, isOneWay, isLocked,key,state,amtOpen);
	}

	private Room loadRoom(JSONObject r) {
		LightMode lightmode = (LightMode) r.get("Light Mode");
		int id = (int) r.get("id");
		String description = (String) r.get("description");
		List<Vector2D> points = new ArrayList<Vector2D>();
		MyJsonObject RoomShape = (MyJsonObject) r.get("RoomShape");
		MyJsonList pointFloats = (MyJsonList) RoomShape.get("points");
		for(int i = 0; i<pointFloats.getSize();i++){
			float x = pointFloats.get(i);
			i++;
			float y = pointFloats.get(i);
			points.add(new Vector2D(x,y));
		}
		
		Room rm = new Room(lightmode, id, description, points);
		Set<Entity>entitiesInRoom = loadEntities((MyJsonList)r.get("contains"));
		for (Entity e: entitiesInRoom){
			rm.putInRoom(e);
		}
		return rm;
	}

	private Set<Entity> loadEntities(MyJsonList contains) {
		Set<Entity> contained = new HashSet<Entity>();
		 for(JSONObject e: contains){
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

	private Teleporter loadTeleporter(JSONObject e) {
		Vector2D position = loadPoint((MyJsonList) e.get("position"));
		Vector2D teleportstoPos = loadPoint((MyJsonList) e.get("teleportstoPos"));
		int id = (int) e.get("id");
		String description = (String) e.get("description");
		float elevation = (float) e.get("elevation");
		String name = (String) e.get("name");
		
		return new Teleporter(position,teleportstoPos, id, elevation, description, name);
	}
	
	public Vector2D loadPoint(MyJsonList e){
		return new Vector2D((float)e.get(0),(float)e.get(1));
	}
	
	private Button loadButton(JSONObject e) {
		Vector2D position = loadPoint((MyJsonList) e.get("position"));
		int id = (int) e.get("id");
		String description = (String) e.get("description");
		float elevation = (float) e.get("elevation");
		String name = (String) e.get("name");
		
		return new Button(position, id, elevation, description, name);
	}

	private Light loadLight(JSONObject e) {
		Vector2D position = loadPoint((MyJsonList) e.get("position"));
		int id = (int) e.get("id");
		String description = (String) e.get("description");
		float elevation = (float) e.get("elevation");
		String name = (String) e.get("name");
		
		return new Light(position, id, elevation, description, name);
	}

	private Key loadKey(JSONObject e) {
		Vector2D position = loadPoint((MyJsonList) e.get("position"));
		int id = (int) e.get("id");
		String description = (String) e.get("description");
		float elevation = (float) e.get("elevation");
		String name = (String) e.get("name");
		
		return new Key(position, id, elevation, description, name);
	}
	
	private Player loadPlayer(JSONObject e){
		Vector2D position = loadPoint((MyJsonList) e.get("position"));
		int id = (int) e.get("id");
		String name = (String) e.get("name");
		Player p = new Player(position,id,name);
		p.setPoints((int) e.get("points"));
		p.setXRotation((float) e.get("x rotation"));
		p.setYRotation((float) e.get("y rotation"));
		return p;
	}

	@Override
	public List<Player> getPlayers() {
		ArrayList<Player> ps = new ArrayList<Player>();
		MyJsonList players = new MyJsonList((JSONArray) json.get("players"));
		for(JSONObject o : players){
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
