package space.world;

/**Represents a strategy which is used by the turret.
 * Controls the turret's y rotation and update method
 * @author Maria Libunao*/
public interface TurretStrategy {

	/**Updates the turret
	 * @param delta the amount of time since the last update*/
	public void update(int delta);

	/**@return the angle of the turret*/
	public float getAngle();

	public void setAngle(float angle);
}
