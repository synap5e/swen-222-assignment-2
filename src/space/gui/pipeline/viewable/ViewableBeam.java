package space.gui.pipeline.viewable;

import space.math.Vector3D;

public interface ViewableBeam extends ViewableObject{
	
	public Vector3D getBeamDirection();
	
	/** decreases over the lifespan of the beam until it gets to 0 when the beam is not longer visible
	 * 
	 * @return
	 */
	public float getRemainingLife();

}
