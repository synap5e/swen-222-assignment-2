package space.gui.pipeline.models;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Disk;

import space.gui.pipeline.Material;

/** A model for a bullet
 * 
 * @author Simon Pinfold (300280028)
 *
 */
public class BulletModel implements RenderModel {

	/**
	 *  The length of the bullet (unitles, but relative to all other models)
	 */
	private static final float BULLET_LENGTH = 1f;
	
	/**
	 * The radius of the cylynder of the bullet
	 */
	private static final float BULLET_RADIUS = 0.1f;
	
	/**
	 * The levels of detail on the bullet.
	 * This corresponds to the number of slices and strips for the quadrics
	 */
	private static final int BULLET_LOD = 5;
	
	
	@Override
	/** 
	 * {@inheritdoc}
	 */
	public void render() {
		glPushAttrib(GL_ALL_ATTRIB_BITS);

		glColor3f(0.05f, 0.05f, 0.05f);
		Material.black_plastic.apply();
		
		Cylinder c = new Cylinder();
		Disk d = new Disk();
		
		// the main cylinder
		glRotatef(90, 0, 1, 0);
		c.draw(BULLET_RADIUS, BULLET_RADIUS, BULLET_LENGTH*0.75f, BULLET_LOD, BULLET_LOD);
		
		// the point
		glTranslatef(0, 0, BULLET_LENGTH*0.75f);
		c.draw(BULLET_RADIUS, 0, BULLET_LENGTH*0.25f, BULLET_LOD, BULLET_LOD);
		
		// the end
		glTranslatef(0, 0, -BULLET_LENGTH*0.75f);
		d.draw(BULLET_RADIUS, 0, BULLET_LOD, BULLET_LOD);
		
		glPopAttrib();
	}

}
