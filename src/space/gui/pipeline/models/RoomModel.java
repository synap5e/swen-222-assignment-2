package space.gui.pipeline.models;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import space.gui.pipeline.GameRenderer;
import space.gui.pipeline.Material;
import space.gui.pipeline.ModelFlyweight;
import space.gui.pipeline.TextureLoader;
import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewableObject;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableStationary;
import space.gui.pipeline.viewable.ViewableWall;
import space.math.Segment2D;
import space.math.Vector2D;
import space.math.Vector3D;

/**
 *
 * @author Simon Pinfold
 *
 */
public class RoomModel {

	private int displayList;
	private HashMap<ViewableDoor, Float> doorRotations = new HashMap<ViewableDoor, Float>();;
	public RoomModel(ViewableRoom room, ModelFlyweight models){
		// pre-compile the viewmodel for all static models, the walls and the ceiling
		this.displayList = createDisplayList(room, models);
	}
	public void render() {
		glPushAttrib(GL_ALL_ATTRIB_BITS);
		glCallList(this.displayList);
		
		
		// doors are nonstationary so need to be rendered dynamically
		for (Entry<ViewableDoor, Float> e : doorRotations.entrySet()) {
			ViewableDoor door = e.getKey();
			float angle = e.getValue();
			
			glPushMatrix();
			glTranslatef(door.getPosition().getX(), door.getOpenPercent()*DoorFrameModel.DOOR_HEIGHT, door.getPosition().getY());
			glRotatef(angle, 0, -1, 0);
			doorSurface.render();
			glPopMatrix();
			
		}
		glPopAttrib();
	}



	private static final float WALL_HEIGHT = 11;

	
	/** What size to make the quads that make up a wall.
	 * Walls must be tessellated for secular light to display
	 * correctly.
	 */
	private static final float TESSELLATION_SIZE = 1;

	/** How many tessellated squares does a wall texture cover */
	private static final float TEXTURE_TESSELLATION_MULTIPLE = 11;

	private static int wallTexture;
	private static int floorTexture;
	private static int ceilingTexture;

	private static Material wallMaterial;
	private static Material ceilingMaterial;
	//private static int doorDisplayList;
	//private static int doorTexture;
	private static DoorFrameModel doorFrame;
	private static DoorSurfaceModel doorSurface;

	public static void loadModels() {
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
			wallTexture = TextureLoader.loadTexture(new File("./assets/wall textured evened.png"));
			floorTexture = TextureLoader.loadTexture(new File("./assets/floor maybs.png"));
			ceilingTexture = TextureLoader.loadTexture(new File("./assets/shiphull.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		doorFrame = new DoorFrameModel();
		doorSurface = new DoorSurfaceModel();
	}

	private int createDisplayList(ViewableRoom room, ModelFlyweight models){
		int displayList = glGenLists(1);
		glNewList(displayList, GL_COMPILE);
		glPushAttrib(GL_ALL_ATTRIB_BITS);

		wallMaterial.apply();
		//Material.simple.apply(); //silver.apply();

		glColor3d(0.20, 0.2, 0.22);


		glDisable(GL_COLOR_MATERIAL);
		glEnable(GL_TEXTURE_2D);

		glBindTexture(GL_TEXTURE_2D, wallTexture);

		glBegin(GL_QUADS);
		
		for (ViewableWall wall : room.getWalls()) {
			
			List<? extends ViewableDoor> doors = wall.getDoors();
			
			float x1 = wall.getStart().getX();
			float x2 = wall.getEnd().getX();

			float z1 = wall.getStart().getY();
			float z2 = wall.getEnd().getY();

			// normal of a line segment (the wall)
			Vector3D normal = new Vector3D(1 * (z2 - z1), 0, -1 * (x2 - x1)).normalized();

			glNormal3f(normal.getX(), normal.getY(), normal.getZ());

			renderWall(wall, doors);
			
			Vector2D wallVector2D = wall.getEnd().sub(wall.getStart());
			float angle = (float) Math.toDegrees(wallVector2D.getPolarAngle());
			for (ViewableDoor door : doors){
				this.doorRotations.put(door, angle);
			}
		}
		glEnd();
		
		glBindTexture(GL_TEXTURE_2D, floorTexture);
		ceilingMaterial.apply();
		glNormal3f(0,1,0);
		glBegin(GL_QUADS);
		renderYPlane(room, 0, true);
		glEnd();

		glBindTexture(GL_TEXTURE_2D, ceilingTexture);
		ceilingMaterial.apply();
		glNormal3f(0,-1,0);
		glBegin(GL_QUADS);
		renderYPlane(room, WALL_HEIGHT, false);
		glEnd();


		glDisable(GL_TEXTURE_2D);
		glEnable(GL_COLOR_MATERIAL);
		for (ViewableObject viewableObject : room.getContainedObjects()){
			if (viewableObject instanceof ViewableStationary){
				GameRenderer.drawObject(viewableObject, models);
			}
		}
		
		for (Entry<ViewableDoor, Float> e : doorRotations.entrySet()) {
			ViewableDoor door = e.getKey();
			float angle = e.getValue();
			glPushMatrix();
			glTranslatef(door.getPosition().getX(), 0, door.getPosition().getY());
			glRotatef(angle, 0, -1, 0);
			doorFrame.render();
			glPopMatrix();
		}

		glPopAttrib();
		glEndList();

		return displayList;
	}
	
	private static void renderWall(ViewableWall r, List<? extends ViewableDoor> doors) {
		// we need to tessellate the wall into squares of TESSELLATION_SIZE * TESSELLATION_SIZE
		// for the length of the wall the vector 'delta' is added to the start vector until
		// we reach past the end of the wall. Once we are past the wall end is then clipped back
		// to end
		
		float tex_step = 1/TEXTURE_TESSELLATION_MULTIPLE;
		
		Vector2D wallVec = r.getEnd().sub(r.getStart());
		Vector2D doorVec = wallVec.normalized().mul(DoorFrameModel.DOOR_WIDTH/2);
		float lengthTesselations = wallVec.len() / TESSELLATION_SIZE;
		Vector2D delta = wallVec.div(lengthTesselations);
		Vector2D start = r.getStart();

		float left_tex = 1;
		for (int l=0;l<Math.ceil(lengthTesselations);l++){
			Vector2D end = start.add(delta);
			
			Vector2D drawStart = start;
			Vector2D drawEnd = end;
			
			if (l+1>lengthTesselations){
				end = r.getEnd();
				drawEnd = r.getEnd();
			}
			float xtc = end.sub(start).len() / TEXTURE_TESSELLATION_MULTIPLE;

			
			boolean quadInDoor = false;
			for (ViewableDoor door : doors){
				Vector2D doorLoc = door.getPosition();
				Vector2D doorLeft = doorLoc.sub(doorVec);
				Vector2D doorRight = doorLoc.add(doorVec);
				Segment2D s = new Segment2D(doorLeft, doorRight);
				if (s.onLine(drawStart)){
					if (s.onLine(drawEnd)){
						// both start and end are in the door, so don't draw
						// the quad
						quadInDoor = true;
					} else {
						// start in the door but end is not
						drawStart = doorRight;
					}
				} else if (s.onLine(drawEnd)){
					// end is in the door but start is not
					drawEnd = doorLeft;
				}
			}

			
			float yStep = TESSELLATION_SIZE;
			float top_tex = 1;
			float wallBottom = 0;
			if (quadInDoor){
				wallBottom = DoorFrameModel.DOOR_HEIGHT;
				top_tex -= tex_step * DoorFrameModel.DOOR_HEIGHT;
			}
			for (float y=wallBottom;y<WALL_HEIGHT;y+=TESSELLATION_SIZE){
				if (y+TESSELLATION_SIZE > WALL_HEIGHT) yStep = WALL_HEIGHT % TESSELLATION_SIZE;
				float ytc = yStep/TEXTURE_TESSELLATION_MULTIPLE;
				
				if (y >= DoorFrameModel.DOOR_HEIGHT){
					// if we are above the door then we can 
					// ignore clamping the left and right textures
					drawEnd = end;
					drawStart = start;
				}

				glTexCoord2f(left_tex, top_tex); // bottom left
				glVertex3d(drawStart.getX(), y, drawStart.getY());

				glTexCoord2f(left_tex, top_tex-ytc); //top left
				glVertex3d(drawStart.getX(), y+yStep, drawStart.getY());

				glTexCoord2f(left_tex-xtc, top_tex-ytc); // top right
				glVertex3d(drawEnd.getX(), y+yStep, drawEnd.getY());

				glTexCoord2f(left_tex-xtc, top_tex); // bottom right
				glVertex3d(drawEnd.getX(), y, drawEnd.getY());

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

	private static void renderYPlane(ViewableRoom room, float yVal, boolean windClockwise) {
		float texStep = 1/TEXTURE_TESSELLATION_MULTIPLE;
		Vector2D tl = room.getAABBTopLeft();
		Vector2D br = room.getAABBBottomRight();
		for (float xt = tl.getX();xt<br.getX();xt+=TEXTURE_TESSELLATION_MULTIPLE){
			for (float yt = tl.getY();yt<br.getY();yt+=TEXTURE_TESSELLATION_MULTIPLE){
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