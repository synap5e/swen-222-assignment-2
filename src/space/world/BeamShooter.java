package space.world;

import space.math.Vector2D;
import space.math.Vector3D;

/**Represents an item which shoots beams.
 * It constantly rotates until it is stopped. Once stopped, it fires beams.*/
public class BeamShooter extends NonStationary{
	private float yRotation = 0;
	private Room room; //the room it is in
	private Turret turret; //the turret this is trying to shut down
	private boolean stopped = false; //whether it's been stopped from rotating
	private int beamsShot = 0; //to provide a unique id
	private float readyToShoot = 1;
	private static final float TURN_DURATION = 100;
	private static final float SHOOT_INTERVAL = 500;
	private int timeSinceRotate = 0;

	/**Constructs a new BeamShooter
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name
	 * @param room The room this is in
	 * @param turret The turret it's trying to shoot down*/
	public BeamShooter(Vector2D position, int id, float elevation,
			String description, String name, Room room, Turret turret) {
		super(position, id, elevation, description, name);
		this.room = room;
		this.turret = turret;
	}

	/**Constructs a new BeamShooter
	 * @param position
	 * @param id
	 * @param elevation
	 * @param description
	 * @param name
	 * @param room The room this is in
	 * @param turret The turret it's trying to shoot down
	 * @param yRotation
	 * @param stopped whether it's been stopped from rotating
	 * @param beamsShot how many beams it has shot*/
	public BeamShooter(Vector2D position, int id, float elevation,
			String description, String name, Room room, Turret turret, float yRotation, boolean stopped, int beamsShot) {
		super(position, id, elevation, description, name);
		this.room = room;
		this.yRotation = yRotation;
		this.stopped = stopped;
		this.beamsShot = beamsShot;
		this.turret = turret;
	}

	@Override
	public void update(int delta) {
		timeSinceRotate += delta;
		if(!stopped){//keeps turning unless stopped
			if (timeSinceRotate > TURN_DURATION){
				yRotation += 10;
				timeSinceRotate = 0;
				if(yRotation >= 360){
					yRotation = 360 - yRotation;
				}
			}
		} else {//shoots when stopped
			if(readyToShoot ==1){
				shoot();
			}
			readyToShoot -= delta/SHOOT_INTERVAL;
			if(readyToShoot < 0){
				readyToShoot = 1;
			}

		}
	}

	@Override
	public boolean interact(Character c, World w){
		if(stopped){
			readyToShoot = 1;
		}
		stopped = !stopped;
		return true;
	}

	@Override
	public float getCollisionRadius() {
		return 1;
	}
	@Override
	public float getHeight() {
		return 1;
	}

	@Override
	public float getAngle() {
		return yRotation;
	}

	@Override
	public boolean canClip() {
		return true;
	}

	/**@return the room the beamShooter is in*/
	public Room getRoom() {
		return room;
	}

	/**@return the turret this is trying to shut down*/
	public Turret getTurret() {
		return turret;
	}

	/**@return the number of beams this has shot so far*/
	public int getBeamsShot() {
		return beamsShot;
	}

	@Override
	public String getType() {
		return "BeamShooter";
	}

	/**@return whether or not this has stopped rotating*/
	public boolean isStopped() {
		return stopped;
	}

	/**Creates a new beam and puts it in the room*/
	private void shoot(){
		Vector2D lookDir = Vector2D.fromPolar((float) Math.toRadians(yRotation), 1);
		room.addBeam(new Beam(getPosition(), getID()+1000*beamsShot++, getHeight() + getElevation(), new Vector3D(lookDir.getX(),0,lookDir.getY()), turret));
	}

	public void setAngle(float angle){
		yRotation = angle;
	}

}
