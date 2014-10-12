package space.world;

import space.gui.pipeline.viewable.ViewableBeam;
import space.math.Vector2D;
import space.math.Vector3D;

public class Beam extends NonStationary implements ViewableBeam {
	private float life = 3;
	private Vector3D beamDir;
	private Turret turret;
	private boolean willHit;
	private static final float LIFE_DURATION = 400;
	
	public Beam(Vector2D position, int id, float elevation,Vector3D beamDirection,Turret t) {
		super(position, id, elevation, "", "");
		beamDir = beamDirection.normalized();
		turret = t;
		willHit();
	}

	public Beam(Vector2D position, int id, float elevation,Vector3D beamDirection,Turret t, float life) {
		super(position, id, elevation, "", "");
		beamDir = beamDirection.normalized();
		turret = t;
		this.life = life;
		willHit();
	}
	
	@Override
	public Vector3D getBeamDirection() {
		return beamDir;
	}

	@Override
	public float getRemainingLife() {
		return life;
	}

	@Override
	public boolean canClip() {
		return false;
	}

	@Override
	public void update(int delta) {
		life -= delta/LIFE_DURATION;
		if(life == 0 && willHit){
			turret.shutDown();
		}
		
	}

	@Override
	public float getCollisionRadius() {
		return 1;
	}

	@Override
	public float getHeight() {
		return 1;
	}

	public Vector3D getBeamDir() {
		return beamDir;
	}

	public Turret getTurret() {
		return turret;
	}
	
	private void willHit(){
		Vector2D toTurret = getPosition().sub(turret.getPosition());
		Vector2D vector = new Vector2D(beamDir.getX(), beamDir.getZ());
		float projectionLength = toTurret.dot(vector);
		Vector2D projection = toTurret.mul(projectionLength);
		willHit = toTurret.sub(projection).len()< turret.getCollisionRadius();
	}

	@Override
	public String getType() {
		return "Beam";
	}
	
}
