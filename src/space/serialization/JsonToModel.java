package space.serialization;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import space.math.Vector2D;
import space.math.Vector3D;
import space.world.Beam;
import space.world.BeamShooter;
import space.world.Bullet;
import space.world.Button;
import space.world.Chair;
import space.world.Chest;
import space.world.Container;
import space.world.Door;
import space.world.Entity;
import space.world.Key;
import space.world.Light;
import space.world.Pickup;
import space.world.Player;
import space.world.Room;
import space.world.Table;
import space.world.Teleporter;
import space.world.Turret;
import space.world.TurretStrategyImpl;
import space.world.Wallet;
import space.world.World;

public class JsonToModel implements WorldLoader {

	private World world;
	MyJsonObject jsonObj;
	Set<Entity> entities = new HashSet<Entity>();
	Set<Room> rooms = new HashSet<Room>();
	List<Key> keys = new ArrayList<Key>();
	List<Turret> turrets = new ArrayList<Turret>();
	MyJsonList buttonsJsonObjects = new MyJsonList();
	MyJsonList bulletsJsonObjects = new MyJsonList();
	MyJsonList turretsJsonObjects = new MyJsonList();
	MyJsonList beamShooterObjects = new MyJsonList();
	MyJsonList beamsJsonObjects = new MyJsonList();

	@Override
	public void loadWorld(String savePath) throws SaveFileNotAccessibleException, SaveFileNotValidException {
		JSONParser p = new JSONParser();
		try {
			jsonObj = new MyJsonObject((JSONObject) p.parse(new FileReader(new File(savePath + ".json"))));
		} catch (IOException e){
			throw new SaveFileNotAccessibleException("The save file " + savePath + " was not accessible", e);
		} catch (ParseException e) {
			throw new SaveFileNotValidException("The save file " + savePath + " was not valid", e);
		}
		
		world = createWorld(jsonObj);
	}
	private World createWorld(MyJsonObject json) {

		MyJsonList keyJsonObjects = json.getMyJsonList("keys");
		for(int i=0;i<keyJsonObjects.getSize();i++){
			MyJsonObject k = keyJsonObjects.getMyJsonObject(i);
			keys.add(loadKey(k));
		}

		MyJsonList roomJsonObjects = json.getMyJsonList("rooms");
		for (int i = 0; i < roomJsonObjects.getSize(); i++) {
			MyJsonObject r = roomJsonObjects.getMyJsonObject(i);
			rooms.add(loadRoom(r));
		}

		MyJsonList doorJsonObjects = json.getMyJsonList("doors");
		for (int i = 0; i < doorJsonObjects.getSize(); i++) {
			MyJsonObject d = doorJsonObjects.getMyJsonObject(i);
			entities.add(loadDoor(d));
		}
		
		
		for(int i=0;i<bulletsJsonObjects.getSize();i++){
			MyJsonObject b = bulletsJsonObjects.getMyJsonObject(i);
			loadBullet(b);
		}
		
		for(int i=0;i<turretsJsonObjects.getSize();i++){
			MyJsonObject t = turretsJsonObjects.getMyJsonObject(i);
			loadTurret(t);
		}
		
		for(int i=0;i<beamShooterObjects.getSize();i++){
			MyJsonObject b = beamShooterObjects.getMyJsonObject(i);
			loadBeamShooter(b);
		}
		
		for(int i=0;i<buttonsJsonObjects.getSize();i++){
			MyJsonObject b = buttonsJsonObjects.getMyJsonObject(i);
			loadButton(b);
		}
	
		for(int i=0;i<beamsJsonObjects.getSize();i++){
			MyJsonObject b = beamsJsonObjects.getMyJsonObject(i);
			Room roomIn = null;
			for(Room r :rooms){
				int roomId = (int) b.getNumber("roomBeamIsIn");
				if(r.getID()==roomId){
					roomIn = r;
				}
			}
			Beam beam = loadBeam(b);
			roomIn.addBeam(beam);
		}
		
		for (Key key : keys){
			if (!entities.contains(key)){
				entities.add(key);
			}
		}
		
		
		return new World(entities, rooms);
	}

	private void loadBeamShooter(MyJsonObject b) {
		int id = (int) b.getNumber("id");
		Vector2D position = loadPoint(b.getMyJsonList("position"));
		float elevation = (float) b.getNumber("elevation");
		String description = b.getString("description");
		String name = b.getString("name");
		Room room = null;
		Room roomIn = null;
		for(Room r : rooms){
			Double RoomId = (double) r.getID();
			if(b.getNumber("roomId")==RoomId){
				room = r;
			}
			if(b.getNumber("roomBeamShooterIsIn")==RoomId){
				roomIn = r;
			}
		}
		float yRotation = (float) b.getNumber("yRotation");
		boolean stopped = b.getBoolean("stopped");
		int beamsShot = (int) b.getNumber("beamsShot");
		Turret turret = null;
		for(Turret t : turrets){
			if(b.getNumber("turretId")==t.getID()){
				turret = t;
			}
		}
		BeamShooter bs = new BeamShooter(position,id,elevation,description,name,room,turret,yRotation,stopped,beamsShot);
		roomIn.putInRoom(bs);
		entities.add(bs);
	}

	private void loadTurret(MyJsonObject t) {
		int id = (int) t.getNumber("id");
		Vector2D position = loadPoint(t.getMyJsonList("position"));
		float elevation = (float) t.getNumber("elevation");
		String description = t.getString("description");
		String name = t.getString("name");
		Room room = null;
		for(Room r : rooms){
			Double RoomId = (double) r.getID();
			if(t.getNumber("roomId")==RoomId){
				room = r;
			}
		}
		
		Turret turret = new Turret(position, id, elevation,description,name,room);
		boolean shutDown = t.getBoolean("shutDown");
		TurretStrategyImpl ts = createTurretStrategyImpl(t.getMyJsonList("strategy"), turret);
		turret.setStrategy(ts);
		turrets.add(turret);
		if(shutDown){
			turret.shutDown();
		}
		else{
		room.putInRoom(turret);
		entities.add(turret);
		}
		
		
	}

	private TurretStrategyImpl createTurretStrategyImpl(MyJsonList strategy, Turret turret) {
		float yRot = (float) strategy.getNumber(0);
		int  bulletsShot = (int) strategy.getNumber(1);
		Vector2D teleportTo = loadPoint(strategy.getMyJsonList(2));
		int roomId = (int) strategy.getNumber(3);
		Room telRoom = null;
		for(Room r:rooms){
			if(roomId==r.getID()){
				telRoom = r;
			}
		}
		return new TurretStrategyImpl(turret,yRot,bulletsShot,teleportTo,telRoom);
	}

	private void loadBullet(MyJsonObject b) {
		int id = (int) b.getNumber("id");
		Vector2D position = loadPoint(b.getMyJsonList("position"));
		float elevation = (float) b.getNumber("elevation");
		Vector3D velocity = loadVector3D(b.getMyJsonList("velocity"));
		Vector2D teleportTo = loadPoint(b.getMyJsonList("teleportTo"));
		Room room = null;
		Room roomTel = null;
		for(Room r:rooms){
			Double RoomId = (double) r.getID();
			if(b.getNumber("roomId")==RoomId){
				room=r;
			}
			if(b.getNumber("roomTeleportTo")==RoomId){
				roomTel = r;
			}
		}
		Bullet bullet = new Bullet(position,id,elevation,room,velocity,teleportTo,roomTel);
		room.putInRoom(bullet);
		entities.add(bullet);
	}

	@Override
	public List<Player> getPlayers() {
		ArrayList<Player> ps = new ArrayList<Player>();
		MyJsonList players = jsonObj.getMyJsonList("players");
		for (int i = 0; i < players.getSize(); i++) {
			MyJsonObject o = players.getMyJsonObject(i);
			ps.add(loadPlayer(o));
		}
		return ps;

	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public void loadWorldFromString(String wrld) throws SaveFileNotValidException{
		JSONParser p = new JSONParser();
			try {
				jsonObj = new MyJsonObject((JSONObject) p.parse(wrld));
			} catch (ParseException e) {
				throw new SaveFileNotValidException("The string was not formatted correctly", e);
			}		
		world = createWorld(jsonObj);

	}
	
	//LOAD OBJECT METHODS

	private Room loadRoom(MyJsonObject r) {
		Vector3D light = loadVector3D(r.getMyJsonList("Light"));
		int id = (int) r.getNumber("id");
		String description = r.getString("description");
		List<Vector2D> points = new ArrayList<Vector2D>();
		MyJsonObject RoomShape = r.getMyJsonObject("Room Shape");
		MyJsonList pointFloats = RoomShape.getMyJsonList("points");
		for (int i = 0; i < pointFloats.getSize(); i++) {
			points.add(new Vector2D(loadPoint(pointFloats.getMyJsonList(i))));
		}

		Room rm = new Room(light, id, description, points);
		Set<Entity> entitiesInRoom = loadEntities(r.getMyJsonList("contains"));
		for (Entity e : entitiesInRoom) {
			rm.putInRoom(e);
		}
		for(int i=0;i<r.getMyJsonList("beams").getSize();i++){
			beamsJsonObjects.add(r.getMyJsonList("beams").getMyJsonObject(i));
		}
		return rm;
	}

	private Beam loadBeam(MyJsonObject beam) {


		Vector2D position = loadPoint(beam.getMyJsonList("position"));
		int id = (int) beam.getNumber("id");
		float elevation = (float) beam.getNumber("elevation");
		Vector3D beamDirection = loadVector3D(beam.getMyJsonList("beamDirection"));
		Turret turret = null;
		for(Turret t:turrets){
			if(t.getID()==(int)beam.getNumber("turret")){
				turret = t;
			}
		}
		return new Beam(position,id,elevation,beamDirection,turret);


	}

	private Door loadDoor(MyJsonObject d) {
		double id = d.getNumber("id");
		Vector2D position = loadPoint(d.getMyJsonList("position"));
		Room room1 = null;
		Room room2 = null;
		for (Room r : rooms) {

			if (d.getNumber("room1") == (double) r.getID()) {
				room1 = r;
			}
			if (d.getNumber("room2") == (double) r.getID()) {
				room2 = r;
			}

		}
		double room1wall = d.getNumber("room1Wall");
		double room2wall = d.getNumber("room2Wall");
		String description = d.getString("description");
		String name = d.getString("name");
		String state = d.getString("state");
		boolean isOneWay = d.getBoolean("isOneWay");
		float amtOpen = (float) d.getNumber("amtOpen");
		boolean isLocked = d.getBoolean("locked");
		boolean canInteract = d.getBoolean("canInteract");
		Key key = null;
		for (Key k : keys) {
			if (k.getID() == d.getNumber("key")) {
				key = k;
			}
		}
		Door doorObject = new Door(position, (int) id, description, name,room1, room2, isOneWay, isLocked, key, canInteract, state,amtOpen);
		room1.addDoor((int) room1wall, doorObject);
		room2.addDoor((int) room2wall, doorObject);
		return doorObject;

	}

	private Player loadPlayer(MyJsonObject o) {
		Vector2D position = loadPoint(o.getMyJsonList("position"));
		int id = (int) o.getNumber("id");
		String name = o.getString("name");
		Player p = new Player(position, id, name);
		p.setPoints((int) o.getNumber("points"));
		p.setXRotation((float) o.getNumber("x rotation"));
		p.setYRotation((float) o.getNumber("y rotation"));
		p.setTorch(o.getBoolean("torchOn"));
		Set<Entity> items = loadEntities(o.getMyJsonList("inventory"));
		for (Entity e : items) {
			p.pickup(e);
		}
		return p;
	}

	private Set<Entity> loadEntities(MyJsonList contains) {
		Set<Entity> contained = new HashSet<Entity>();
		for (int i = 0; i < contains.getSize(); i++) {
			MyJsonObject e = contains.getMyJsonObject(i);
			
			String type = e.getString("type");
			if (type.equals("Key")) {
				Key ky = null;
				for(Key k : keys){
					if((int)e.getNumber("keyId")==(k.getID())){
						ky=k;
						contained.add(ky);
						entities.add(ky);
					}
				}
				
			}
			else{
				String name = e.getString("name");
				int id = (int) e.getNumber("id");
				Vector2D position = loadPoint(e.getMyJsonList("position"));
				float elevation = (float) e.getNumber("elevation");
				String description = e.getString("description");

				if (type.equals("Light")) {
					Light light = new Light(position, id, elevation, description, name);
					contained.add(light);
					entities.add(light);
				} 
				else if (type.equals("Button")) {
					buttonsJsonObjects.add(e);
				}
				else if (type.equals("Teleporter")) {
					Teleporter teleporter = loadTeleporter(e, id, position,elevation, description, name);
					contained.add(teleporter);
					entities.add(teleporter);
				} 
				else if (type.equals("Wallet") || type.equals("Chest")) {
					Container container = loadContainer(e, id, position, elevation,description, name);
					contained.add(container);
					entities.add(container);
				}
				else if (type.equals("Chair")){
					Chair c = new Chair(position, id, elevation,description,name);
					contained.add(c);
					entities.add(c);
				}
				else if(type.equals("Table")){
					Table t = new Table(position, id, elevation,description,name);
					contained.add(t);
					entities.add(t);
				}
				else if (type.equals("Bullet")){
					bulletsJsonObjects.add(e);
				}
				else if (type.equals("Turret")){
					turretsJsonObjects.add(e);
				}
				else if (type.equals("BeamShooter")){
					beamShooterObjects.add(e);
				}
				
			}
		}
		return contained;

	}
	
	private Container loadContainer(MyJsonObject o, int id, Vector2D position,float elevation, String description, String name) {
		Set<Entity> items = loadEntities(o.getMyJsonList("itemsContained"));
		Set<Pickup> pickup = new HashSet<Pickup>(); 
		for(Entity e : items){
			pickup.add((Pickup) e);
		}
		boolean isOpen = o.getBoolean("isOpen");
		boolean isLocked = o.getBoolean("isLocked");
		if (o.getString("type").equals("Wallet")) {
			Wallet w = new Wallet(position, id, elevation, name, name,isOpen,pickup);
			return w;
		} 
		else if (o.getString("type").equals("Chest")) {
			Key k = null;
				for(Key key:keys){
					Double keyid = (double) key.getID();
					if(keyid.equals(o.getNumber("keyId"))){
						k=key;
					}

				}
			
			Chest c = new Chest(position, id, elevation, description, name,isLocked,k,isOpen, pickup);
			return c;
		}
		else return null;

	}
	
	private Teleporter loadTeleporter(MyJsonObject e, int id,Vector2D position, float elevation, String description, String name) {
		Vector2D teleportstoPos = loadPoint(e.getMyJsonList("teleportstoPos"));
		boolean canInteract = e.getBoolean("canInteract");

		return new Teleporter(position, teleportstoPos, id, elevation,description, name, canInteract);
	}

	private Key loadKey(MyJsonObject k) {
		double id = k.getNumber("id");
		Vector2D position = loadPoint(k.getMyJsonList("position"));
		String description = k.getString("description");
		String name = k.getString("name");
		double elevation = k.getNumber("elevation");
		return new Key(position, (int)id, (float)elevation, description, name);
	}

	private void loadButton(MyJsonObject b) {
		int id = (int) b.getNumber("id");
		Vector2D position = loadPoint(b.getMyJsonList("position"));
		float elevation = (float) b.getNumber("elevation");
		String description = b.getString("description");
		String name = b.getString("name");
		Entity entity = null;
		for(Entity e:entities){
			Double entityId = (double) e.getID();
			if(b.getNumber("entityId")==entityId){
				entity=e;
			}
		}
		Button button = new Button(position, id, elevation, description, name, entity);
		for(Room r:rooms){
			Double roomId = (double) r.getID();
			if(roomId==b.getNumber("roomButtonIsIn")){
				r.putInRoom(button);
				entities.add(button);
			}
		}

		
	}

	//UTILITY METHODS
	private Vector2D loadPoint(MyJsonList e) {
		return new Vector2D((float) (e.getNumber(0)), (float) (e.getNumber(1)));
		}
	
	private Vector3D loadVector3D(MyJsonList e) {
		return new Vector3D((float) (e.getNumber(0)), (float) (e.getNumber(1)),(float) (e.getNumber(2)));
		
	
	}

}