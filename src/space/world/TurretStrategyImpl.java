package space.world;

import space.math.Vector2D;
import space.math.Vector3D;

/**Represents a strategy used by turrets. This strategy looks for a player in the room. 
 * It adjusts where it is facing so that it can fire bullets at the closest player in the room
 * @author Maria Libunao
 */
public class TurretStrategyImpl implements TurretStrategy {
	private float yRotation;
	private int bulletsShot = 0; //helps to get unique IDs
	private Turret turret;
	private Vector2D teleportTo; //position players teleport to when hit by a bullet
	private Room roomTeleportTo; //the room it teleports the player to
	private static final float TURN_DURATION = 50;
	private static final float ROTATION_DIFFERENCE = 4; //the amount the difference in rotation from player it will allow before shooting
	private static final float VELOCITY = 1;
	
	/**Constructs a new TurretStrategyImpl
	 * @param t the turret using this strategy
	 * @param yRotation where the turret is facing
	 * @param teleportTo position players teleport to when hit by a bullet*/
	public TurretStrategyImpl(Turret t, float yRotation, Vector2D teleportTo){
		this.turret = t;
		this.yRotation = yRotation;
		this.teleportTo = teleportTo;
	}
	
	/**Constructs a new TurretStrategyImpl
	 * @param t the turret using this strategy
	 * @param yRotation where the turret is facing
	 * @param bulletsShot number of bullets shot out of turret
	 * @param teleportTo position players teleport to when hit by a bullet*/
	public TurretStrategyImpl(Turret t, float yRotation, int bulletsShot,Vector2D teleportTo){
		this.turret = t;
		this.yRotation = yRotation;
		this.teleportTo = teleportTo;
		this.bulletsShot = bulletsShot;
	}
	
	/**Updates the turret. This is done by looking for the closest player in the room 
	 * & if there is one, adjust where the turret is facing and shoot at the player*/
	@Override
	public void update(int delta) {
		Player p = turret.getRoom().closestPlayer(turret.getPosition());
		if(p==null){return;}
		Vector2D lookDir = Vector2D.fromPolar(yRotation, 1);
		Vector2D targetDir = turret.getPosition().sub(p.getPosition()).normalized();
		float adjustRotation = lookDir.angleTo(targetDir);
		if(adjustRotation <= ROTATION_DIFFERENCE){
			shoot();
		} else{
			yRotation += delta/TURN_DURATION;
			if(yRotation >= 360){
				yRotation = 360 - yRotation;
			}
		}
		
	}

	/**@return the amount of bullets shot*/
	public int getBulletsShot() {
		return bulletsShot;
	}

	/**@return the turret this strategy is controlling*/
	public Turret getTurret() {
		return turret;
	}

	@Override
	public float getAngle() {
		return yRotation;
	}
	
	/**@return the room the player is teleported to if hit*/
	public Room getRoomTeleportTo() {
		return roomTeleportTo;
	}

	/**@return the position the player is teleported to if hit*/
	public Vector2D getTeleportTo() {
		return teleportTo;
	}

	/**Adds a new bullet into the room the turret is in*/
	private void shoot(){
		Vector2D turretFacing = Vector2D.fromPolar(yRotation, 1);
		Vector3D bulletVel = new Vector3D(turretFacing.getX(),0,turretFacing.getY()).mul(VELOCITY);
		turret.getRoom().putInRoom(new Bullet(turret.getPosition(), turret.getID()+1000*bulletsShot++, turret.getHeight(), turret.getRoom(), bulletVel, teleportTo, roomTeleportTo));
	}
}
