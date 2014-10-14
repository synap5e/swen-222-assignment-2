package space.gui.pipeline.viewable;

import space.math.Vector3D;

/** A laser beam that is drawn in a straight line. The beam will appear to extend forever.
 * 
 * @author Simon Pinfold (300280028)
 *
 */
public interface ViewableBeam extends ViewableObject{
	
	/** Gets the direction of the beam
	 * 
	 * @return the direction of the beam
	 */
	public Vector3D getBeamDirection();
	
	/** decreases over the lifespan of the beam until it gets to 0 when the beam is not longer visible
	 *  The remaining life of the beam as a scalar from 1 to 0.
	 *  This is the "fade level" of the beam and controls how transparent it is.
	 *  
	 * @return The remaining life of the beam normalized between 1 and 0
	 */
	public float getRemainingLife();

}
