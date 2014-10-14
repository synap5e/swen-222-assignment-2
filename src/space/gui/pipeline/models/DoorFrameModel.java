package space.gui.pipeline.models;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

import space.gui.pipeline.Material;

/** A model for a doorframe without the door in it.
 * Basically three cylinders.
 * 
 * @author Simon Pinfold (300280028)
 *
 */
public class DoorFrameModel implements RenderModel {

	/**
	 * The height of the door
	 */
	static final float DOOR_HEIGHT = 8;
	
	/**
	 * The width of the door
	 */
	static final float DOOR_WIDTH = 4;
	
	
	/**
	 * The display list of the pre-compiled vertex data (relative to this model only)
	 */
	private int frameDisplayList;
	
	/**
	 * Create a new model for a doorframe
	 */
	public DoorFrameModel() {
		this.frameDisplayList = createDoorframeDisplayList();
	}
	
	/** 
	 * Compile the vertex data into a display list
	 *  
	 * @return the compiled display list
	 */
	private static int createDoorframeDisplayList() {
		int frameDisplayList = glGenLists(1);
		glNewList(frameDisplayList, GL_COMPILE);

		// apply material properties
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_COLOR_MATERIAL);
		glColor3f(0.3f, 0.3f, 0.32f);
		Material.chrome.apply();

		// create the primitive quadrics we will be using
		Cylinder c = new Cylinder();
		Sphere s = new Sphere();
		c.setNormals(GL_SMOOTH);
		s.setNormals(GL_SMOOTH);
		
		// this wall offset is used so the frame sits a little further out from the wall and does not cause
		// artifacts when the door is open and thee is another frame on the other side.
		float wall_offset = -0.05f;
		
		// draw the "right" cylinder with a sphere on top (for smoothness)
		glPushMatrix();
		glTranslatef(-DOOR_WIDTH/2, DOOR_HEIGHT, wall_offset);
		s.draw(0.25f, 10, 10);
		glTranslatef(0, -DOOR_HEIGHT, 0);
		glRotatef(-90, 1, 0, 0);
		c.draw(0.25f, 0.25f, DOOR_HEIGHT, 10, 10);
		glPopMatrix();
		
		// draw "left" cylinder
		glPushMatrix();
		glTranslatef(DOOR_WIDTH/2, DOOR_HEIGHT, wall_offset);
		s.draw(0.25f, 10, 10);
		glTranslatef(0, -DOOR_HEIGHT, 0);
		glRotatef(-90, 1, 0, 0);
		c.draw(0.25f, 0.25f, DOOR_HEIGHT, 10, 10);
		glPopMatrix();
		
		// draw the top cylinder
		glPushMatrix();
		glTranslatef(DOOR_WIDTH/2, DOOR_HEIGHT, wall_offset);
		glRotatef(-90, 0, 1, 0);
		c.draw(0.25f, 0.25f, DOOR_WIDTH, 10, 10);
		glPopMatrix();

		glEndList();
		return frameDisplayList;
	}
	
	@Override
	/** 
	 * {@inheritdoc}
	 */
	public void render() {
		// just use the pre-made display list
		glCallList(frameDisplayList);
	}
	

}
