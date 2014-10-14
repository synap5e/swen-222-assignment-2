package space.gui.pipeline.viewable;

/** A container that can be in an open or closed state
 *
 * @author Simon Pinfold (300280028)
 *
 */
public interface ViewableOpenable extends ViewableObject {

	/**
	 *  Get whether the container should be draw in the open state or the closed state
	 * @return if the container is open
	 */
	public boolean isOpen();

}
