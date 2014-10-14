package space.world;

import space.math.Vector2D;

/**Represents a teleporter which teleports a character to a specified position
 * @author Maria Libunao*/

public class Teleporter extends Stationary{
	private Vector2D teleportsToPos; //player teleport destination
	private boolean canInteract; //true if can interact directly, false if player must press on button to interact
	
	/**Constructs a new Teleport
	 * @param teleporterPos the position of the teleporter
	 * @param teleportsToPos the position where the teleporter teleports players to
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name
	 * @param canInteract whether or not it can be directly interacted with*/
	public Teleporter(Vector2D teleporterPos, Vector2D teleportsToPos, int id, float elevation,
			String description, String name,boolean canInteract) {
		super(teleporterPos, id, elevation, description, name);
		this.teleportsToPos = teleportsToPos;
		this.canInteract = canInteract;
	}
	
	@Override
	public boolean canInteract(){
		return canInteract;
	}
	
	@Override
	public boolean interact(Character c, World world){
		Room room = world.getRoomAt(getPosition());
		Character charOnTeleporter = room.collidedWithPlayer(this);
		if(charOnTeleporter == null){
			return false;
		}
		Room otherRoom = world.getRoomAt(teleportsToPos);
		if(otherRoom.isPositionVacant(teleportsToPos, charOnTeleporter)){
			charOnTeleporter.setPosition(teleportsToPos);
			charOnTeleporter.getRoom().removeFromRoom(charOnTeleporter);
			otherRoom.putInRoom(charOnTeleporter);
			charOnTeleporter.setRoom(otherRoom);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean canClip() {
		return false;
	}
	
	@Override
	public float getCollisionRadius() {
		return 3;
	}
	
	@Override
	public float getHeight() {
		return 5;
	}
	
	/**@return the position where this teleporter telports players to*/
	public Vector2D getTeleportsTo() {
		return teleportsToPos;
	}

	@Override
	public String getType() {
		return "Teleporter";
	}

}
