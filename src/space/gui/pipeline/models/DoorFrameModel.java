package space.gui.pipeline.models;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

import space.gui.pipeline.Material;

public class DoorFrameModel implements RenderModel {

	static final float DOOR_HEIGHT = 8;
	static final float DOOR_WIDTH = 4;
	
	private int frameDisplayList;
	
	public DoorFrameModel() {
		this.frameDisplayList = createDoorframeDisplayList();
	}
	
	private static int createDoorframeDisplayList() {
		int frameDisplayList = glGenLists(1);
		glNewList(frameDisplayList, GL_COMPILE);

		glDisable(GL_TEXTURE_2D);
		glEnable(GL_COLOR_MATERIAL);
		glColor3f(0.3f, 0.3f, 0.32f);
		Material.chrome.apply();

		Cylinder c = new Cylinder();
		Sphere s = new Sphere();
		c.setNormals(GL_SMOOTH);
		s.setNormals(GL_SMOOTH);
		glEnable(GL_COLOR_MATERIAL);
		glPushMatrix();
		glTranslatef(-DOOR_WIDTH/2, DOOR_HEIGHT, -0.05f);
		s.draw(0.25f, 10, 10);
		glTranslatef(0, -DOOR_HEIGHT, 0);
		glRotatef(-90, 1, 0, 0);
		c.draw(0.25f, 0.25f, DOOR_HEIGHT, 10, 10);
		glPopMatrix();
		
		glPushMatrix();
		glTranslatef(DOOR_WIDTH/2, DOOR_HEIGHT, -0.05f);
		s.draw(0.25f, 10, 10);
		glTranslatef(0, -DOOR_HEIGHT, 0);
		glRotatef(-90, 1, 0, 0);
		c.draw(0.25f, 0.25f, DOOR_HEIGHT, 10, 10);
		glPopMatrix();
		
		glPushMatrix();
		glTranslatef(DOOR_WIDTH/2, DOOR_HEIGHT, -0.05f);
		glRotatef(-90, 0, 1, 0);
		c.draw(0.25f, 0.25f, DOOR_WIDTH, 10, 10);
		glPopMatrix();

		glEndList();
		return frameDisplayList;
	}
	
	@Override
	public void render() {
		glCallList(frameDisplayList);
	}
	

}
