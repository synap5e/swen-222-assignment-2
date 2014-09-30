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

/**Represents a location or room in the World*/
public class Room implements ViewableRoom{
	private LightMode mode;
	private int id;
	private String description;
	private ConcaveHull roomShape;
	private Set<Entity> entities = new HashSet<Entity>();
	private Map<Integer, List<Door>> doors;

	/**Constructs a new room
	 *@param m The lighting of the room
	 *@param i The room's id
	 *@param d The room's description
	 *@param points The list of points which make up the room's shape
	 *@param doors The list of doors in the room which maps to which wall it belongs to*/
	public Room(LightMode m, int i, String d, List<Vector2D> points,Map<Integer,List<Door>> doors){
		mode = m;
		id = i;
		description = d;
		roomShape = new ConcaveHull(points);
		this.doors = doors;
	}

	/**Whether or not a particular position in the room is vacant
	 * @param position the position that will be checked
	 * @param radius the bounding circle of how much space should be vacant around the position*/
	public boolean isPositionVacant(Vector2D position, float radius){
		for(Entity e : entities){
			if(position.sub(e.getPosition()).len() <  e.getCollisionRadius() + radius){
				if(e.canClip()){
					return false;
				}
			}
		}
		return true;
	}

	/**Updates all the entities and doors in the room
	 * @param delta the amount of time since the previous update*/
	public void update(int delta) {
		for(Entity e: entities){
			e.update(delta);
		}
		for(List<Door> doorList : doors.values()){
			for(Door d : doorList){
				if(d.getRoom1().equals(this)){//prevents doors being updated twice
					d.update(delta);
				}
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Vector2D position) {
		return roomShape.contains(position);
	}
	

	public boolean contains(Vector2D position, float radius) {
		return roomShape.contains(position);
		//return roomShape.contains(position,radius);
	}
	
	/**Returns whether or not the entity is in the room
	 * @param e the entity that will be checked
	 * @return*/
	public boolean containsEntity(Entity e){
		return entities.contains(e);
	}
	
	/**Adds the entity to the room
	 * @param e the entity that will be put in the room*/
	public void putInRoom(Entity e){
		entities.add(e);
	}

	/**Removes the entity from the room
	 * @param e the entity that will be removed from the room*/
	public void removeFromRoom(Entity e){
		entities.remove(e);
	}
	
	/**Adds a door to the room.
	 * @param i the wall the door belongs to
	 * @param d the door that will be added*/
	public void addDoor(int i, Door d){
		if(doors.get(i) == null){
			List<Door> doorList = new ArrayList<Door>();
			doorList.add(d);
			doors.put(i, doorList);
		}else{
			doors.get(i).add(d);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2D getCentre() {
		return roomShape.getCentre();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<? extends ViewableWall> getWalls() {
		List<Wall> walls = new ArrayList<Wall>();
		for(int i= 0; i<roomShape.size(); i++){
			List<Door> doorsForWall = doors.get(i);
			if (doorsForWall == null) doorsForWall = new ArrayList<Door>();
			walls.add(new Wall(roomShape.get(i), doorsForWall));
		}
		return walls;
	}

	/**Returns the description of the room
	 * @return*/
	public String getDescription() {
		return description;
	}

	/**Returns the id of the room
	 * @return*/
	public int getID() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LightMode getLightMode() {
		return mode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<? extends ViewableObject> getContainedObjects() {
		return new ArrayList<Entity>(entities);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Door> getAllDoors() {
		List<Door> allDoors = new ArrayList<Door>();
		for(List<Door> d : doors.values()){
			allDoors.addAll(d);
		}
		return allDoors;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2D getAABBTopLeft() {
		return roomShape.getAABBTopLeft();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2D getAABBBottomRight() {
		return roomShape.getAABBBottomRight();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<? extends ViewableBeam> getBeams() {
		// TODO Auto-generated method stub
		return new ArrayList<ViewableBeam>();
	}
	
	/**Represents a wall in the room. It contains doors and is represented by a line segment*/
	private class Wall implements ViewableWall{
		private Segment2D lineSeg;
		private List<Door> wallDoors;
		
		/**Constructs a new wall
		 * @param ls the line segment
		 * @param doors the list of doors belonging to the wall*/
		public Wall(Segment2D ls, List<Door> doors){
			lineSeg = ls;
			this.wallDoors = doors;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector2D getStart() {
			return lineSeg.start;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector2D getEnd() {
			return lineSeg.end;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public List<? extends ViewableDoor> getDoors() {
			return Collections.unmodifiableList(wallDoors);
		}

	}


	public ConcaveHull getRoomShape() {
		return roomShape;
	}

	public Set<Entity> getEntities() {
		return entities;
	}
	
	public Map<Integer, List<Door>> getDoors(){
		return doors;
	}

}
