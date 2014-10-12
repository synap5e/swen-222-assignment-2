package space.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import space.gui.pipeline.viewable.ViewableBeam;
import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.math.ConcaveHull;
import space.math.Segment2D;
import space.math.Vector2D;
import space.math.Vector3D;


/**Represents a location or room in the World*/
public class Room implements ViewableRoom{
	private Vector3D light;
	private int id;
	private String description;
	private ConcaveHull roomShape;
	private Set<Entity> entities = new HashSet<Entity>();
	private Map<Integer, List<Door>> doors; //wall index to door. Maps which door belongs to which wall
	private List<Beam> beams = new ArrayList<Beam>();
	
	/**Constructs a new room
	 *@param light The lighting of the room
	 *@param id The room's id
	 *@param description The room's description
	 *@param points The list of points which make up the room's shape
	 *@param doors The list of doors in the room which maps to which wall it belongs to*/
	public Room(Vector3D light, int id, String description, List<Vector2D> points){
		this.light = light;
		this.id = id;
		this.description = description;
		this.roomShape = new ConcaveHull(points);
		this.doors = new HashMap<Integer,List<Door>>();
	}

	/**Whether or not a particular position in the room is vacant
	 * @param position the position that will be checked
	 * @param moveIn the entity being moved in*/
	public boolean isPositionVacant(Vector2D position, Entity moveIn){
		for(Entity e : entities){
			if(!e.equals(moveIn) && position.sub(e.getPosition()).len() <  e.getCollisionRadius() + moveIn.getCollisionRadius()){
				if(e.canClip()){
					return false;
				}
			}
		}
		return true;
	} 

	public Player collidedWithPlayer(Entity collider){
		for(Entity e : entities){
			if(e instanceof Player && collider.getPosition().sub(e.getPosition()).len() <  e.getCollisionRadius() + collider.getCollisionRadius()){
				return (Player) e;
			}
		}
		return null;
	}
	
	public Player closestPlayer(Vector2D position){
		Player closest = null;
		for(Entity e : entities){
			if(e instanceof Player){
				if(closest == null || position.sub(e.getPosition()).len() < position.sub(closest.getPosition()).len()){
					closest = (Player) e;
				}
			}
		}
		return closest;
	}
	/**Updates all the entities and doors in the room
	 * @param delta the amount of time since the previous update*/
	public void update(int delta) {
		List<Beam> beamCopy = new ArrayList<Beam>(beams);
		List<Entity> entityCopy = new ArrayList<Entity>(entities);
		for(Entity e: entityCopy){
			e.update(delta);
		}
		for(Beam b: beamCopy){
			b.update(delta);
			if(b.getRemainingLife() <= 0){
				beams.remove(b);
			}
		}
		for(List<Door> doorList : doors.values()){
			for(Door d : doorList){
				if(d.getRoom1().equals(this)){//prevents doors being updated twice
					d.update(delta);
				}
			}
		}

	}

	@Override
	public boolean contains(Vector2D position) {
		return roomShape.contains(position);
	}


	public boolean contains(Vector2D position, float radius) {
		return roomShape.contains(position,radius);
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
	
	public void addBeam(Beam b){
		beams.add(b);
	}

	@Override
	public Vector2D getCentre() {
		return roomShape.getCentre();
	}

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

	@Override
	public Vector3D getLight() {
		return light;
	}

	/**Returns the concave hull which represents the shape of the room
	 * @return*/
	public ConcaveHull getRoomShape() {
		return roomShape;
	}

	public Set<Entity> getEntities() {
		return entities;
	}

	/**Returns which door is on which wall. The integer is the wall index
	 * @return*/
	public Map<Integer, List<Door>> getDoors(){
		return doors;
	}

	@Override
	public List<Entity> getContainedObjects() {
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
	public List<Beam> getBeams() {
		return beams;
	}

	/**Represents a wall in the room. It contains doors and is represented by a line segment*/
	private class Wall implements ViewableWall{
		private Segment2D lineSeg;
		private List<Door> wallDoors;

		/**Constructs a new wall
		 * @param lineSeg the line segment
		 * @param doors the list of doors belonging to the wall*/
		public Wall(Segment2D lineSeg, List<Door> doors){
			this.lineSeg = lineSeg;
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
