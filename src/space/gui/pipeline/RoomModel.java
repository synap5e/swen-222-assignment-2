package space.gui.pipeline;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.Map;

import space.gui.pipeline.viewable.ViewableObject;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.util.Vec3;

public class RoomModel {
	
	private static final float WALL_HEIGHT = 10;
	
	public static int createDisplayList(ViewableRoom room, Map<Class<? extends ViewableObject>, Integer> models){
		int displayList = glGenLists(1);
		glNewList(displayList, GL_COMPILE);
		
		glEnable(GL_LIGHTING);
		glBegin(GL_QUADS);
		glColor3d(0.5, 0.5, 0.5);
		for (ViewableWall r : room.getWalls()) {
			float x1 = r.getStart().getX();
			float x2 = r.getEnd().getX();

			float z1 = r.getStart().getY();
			float z2 = r.getEnd().getY();

			// normal of a line segment (the wall)
			Vec3 normal = new Vec3(1 * (z2 - z1), 0, -1 * (x2 - x1)).normalized();

			glNormal3f(normal.getX(), normal.getY(), normal.getZ());

			glVertex3d(x1, 0, z1);
			glVertex3d(x1, WALL_HEIGHT, z1);
			glVertex3d(x2, WALL_HEIGHT, z2);
			glVertex3d(x2, 0, z2);
		}
		glEnd();


		glDisable(GL_LIGHTING);
		// floor
		glColor3d(0.3, 0.3, 0.3);
		glNormal3f(0,1,0);
		glBegin(GL_TRIANGLE_FAN);
		glVertex3f(0, 0, 0);
		for (ViewableWall r : room.getWalls()) {
			glVertex3d(r.getStart().getX(), 0, r.getStart().getY());
			glVertex3d(r.getEnd().getX(), 0, r.getEnd().getY());
		}
		glEnd();

		// ceiling
		glColor3d(0.3, 0.3, 0.3);
		glNormal3f(0,-1,0);
		glBegin(GL_TRIANGLE_FAN);
		glVertex3f(0, WALL_HEIGHT, 0);
		for (ViewableWall r : room.getWalls()) {
			// reverse winding (end -> start) to the backface is above
			glVertex3d(r.getEnd().getX(), WALL_HEIGHT, r.getEnd().getY());
			glVertex3d(r.getStart().getX(), WALL_HEIGHT, r.getStart().getY());
		}
		glEnd();
		
		glEnable(GL_LIGHTING);
		
		
		for (ViewableObject viewableObject : room.getContainedObjects()){
			if (!viewableObject.canMove()){
				GameRenderer.drawObject(viewableObject, models);
			}
		}
		

		glEndList();
		
		return displayList;
	}

}
