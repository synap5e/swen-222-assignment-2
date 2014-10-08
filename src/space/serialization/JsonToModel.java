package space.serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import space.math.ConcaveHull;
import space.math.Vector2D;
import space.network.storage.WorldLoader;
import space.world.Door;
import space.world.Player;
import space.world.Room;
import space.world.World;

public class JsonToModel implements WorldLoader{

	private World world;
	MyJsonObject json;

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
		MyJsonList rooms = new MyJsonList((JSONArray) json.get("rooms"));
		
		
		world = new World(null,getRooms(rooms)); 

	}

	private Set<Room> getRooms(MyJsonList rooms) {
		Set<Room> rm = new HashSet<Room>();
		for(JSONObject o:rooms){
			int id = (int) o.get("id");
			String lightMode = (String) o.get("Light Mode");
			String description = (String) o.get("description");
			MyJsonObject hull = (MyJsonObject) o.get("Room Shape");
			MyJsonList points = (MyJsonList) o.get("points");
			List<Vector2D> pts = new ArrayList<Vector2D>();
			for(int i=0; i<points.getSize(); i++){
				int x = points.get(i);
				i++;
				int y = points.get(i);
				Vector2D vector = new Vector2D(x,y);
				pts.add(vector);
			}
			ConcaveHull concaveHull = new ConcaveHull(pts);
			Map<Integer,List<Door>> doors = new HashMap<Integer,List<Door>>();
			MyJsonList walls = (MyJsonList) o.get("walls");//FINISH THIS
			

//			r.put("contains", addContains(room));
//			r.put("walls", addWall(room.getDoors()));
		}
		
		return rm;
	}

	@Override
	public List<Player> getPlayers() {
		ArrayList<Player> ps = new ArrayList<Player>();
		MyJsonList players = new MyJsonList((JSONArray) json.get("players"));
		for(JSONObject o : players){
			MyJsonList pos = new MyJsonList((JSONArray) o.get("positions"));
			Vector2D position = new Vector2D((float) pos.get(0), (float) pos.get(1));
			Player p = new Player(position, (int) o.get("id"),""); //TODO add name
			p.setPoints((int) o.get("points"));
			p.setXRotation((float) o.get("x rotation"));
			p.setYRotation((float) o.get("y rotation"));
			ps.add(p);
		}

		return ps;

	}

	@Override
	public World getWorld() {
		// TODO Auto-generated method stub
		return world;
	}

}
