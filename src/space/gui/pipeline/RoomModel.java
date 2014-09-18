package space.gui.pipeline;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;

import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewableObject;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.math.Vector2D;
import space.math.Vector3D;

/**
 *
 * @author Simon Pinfold
 *
 */
public class RoomModel {

	private int displayList;
	public RoomModel(ViewableRoom room, Map<Class<? extends ViewableObject>, Integer> models){
		// pre-compile the viewmodel for all static models, the walls and the ceiling
		this.displayList = createDisplayList(room, models);
	}
	public void render() {
		glCallList(this.displayList);
	}



	private static final float WALL_HEIGHT = 11;

	/** What size to make the quads that make up a wall.
	 * Walls must be tessellated for secular light to display
	 * correctly.
	 */
	private static final float TESSELLATION_SIZE = 1;

	/** How many tessellated squares does a wall texture cover */
	private static final float TEXTURE_TESSELLATION_MULTIPLE = 10;

	private static int wallTexture;
	private static int floorTexture;
	private static int ceilingTexture;

	private static Material wallMaterial;
	private static Material ceilingMaterial;

	static {
		wallMaterial = new Material(
				new Vector3D(0.2f, 0.2f, 0.2f),
				new Vector3D(1, 1, 1),
				new Vector3D(0.3f, 0.3f, 0.3f),
				0.5f
		);
		ceilingMaterial = new Material(
				new Vector3D(0.4f, 0.4f, 0.4f),
				new Vector3D(1, 1, 1),
				new Vector3D(0.3f, 0.3f, 0.3f),
				0.5f
		);

		try {
			wallTexture = TextureLoader.loadTexture(new File("./assets/shiphull.png"));
			floorTexture = TextureLoader.loadTexture(new File("./assets/shiphull.png"));
			ceilingTexture = TextureLoader.loadTexture(new File("./assets/shiphull.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static int createDisplayList(ViewableRoom room, Map<Class<? extends ViewableObject>, Integer> models){
		int displayList = glGenLists(1);
		glNewList(displayList, GL_COMPILE);

		wallMaterial.apply();
		//Material.simple.apply(); //silver.apply();

		glColor3d(0.20, 0.2, 0.22);


		glDisable(GL_COLOR_MATERIAL);
		glEnable(GL_TEXTURE_2D);

		glBindTexture(GL_TEXTURE_2D, wallTexture);

		glBegin(GL_QUADS);
		float tex_step = 1/TEXTURE_TESSELLATION_MULTIPLE;
		for (ViewableWall r : room.getWalls()) {
			
			List<? extends ViewableDoor> doors = r.getDoors();
			
			float x1 = r.getStart().getX();
			float x2 = r.getEnd().getX();

			float z1 = r.getStart().getY();
			float z2 = r.getEnd().getY();

			// normal of a line segment (the wall)
			Vector3D normal = new Vector3D(1 * (z2 - z1), 0, -1 * (x2 - x1)).normalized();

			glNormal3f(normal.getX(), normal.getY(), normal.getZ());

			// we need to tessellate the wall into squares of TESSELLATION_SIZE * TESSELLATION_SIZE
			// for the length of the wall the vector 'delta' is added to the start vector until
			// we reach past the end of the wall. Once we are past the wall end is then clipped back
			// to end

			Vector2D wallVec = r.getEnd().sub(r.getStart());
			float lengthTesselations = wallVec.len() / TESSELLATION_SIZE;
			Vector2D delta = wallVec.div(lengthTesselations);
			Vector2D start = r.getStart();

			float left_tex = 1;
			for (int l=0;l<Math.ceil(lengthTesselations);l++){
				
				Vector2D end = start.add(delta);

				if (l+1>lengthTesselations){
					end = r.getEnd();
				}
				float xtc = end.sub(start).len() / TEXTURE_TESSELLATION_MULTIPLE;

				float yStep = TESSELLATION_SIZE;
				float top_tex = 1;
				for (float y=0;y<WALL_HEIGHT;y+=TESSELLATION_SIZE){
					if (y+TESSELLATION_SIZE > WALL_HEIGHT) yStep = WALL_HEIGHT % TESSELLATION_SIZE;

					float ytc = yStep/TEXTURE_TESSELLATION_MULTIPLE;

					glTexCoord2f(left_tex, top_tex); // bottom left
					glVertex3d(start.getX(), y, start.getY());

					glTexCoord2f(left_tex, top_tex-ytc); //top left
					glVertex3d(start.getX(), y+yStep, start.getY());

					glTexCoord2f(left_tex-xtc, top_tex-ytc); // top right
					glVertex3d(end.getX(), y+yStep, end.getY());

					glTexCoord2f(left_tex-xtc, top_tex); // bottom right
					glVertex3d(end.getX(), y, end.getY());

					top_tex -= tex_step;
					if (top_tex < tex_step/2){
						top_tex = 1;
					}
				}

				left_tex -= tex_step;
				if (left_tex < tex_step/2){
					left_tex = 1;
				}

				start = end;
			}
		}
		glEnd();
		

		//glBindTexture(GL_TEXTURE_2D, floorTexture);
		ceilingMaterial.apply();
		glNormal3f(0,1,0);
		glBegin(GL_QUADS);
		renderYPlane(room, 0, true);
		glEnd();

		ceilingMaterial.apply();
		glNormal3f(0,-1,0);
		glBegin(GL_QUADS);
		renderYPlane(room, WALL_HEIGHT, false);
		glEnd();


		glDisable(GL_TEXTURE_2D);
		glEnable(GL_COLOR_MATERIAL);
		for (ViewableObject viewableObject : room.getContainedObjects()){
			if (!viewableObject.canMove()){
				GameRenderer.drawObject(viewableObject, models);
			}
		}


		glEndList();

		return displayList;
	}

	private static void renderYPlane(ViewableRoom room, float yVal, boolean windClockwise) {
		float texStep = 1/TEXTURE_TESSELLATION_MULTIPLE;
		for (float xt = -50;xt<50;xt+=TEXTURE_TESSELLATION_MULTIPLE){
			for (float yt = -50;yt<50;yt+=TEXTURE_TESSELLATION_MULTIPLE){
				for (float x=0;x<TEXTURE_TESSELLATION_MULTIPLE/TESSELLATION_SIZE;x++){
					for (float y=0;y<TEXTURE_TESSELLATION_MULTIPLE/TESSELLATION_SIZE;y++){
						// yo dawg, I heard you like for loops...

						if (!inRoom(room,
									xt+TESSELLATION_SIZE*x, yt+TESSELLATION_SIZE*y,
									TESSELLATION_SIZE, 		TESSELLATION_SIZE)){
							continue;
						}

						renderYPlaneQuad(windClockwise, yVal, texStep, xt, yt, x, y);
					}
				}
			}
		}
	}

	private static void renderYPlaneQuad(boolean windClockwise, float yVal, float texStep, float xt, float yt, float x, float y) {
		// if windClockwise ==  true then we loop through the switch statement forwards, this creates a clockwise winding
		// if windClockwise == false then instead we loop backwards from 3 to 0 and run the switch statements in reverse order
		for (int i=windClockwise ? 0 : 3;windClockwise ? i<4 : i >= 0; i+= windClockwise ? 1 : -1){
			switch(i){
			case 0:
				glTexCoord2f(texStep*x, texStep*(y+1));
				glVertex3f(xt+TESSELLATION_SIZE*x, yVal, yt+TESSELLATION_SIZE*(y+1));
				break;
			case 1:
				glTexCoord2f(texStep*(x+1), texStep*(y+1));
				glVertex3f(xt+TESSELLATION_SIZE*(x+1), yVal, yt+TESSELLATION_SIZE*(y+1));
				break;
			case 2:
				glTexCoord2f(texStep*(x+1), texStep*y);
				glVertex3f(xt+TESSELLATION_SIZE*(x+1), yVal, yt+TESSELLATION_SIZE*y);
				break;
			case 3:
				glTexCoord2f(texStep*x, texStep*y);
				glVertex3f(xt+TESSELLATION_SIZE*x, yVal, yt+TESSELLATION_SIZE*y);
				break;
			}
		}
	}

	private static boolean inRoom(ViewableRoom room, float x, float y, float w, float h) {
		return inRoom(room,
						new Vector2D(x, 	y),
						new Vector2D(x+w,	y),
						new Vector2D(x+w, 	y+h),
						new Vector2D(x, 	y+h));
	}

	private static boolean inRoom(ViewableRoom room, Vector2D... points) {
		for (Vector2D point : points){
			if (room.contains(point)) return true;
		}
		return false;
	}

}
