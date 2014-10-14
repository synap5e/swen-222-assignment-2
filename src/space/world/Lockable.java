package space.world;

/**
 * The interface for entities which can be locked.
 * 
 * @author Matt Graham
 *
 */
public interface Lockable {
	/**
	 * Gets current locked state.
	 * 
	 * @return whether the entity is locked.
	 */
	public boolean isLocked();
}
