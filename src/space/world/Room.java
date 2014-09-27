package space.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import space.gui.pipeline.viewable.ViewableBeam;
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
	private Map<Integer, List<Door>> doors;


	public Room(LightMode m, int i, String d, ConcaveHull r,Map<Integer,List<Door>> doors){
		mode = m;
		id = i;
		description = d;
		roomShape = r;
		this.doors = doors;
	}

	public Room(LightMode m, int i, String d, List<Vector2D> points,Map<Integer,List<Door>> doors){
		mode = m;
		id = i;
		description = d;
		roomShape = new ConcaveHull(points);
		this.doors = doors;
	}

	public boolean isPositionVacant(Vector2D position, float radius){
		for(Entity e : entities){
			if(position.sub(e.getPosition()).len() <  e.getCollisionRadius() + radius){
				return !e.canClip();
			}
		}
		return true;
	}

	public void update(int delta) {
		for(Entity e: entities){
			e.update(delta);
		}

	}

	@Override
	public boolean contains(Vector2D position) {
		return roomShape.contains(position);
	}
	
	public boolean containsEntity(Entity e){
		return entities.contains(e);
	}

	public boolean contains(Vector2D position, float radius) {
		return roomShape.contains(position,radius);
	}

	
	public void putInRoom(Entity e){
		entities.add(e);
	}

	public void removeFromRoom(Entity e){
		entities.remove(e);
	}
	
	public void addDoor(int i, Door d){
		if(doors.get(i) == null){
			List<Door> doorList = new ArrayList<Door>();
			doorList.add(d);
			doors.put(i, doorList);
		}else{
			doors.get(i).add(d);
		}
	}
	
	@Override
	public Vector2D getCentre() {
		return roomShape.getCentre();
	}

	@Override
	public List<? extends ViewableWall> getWalls() {
		List<Wall> walls = new ArrayList<Wall>();
		for(int i= 0; i<roomShape.size(); i++){
			walls.add(new Wall(roomShape.get(i),doors.get(i)));
		}
		return walls;
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
	
	@Override
	public List<Door> getAllDoors() {
		List<Door> allDoors = new ArrayList<Door>();
		for(List<Door> d : doors.values()){
			allDoors.addAll(d);
		}
		return allDoors;
	}


	@Override
	public Vector2D getAABBTopLeft() {
		return roomShape.getAABBTopLeft();
	}

	@Override
	public Vector2D getAABBBottomRight() {
		return roomShape.getAABBBottomRight();
	}

	@Override
	public List<? extends ViewableBeam> getBeams() {
		// TODO Auto-generated method stub
		return new ArrayList<ViewableBeam>();
	}

	private class Wall implements ViewableWall{
		private Segment2D lineSeg;
		private List<Door> wallDoors;


		public Wall(Segment2D ls, List<Door> doors){
			lineSeg = ls;
			this.wallDoors = doors;
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
			return Collections.unmodifiableList(wallDoors);
		}

	}

}
