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
import space.math.ConcaveHull;
import space.math.Segment2D;
import space.math.Vector2D;
import space.world.items.Item;

public class Room implements ViewableRoom{
	private LightMode mode;
	private int id;
	private String description;
	private ConcaveHull roomShape;
	private Set<Item> items = new HashSet<Item>();
	private Set<Player> players = new HashSet<Player>();
	private Map<Room,Exit> exits = new HashMap<Room,Exit>();
	//probably need to add something about room exits
	
	public Room(LightMode m, int i, String d, ConcaveHull r){
		mode = m;
		id = i;
		description = d;
		roomShape = r;
	}
	
	public Room(LightMode m, int i, String d, List<Vector2D> points){
		mode = m;
		id = i;
		description = d;
		roomShape = new ConcaveHull(points);
	}

	@Override
	public Vector2D getCentre() {
//		float x = (float) roomShape.getBounds().getCenterX();
//		float y = (float) roomShape.getBounds().getCenterY();
//		return new Vec2(x,y);
		return null;
	}

	@Override
	public List<? extends ViewableWall> getWalls() {
		List<Wall> walls = new ArrayList<Wall>();
		for(Segment2D seg : roomShape){
			walls.add(new Wall(seg));
		}
		return walls;
	}
	
	public boolean pointInRoom(Vector2D point){
		return roomShape.contains(point);
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
	
	private class Wall implements ViewableWall{
		private Segment2D lineSeg;
		
		public Wall(Segment2D ls){
			lineSeg = ls;
		}
		@Override
		public Vector2D getStart() {
			return lineSeg.start;
		}

		@Override
		public Vector2D getEnd() {
			return lineSeg.end;
		}

	}
	
}
