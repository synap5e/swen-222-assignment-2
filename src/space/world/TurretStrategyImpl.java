package space.world;

import space.math.Vector2D;

public class TurretStrategyImpl implements TurretStrategy {
	private float yRotation;
	private int bulletsShot = 0;
	private Turret turret;
	private static final float TURN_DURATION = 50;
	private static final float ROTATION_DIFFERENCE = 4; //the amount the difference in rotation from player it will allow before shooting
	
	public TurretStrategyImpl(Turret t, float yRotation){
		this.yRotation = yRotation;
	}
	
	public TurretStrategyImpl(Turret t, float yRotation, int bulletsShot){
		this.yRotation = yRotation;
		this.bulletsShot = bulletsShot;
	}
	
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

	public int getBulletsShot() {
		return bulletsShot;
	}

	public Turret getTurret() {
		return turret;
	}

	@Override
	public float getAngle() {
		return yRotation;
	}

	private void shoot(){
		turret.getRoom().putInRoom(new Bullet(turret.getPosition(), turret.getID()+1000+bulletsShot++, turret.getHeight(), turret.getRoom(), null/*what is velocity*/));
	}
}
