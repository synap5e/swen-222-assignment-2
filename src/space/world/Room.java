package space.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewableObject;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.math.ConcaveHull;
import space.math.Segment2D;
import space.math.Vector2D;

public class Room implements ViewableRoom{
	private LightMode mode;
	private int id;
	private String description;
	private ConcaveHull roomShape;
	private Set<Entity> entities = new HashSet<Entity>();
	private Map<Room,Exit> exits = new HashMap<Room,Exit>();
	
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
		return roomShape.getCentre();
	}

	@Override
	public List<? extends ViewableWall> getWalls() {
		List<Wall> walls = new ArrayList<Wall>();
		for(Segment2D seg : roomShape){
			walls.add(new Wall(seg));
		}
		return walls;
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
	
	public void putInRoom(Entity e){
		entities.add(e);
	}
	
	public void removeFromRoom(Entity e){
		entities.remove(e);
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
		return new ArrayList<Entity>(entities);
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
		@Override
		public List<? extends ViewableDoor> getDoors() {
			return new ArrayList<Exit>(exits.values());
		}

	}

	@Override
	public boolean contains(Vector2D position) {
		return roomShape.contains(position);
	}

	@Override
	public List<? extends ViewableDoor> getAllDoors() {
		return new ArrayList<Exit>(exits.values());
	}
	
	
	@Override
	public Vector2D getAABBTopLeft() {
		return roomShape.getAABBTopLeft();
	}

	@Override
	public Vector2D getAABBBottomRight() {
		return roomShape.getAABBBottomRight();
	}
	
	public boolean isPositionVacant(Vector2D position){
		for(Entity e : entities){
			if(e.getPosition().equals(position, 0.5f)){
				if(e.canClip()){
					return false;
				}
				return true;
			}
		}
		return true;
	}

	public Map<Room, Exit> getExits() {
		return exits;
	}
	
	public boolean containsEntity(Entity e){
		return entities.contains(e);
	}
}
