package space.gui.pipeline.viewable;

/**
 * A door that can be viewed and can change how open it is
 * 
 * @author Simon Pinfold (300280028)
 *
 */
public interface ViewableDoor extends ViewableObject{
	
	/**
	 * The percentage the door is open as a decimal (between 1 and 0)<br>
	 * 
	 *  1 = fully open <br>
	 *  0 = fully closed
	 * @return the open percent of the door
	 */
	public float getOpenPercent();

	/** One of the rooms that the door leads to (not the one that getRoom2 leads to). 
	 * May be null if the door leads to nowhere.
	 * 
	 * @return one of the rooms that the door leads to
	 */
	public ViewableRoom getRoom1();
	
	/** One of the rooms that the door leads to (not the one that getRoom1 leads to). 
	 * May be null if the door leads to nowhere.
	 * 
	 * @return one of the rooms that the door leads to
	 */
	public ViewableRoom getRoom2();

}
