package space.world;

import space.math.Vector2D;
import space.math.Vector3D;

public class BeamShooter extends NonStationary{
	private float yRotation;
	private Room room;
	private Turret turret;
	private boolean stopped = false;
	private int beamsShot = 0;
	private float readyToShoot = 1;
	private static final float TURN_DURATION = 500;
	private static final float SHOOT_INTERVAL = 500;
	
	public BeamShooter(Vector2D position, int id, float elevation,
			String description, String name, Room room, Turret turret) {
		super(position, id, elevation, description, name);
		this.room = room;
		this.turret = turret;
	}
	
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
	public float getAngle() {
		return yRotation;
	}
	
	@Override
	public boolean canClip() {
		return true;
	}
	
	@Override
	public void update(int delta) {
		if(!stopped){//keeps turning unless stopped
			yRotation += delta/TURN_DURATION;
			if(yRotation >= 360){
				yRotation = 360 - yRotation;
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
	
	public float getyRotation() {
		return yRotation;
	}

	public Room getRoom() {
		return room;
	}

	public Turret getTurret() {
		return turret;
	}

	public int getBeamsShot() {
		return beamsShot;
	}

	private void shoot(){
		Vector2D lookDir = Vector2D.fromPolar(yRotation, 1);
		room.addBeam(new Beam(getPosition(), getID()+1000+beamsShot++, getHeight() + getElevation(), new Vector3D(lookDir.getX(),0,lookDir.getY()), turret));
	}
}
