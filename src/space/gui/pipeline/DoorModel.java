package space.gui.pipeline;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import space.gui.pipeline.viewable.ViewableDoor;

public class DoorModel {

	private static int frameDisplayList;
	static{
		frameDisplayList = createDoorFrameDisplayList();
	}

	private ViewableDoor door;

	public DoorModel(ViewableDoor door) {
		this.door = door;
	}
	public void render() {
		glCallList(frameDisplayList);
	}



	private static int createDoorFrameDisplayList() {
		int frameDisplayList = glGenLists(1);
		glNewList(frameDisplayList, GL_COMPILE);


		glPushMatrix();

		glDisable(GL_TEXTURE_2D);
		glEnable(GL_COLOR_MATERIAL);
		Material.brass.apply();
		glRotatef(-90, 1, 0, 0);
		Cylinder c = new Cylinder();
		c.setNormals(GL_SMOOTH);
		c.draw(1, 1, 20, 10, 10);
		glPopMatrix();


		glEndList();
		return frameDisplayList;
	}

}
