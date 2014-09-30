package space.world;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.math.Vector2D;

public class WorldTest {
	@Test
	public void moveInSameRoomValid(){
		World world = new World();
		Room room = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Vector2D newPos = new Vector2D(42,45);
		Player p = new Player(new Vector2D(45,45),1);
		p.setRoom(room);
		room.putInRoom(p);
		world.moveCharacter(p, newPos);
		assertTrue("Player should have moved", p.getPosition().equals(newPos));
	}
	
	@Test 
	public void moveToDifferentRoomValid(){
		World world = new World();
		Room r1 = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Room r2 = createRoom(1,new Vector2D(60,60),new Vector2D(90,60),new Vector2D(90,30),new Vector2D(60,30));
		Door d = new Door(new Vector2D(60,50), 2, "", r1, r2, false, false);
		r1.addDoor(1,d);
		r2.addDoor(3,d);
		addToWorld(world,r1,r2);
		d.openDoor();
		world.update(500);
		Vector2D newPos = new Vector2D(70,50);
		Player p = new Player(new Vector2D(50,50),1);
		p.setRoom(r1);
		r1.putInRoom(p);
		world.moveCharacter(p,newPos);
		assertTrue("Player should have changed its position", p.getPosition().equals(newPos));
		assertTrue("Player should be in the new room", p.getRoom().equals(r2));
		assertTrue("The new room should hold the player", r2.containsEntity(p));
		assertFalse("Player should no longer be in the old room", r1.containsEntity(p));
	}
	
	@Test
	public void moveInSameRoomInvalid(){//position not vacant
		World world = new World();
		Room room = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Vector2D oldPos = new Vector2D(45,45);
		Player p = new Player(oldPos,1);
		Key key = new Key(new Vector2D(49,45), 2, "", 0, null);
		p.setRoom(room);
		room.putInRoom(p);
		room.putInRoom(key);
		world.moveCharacter(p, new Vector2D(48,45));
		System.out.println(p.getPosition() + " " + oldPos);
		assertTrue("Player should not have moved", p.getPosition().equals(oldPos));
	} 
	
	@Test
	public void moveToDifferentRoomInvalid1(){//closed door
		World world = new World();
		Room r1 = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Room r2 = createRoom(1,new Vector2D(60,60),new Vector2D(90,60),new Vector2D(0,30),new Vector2D(60,30));
		Door d = createDoor(r2,r1,new Vector2D(60,50));
		Vector2D oldPos = new Vector2D(50,50);
		Player p = new Player(oldPos,1);
		p.setRoom(r1);
		r1.putInRoom(p);
		world.moveCharacter(p,new Vector2D(70,50));
		assertTrue("Player should not have moved when door was closed", p.getPosition().equals(oldPos));
	}
	
	@Test
	public void moveToDifferentRoomInvalid2(){//open door but not going through it
		World world = new World();
		Room r1 = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Room r2 = createRoom(1,new Vector2D(60,60),new Vector2D(90,60),new Vector2D(90,30),new Vector2D(60,30));
		Door d = createDoor(r2,r1,new Vector2D(60,50));
		addToWorld(world,r1,r2);
		d.openDoor();
		world.update(500);
		Vector2D oldPos = new Vector2D(40,40);
		Player p = new Player(oldPos,1);
		p.setRoom(r1);
		r1.putInRoom(p);
		world.moveCharacter(p,new Vector2D(70,40));
		assertTrue("Player should not have moved to other room when not going through door", p.getPosition().equals(oldPos));
	}
	
	@Test
	public void moveToDifferentInvalid3(){//position in other room is not vacant
		World world = new World();
		Room r1 = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Room r2 = createRoom(1,new Vector2D(60,60),new Vector2D(90,60),new Vector2D(90,30),new Vector2D(60,30));
		Door d = createDoor(r2,r1,new Vector2D(60,50));
		addToWorld(world,r1,r2);
		d.openDoor();
		world.update(500);
		Vector2D oldPos = new Vector2D(50,50);
		Player p = new Player(oldPos,1);
		Key key = new Key(new Vector2D(71,50), 3, "", 0, null);
		r2.putInRoom(key);
		p.setRoom(r1);
		r1.putInRoom(p);
		world.moveCharacter(p,new Vector2D(70,50));
		assertTrue("Player should not have moved to other room when the position is occupied", p.getPosition().equals(oldPos));
	}
	
	@Test
	public void moveToDifferentInvalid4(){//trying to move to a room which is not connected to the room it is in
		World world = new World();
		Room r1 = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Room r2 = createRoom(1,new Vector2D(60,60),new Vector2D(90,60),new Vector2D(90,30),new Vector2D(60,30));
		Door d = createDoor(r2,r1,new Vector2D(60,50));
		addToWorld(world,r1,r2);
		d.openDoor();
		world.update(500);
		Vector2D oldPos = new Vector2D(50,50);
		Player p = new Player(oldPos,1);
		p.setRoom(r1);
		r1.putInRoom(p);
		world.moveCharacter(p, new Vector2D(91,50));
		assertTrue("Player should not have moved when the room it is in has no door that leads to the position",p.getPosition().equals(oldPos));
	}
	
	@Test
	public void invalidPickup1(){//entity being picked up is not pickup-able
		World world = new World();
		Player p1 = new Player(new Vector2D(45,45),1);
		Player p2 = new Player(new Vector2D(48,48),2);
		world.pickUpEntity(p1, p2);
		assertTrue("Player should not be able to pickup another player", p1.getInventory().size() == 0);
	}
	
	@Test
	public void invalidPickup2(){//picking up something out of reach
		World world = new World();
		Room r = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Player p1 = new Player(new Vector2D(40,40),1);
		p1.setRoom(r);
		r.putInRoom(p1);
		Key key = new Key(new Vector2D(50,50), 3, "", 0, null);
		r.putInRoom(key);
		world.pickUpEntity(p1, key);
		assertTrue("Player should not be able to pickup something which is out of reach", p1.getInventory().size() == 0);
	}
	
	@Test
	public void invalidPickup3(){//entity not in same room
		World world = new World();
		Room r = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Player p1 = new Player(new Vector2D(59,45),1);
		p1.setRoom(r);
		r.putInRoom(p1);
		Key key = new Key(new Vector2D(61,45), 3, "", 0, null);
		world.pickUpEntity(p1, key);
		assertTrue("Player should not be able to pickup something which is not in the same room", p1.getInventory().size() == 0);
	}
	
	@Test
	public void validPickup(){
		World world = new World();
		Room r = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Player p = new Player(new Vector2D(52,53),1);
		p.setRoom(r);
		r.putInRoom(p);
		Key key = new Key(new Vector2D(55,55), 3, "", 0, null);
		r.putInRoom(key);
		world.pickUpEntity(p, key);
		assertTrue("Player should be able to pickup", p.getInventory().contains(key));
	}
	
	@Test
	public void invalidDrop1(){//not within reach
		World world = new World();
		Room r = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Player p = new Player(new Vector2D(52,53),1);
		p.setRoom(r);
		Pickup item = addToInventory(p);
		world.dropEntity(p, item, new Vector2D(20,20));
		assertTrue("Player should not have dropped entity", p.getInventory().contains(item));
	}
	
	@Test
	public void invalidDrop2(){//entity not in player's inventory
		World world = new World();
		Room r = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Player p = new Player(new Vector2D(50,50),1);
		p.setRoom(r);
		Vector2D pos = new Vector2D(15,15);
		Key k = new Key(pos, 0, null, 0, null);
		world.dropEntity(p, k, new Vector2D(52,53));
		assertTrue("Entity should not have moved because it is not in player's inventory",k.getPosition().equals(pos));
	}
	
	@Test
	public void invalidDrop3(){//the position at which the entity is being dropped is not vacant
		World world = new World();
		Room r = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Player p = new Player(new Vector2D(50,50),1);
		p.setRoom(r);
		Key k = new Key(new Vector2D(51,52), 0, null, 0, null);
		r.putInRoom(k);
		Pickup item = addToInventory(p);
		world.dropEntity(p, item, new Vector2D(52,53));
		assertTrue("Entity cannot be dropped because the spot is not vacant",p.getInventory().contains(item));		
	}
	
	@Test
	public void invalidDrop4(){
		World world = new World();
		Room r = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Player p = new Player(new Vector2D(59,45),1);
		p.setRoom(r);
		Pickup item = addToInventory(p);
		world.dropEntity(p, item, new Vector2D(61,45));
		assertTrue("Entity cannot be dropped because the spot is not vacant",p.getInventory().contains(item));		
	}
	
	@Test
	public void validDrop(){
		World world = new World();
		Room r = createRoom(0,new Vector2D(30,60),new Vector2D(60,60),new Vector2D(60,30),new Vector2D(30,30));
		Vector2D pos = new Vector2D(50,50);
		Player p = new Player(pos,1);
		p.setRoom(r);
		Pickup item = addToInventory(p);
		world.dropEntity(p, item, pos);
		assertFalse("Entity should have been removed from inventory",p.getInventory().contains(item));
		assertTrue("Entity's position should be where the player dropped it", item.getPosition().equals(pos));
		assertTrue("Entity should be in the room it was dropped in", r.containsEntity(item));
		
	}
	private Room createRoom(int id,Vector2D ... roomPoints){
		return new Room(LightMode.BASIC_LIGHT, id, "" , Arrays.asList(roomPoints), new HashMap<Integer,List<Door>>());
	}
	
	private Door createDoor(Room r1, Room r2, Vector2D pos){
		Door d = new Door(pos,0, " ", r1,r2, false, false);
		r1.addDoor(0,d);
		r2.addDoor(0,d);
		return d;
	}
	
	private Pickup addToInventory(Character c){
		Key k = new Key(null, 0, "", 0, null);
		c.pickup(k);
		return k; 
	}
	private void addToWorld(World w, Room ... rooms){
		for(Room r : rooms){
			w.addRoom(r);
		}
	}
	
}
