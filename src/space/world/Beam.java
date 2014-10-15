package space.world;

import space.gui.pipeline.viewable.ViewableBeam;
import space.math.Vector2D;
import space.math.Vector3D;

/**Represents a laser beam which has been shot out of a BeamShooter.
 * It shuts off a turret if the beam collides with the turret.
 *
 * @author Maria Libunao
 */
public class Beam extends NonStationary implements ViewableBeam {
	private float life = 3;
	private Vector3D beamDir; //the direction of the beam
	private Turret turret; //the turret this is trying to shut down
	private boolean willHit; //whether or not the beam will hit the turret
	private static final float LIFE_DURATION = 400;

	/**Constructs a new beam
	 * @param position
	 * @param id
	 * @param elevation
	 * @param beamDirection
	 * @param t The turret the beam is trying to shut down*/
	public Beam(Vector2D position, int id, float elevation,Vector3D beamDirection,Turret t) {
		super(position, id, elevation, "", "");
		beamDir = beamDirection.normalized();
		turret = t;
		willHit();
	}

	/**Constructs a new beam
	 * @param position
	 * @param id
	 * @param elevation
	 * @param beamDirection
	 * @param t The turret the beam is trying to shut down
	 * @param life the remaining amount of the beam's life*/
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
		if(willHit){
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

	@Override
	public String getType() {
		return "Beam";
	}

	/**@return The direction of the beam*/
	public Vector3D getBeamDir() {
		return beamDir;
	}

	/**@return The turret the beam is trying to shut down*/
	public Turret getTurret() {
		return turret;
	}

	/**Initialises the willHit field.
	 * willHit is whether or not the beam will hit the target turret.
	 * It calculates this by checking if the direction collides with the turret's radius*/
	private void willHit(){
		if(turret == null){
			return;
		}

		Vector2D toTurret = turret.getPosition().sub(getPosition());
		Vector2D vector = new Vector2D(beamDir.getX(), beamDir.getZ());
		float angleDifference = vector.getPolarAngle() + toTurret.getPolarAngle();
		float epsilon = 0.01f;
		willHit = angleDifference <= epsilon && angleDifference >= -epsilon;
	}

}
