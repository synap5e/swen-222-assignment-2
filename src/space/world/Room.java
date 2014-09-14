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
import space.util.Hull2;
import space.util.Segment2;
import space.util.Vec2;
import space.world.items.Item;

public class Room implements ViewableRoom{
	private LightMode mode;
	private int id;
	private String description;
	private Hull2 roomShape;
	private Set<Item> items = new HashSet<Item>();
	private Set<Player> players = new HashSet<Player>();
	private Map<Room,Exit> exits = new HashMap<Room,Exit>();
	//probably need to add something about room exits
	
	public Room(LightMode m, int i, String d, Hull2 r){
		mode = m;
		id = i;
		description = d;
		roomShape = r;
	}
	
	public Room(LightMode m, int i, String d, List<Vec2> points){
		mode = m;
		id = i;
		description = d;
		roomShape = new Hull2(points);
	}

	@Override
	public Vec2 getCentre() {
//		float x = (float) roomShape.getBounds().getCenterX();
//		float y = (float) roomShape.getBounds().getCenterY();
//		return new Vec2(x,y);
		return null;
	}

	@Override
	public List<? extends ViewableWall> getWalls() {
		List<Wall> walls = new ArrayList<Wall>();
		for(Segment2 seg : roomShape){
			walls.add(new Wall(seg));
		}
		return walls;
	}
	
	public boolean pointInRoom(Vec2 point){
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
		private Segment2 lineSeg;
		
		public Wall(Segment2 ls){
			lineSeg = ls;
		}
		@Override
		public Vec2 getStart() {
			return lineSeg.start;
		}

		@Override
		public Vec2 getEnd() {
			return lineSeg.end;
		}

	}
	
}
