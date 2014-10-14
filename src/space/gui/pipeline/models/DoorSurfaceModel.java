package space.gui.pipeline.models;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;

import space.gui.pipeline.Material;
import space.gui.pipeline.TextureLoader;

/** A mode for the surface of a door.
 * Basically a textured quad.
 * 
 * @author Simon Pinfold (300280028)
 *
 */
public class DoorSurfaceModel implements RenderModel {

	/**
	 * The display list of the pre-compiled vertex data (relative to this model only)
	 */
	private int doorDisplayList;
	

	public DoorSurfaceModel() {
		doorDisplayList = createDoorDisplayList();
	}
	
	/**
	 * The texture ID for the door
	 */
	private static int doorTexture = 0;

	/** 
	 * Compile the vertex data into a display list
	 *  
	 * @return the compiled display list
	 */
	private static int createDoorDisplayList() {
		// load the texture only once
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

		
		// draw a quad the size of the door
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
	/** 
	 * {@inheritdoc}
	 */
	public void render() {
		glCallList(doorDisplayList);
	}

}
