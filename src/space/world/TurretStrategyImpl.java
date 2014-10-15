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
	private float cooldown;
	private Room roomTeleportTo;

	/**
	 *  Average (deviates by up to 15%) velocity of bullets in units/second
	 */
	private static final float BULLETVELOCITY = 40;

	/**
	 * n.b. in degrees/second
	 */
	private static final float ROTAION_SPEED = 30;

	/**
	 * degrees
	 */
	private static final float FIRING_TOLERANCE = 45; //the amount the difference in rotation from player it will allow before shooting

	/**
	 * seconds
	 */
	private static final float FIRING_COOLDOWN = 1;

	private static final int PELLET_COUNT = 10;

	/**Constructs a new TurretStrategyImpl
	 * @param t the turret using this strategy
	 * @param yRotation where the turret is facing
	 * @param teleportTo position players teleport to when hit by a bullet*/
	public TurretStrategyImpl(Turret t, float yRotation, Vector2D teleportTo, Room roomTeleportTo){
		this.turret = t;
		this.yRotation = yRotation;
		this.teleportTo = teleportTo;
		this.roomTeleportTo = roomTeleportTo;
	}

	/**Constructs a new TurretStrategyImpl
	 * @param t the turret using this strategy
	 * @param yRotation where the turret is facing
	 * @param bulletsShot number of bullets shot out of turret
	 * @param teleportTo position players teleport to when hit by a bullet*/
	public TurretStrategyImpl(Turret t, float yRotation, int bulletsShot,Vector2D teleportTo, Room roomTeleportTo){
		this.turret = t;
		this.yRotation = yRotation;
		this.teleportTo = teleportTo;
		this.bulletsShot = bulletsShot;
		this.roomTeleportTo = roomTeleportTo;
	}

	/**Updates the turret. This is done by looking for the closest player in the room
	 * & if there is one, adjust where the turret is facing and shoot at the player*/
	@Override
	public void update(int delta) {
		Player p = turret.getRoom().closestPlayer(turret.getPosition());
		if(p==null){return;}
		Vector2D targetDir = p.getPosition().sub(turret.getPosition()).normalized();
		Vector2D lookDir = Vector2D.fromPolar((float)(Math.toRadians(yRotation)), 1);
		Vector2D rightLookDir = Vector2D.fromPolar((float)(Math.toRadians(yRotation) + Math.PI/2), 1);
		if (lookDir.dot(targetDir) > Math.sin(Math.toRadians(FIRING_TOLERANCE)) && cooldown < 0){
			cooldown = FIRING_COOLDOWN;
			for (int i=0; i<PELLET_COUNT;i++){
				shoot();
			}
		}
		cooldown -= delta/1000.f;
		yRotation += Math.signum(rightLookDir.dot(targetDir)) * ROTAION_SPEED * delta/1000.f;
		if(yRotation >= 360){
			yRotation -= 360;
		} else if (yRotation < 0){
			yRotation += 360;
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

	/**Adds a new bullet into the room the turret is in
	 * @param targetDir */
	private void shoot(){
		Vector2D bulletVel2D = Vector2D.fromPolar((float) Math.toRadians(yRotation), 1);
		Vector3D bulletVel3D = new Vector3D(
								bulletVel2D.getX() + (float)(0.5 - Math.random())/10.f,
								(float)(0.5 - Math.random())/10.f,
								bulletVel2D.getY() + (float)(0.5 - Math.random())/10.f).mul(BULLETVELOCITY);
		turret.getRoom().putInRoom(new Bullet(turret.getPosition(), turret.getID()+1000*bulletsShot++, turret.getHeight(), turret.getRoom(), bulletVel3D, teleportTo, roomTeleportTo));
	}

	@Override
	public void setAngle(float angle) {
		yRotation = angle;
	}
}
