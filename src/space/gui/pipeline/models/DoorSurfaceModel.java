package space.gui.pipeline.models;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;

import space.gui.pipeline.Material;
import space.gui.pipeline.TextureLoader;

public class DoorSurfaceModel implements RenderModel {

	private int doorDisplayList;
	

	public DoorSurfaceModel() {
		doorDisplayList = createDoorDisplayList();
	}
	
	private static int doorTexture = 0;
	private static int createDoorDisplayList() {
		if (doorTexture == 0){
			try {
				doorTexture = TextureLoader.loadTexture(new File("./assets/door.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		int doorDisplayList = glGenLists(1);
		glNewList(doorDisplayList, GL_COMPILE);
		glPushAttrib(GL_ALL_ATTRIB_BITS);

		glEnable(GL_TEXTURE_2D);
		glDisable(GL_COLOR_MATERIAL);
		glBindTexture(GL_TEXTURE_2D, doorTexture);
		
		Material.chrome.apply();

		glBegin(GL_QUADS);
		glNormal3f(0, 0, -1);
		glTexCoord2f(1,	0);
		glVertex3f(-DoorFrameModel.DOOR_WIDTH/2, DoorFrameModel.DOOR_HEIGHT, 0.1f);
		
		glTexCoord2f(0,	0);
		glVertex3f(DoorFrameModel.DOOR_WIDTH/2, DoorFrameModel.DOOR_HEIGHT, 0.1f);
		
		glTexCoord2f(0,	1);
		glVertex3f(DoorFrameModel.DOOR_WIDTH/2, 0, 0.1f);
		
		glTexCoord2f(1,	1);
		glVertex3f(-DoorFrameModel.DOOR_WIDTH/2, 0, 0.1f);
		
		
		glEnd();
		
		glPopAttrib();
		glEndList();
		return doorDisplayList;
	}
	
	@Override
	public void render() {
		glCallList(doorDisplayList);
	}

}
