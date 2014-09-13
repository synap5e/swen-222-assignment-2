package space.world;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import space.gui.pipeline.viewable.ViewableObject;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.util.Vec2;
import space.world.items.Item;

public class Room implements ViewableRoom{
	private LightMode mode;
	private int id;
	private String description;
	private Polygon roomShape;
	private Set<Item> items = new HashSet<Item>();
	private Set<Player> players = new HashSet<Player>();
	private Map<Room,Exit> exits = new HashMap<Room,Exit>();
	//probably need to add something about room exits
	
	public Room(LightMode m, int i, String d, Polygon r){
		mode = m;
		id = i;
		description = d;
		roomShape = r;
	}
	
	public Room(LightMode m, int i, String d, List<Vec2> points){
		mode = m;
		id = i;
		description = d;
		setUpPolygon(points);
	}

	@Override
	public Vec2 getCentre() {
		float x = (float) roomShape.getBounds().getCenterX();
		float y = (float) roomShape.getBounds().getCenterY();
		return new Vec2(x,y);
	}

	@Override
	public List<? extends ViewableWall> getWalls() {
		List<Wall> walls = new ArrayList<Wall>();
		Vec2 firstPoint = new Vec2(roomShape.xpoints[0],roomShape.ypoints[0]);
		Vec2 previousPoint = firstPoint;
		for(int i = 1; i < roomShape.npoints; i++){
			Vec2 currentPoint = new Vec2(roomShape.xpoints[i],roomShape.ypoints[i]);
			walls.add(new Wall(previousPoint,currentPoint));
			previousPoint = currentPoint;
		}
		walls.add(new Wall(previousPoint,firstPoint));
		return walls;
	}
	
	public boolean pointInRoom(Vec2 point){
		return roomShape.contains(point.getX(), point.getY());
	}
	
	public void addExit(Exit e){
		if(e.getRoom1().equals(this)){
			exits.put(e.getRoom2(), e);
		}
		else if(e.getRoom2().equals(this) && !e.isOneWay()){
			exits.put(e.getRoom1(), e);
		}
	}
	
	public Exit getExitTo(Room other){
		return exits.get(other);
	}
	
	public void putInRoom(Item i){
		items.add(i);
	}
	
	public void removeFromRoom(Item i){
		items.remove(i);
	}
	
	public void enterRoom(Player p){
		players.add(p);
	}
	
	public void leaveRoom(Player p){
		players.remove(p);
	}
	public String getDescription() {
		return description;
	}

	public int getID() {
		return id;
	}

	@Override
	public LightMode getLightMode() {
		return mode;
	}
	
	@Override
	public List<? extends ViewableObject> getContainedObjects() {
		return new ArrayList<Item>(items);
	}
	
	private void setUpPolygon(List<Vec2> points){
		roomShape = new Polygon();
		for(Vec2 p : points){
			roomShape.addPoint((int)p.getX(), (int)p.getY());
		}
	}
	
	private class Wall implements ViewableWall{
		private Vec2 start;
		private Vec2 end;
		
		public Wall(Vec2 s, Vec2 e){
			start = s;
			end = e;
		}
		@Override
		public Vec2 getStart() {
			return start;
		}

		@Override
		public Vec2 getEnd() {
			return end;
		}

	}
	
}
