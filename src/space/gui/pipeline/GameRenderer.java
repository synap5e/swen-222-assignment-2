package space.gui.pipeline;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Canvas;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import space.gui.pipeline.ViewableRoom.LightMode;
import space.gui.pipeline.mock.MockPlayer;
import space.gui.pipeline.mock.MockWorld;
import space.util.Vec2;
import space.util.Vec3;

public class GameRenderer {

	private static final int WALL_HEIGHT = 10;
	private static final float FIELD_OF_VIEW = 50.0f;
	private static final float EYE_HEIGHT = 8;

	private int height;
	private int width;
	
	public GameRenderer(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void init(){
		glClearColor(0, 0, 0, 0);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glShadeModel(GL_FLAT);
		glEnable(GL_LIGHTING);
		glEnable(GL_COLOR_MATERIAL);
	}

	private void setCamera(Vec3 eyepos, Vec3 look) {
		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(	FIELD_OF_VIEW, (float)width/(float)height, 1f, 1000f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();


		GLU.gluLookAt( 	eyepos.getX(), 					eyepos.getY(), 				eyepos.getZ(),
						eyepos.getX() + look.getX(), 	eyepos.getY()+look.getY(), 	eyepos.getZ()+look.getZ(),
						0,  							1, 							0);
	}
	
	private void setLight(ViewableRoom currentRoom) {
		
		FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
		ambient.put(new float[] { 0.2f, 0.2f, 0.2f, 1f, });
		ambient.flip();    

		FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
		diffuse.put(new float[] { 0.7f, 0.7f, 0.7f, 1f, });
		diffuse.flip();   
		
		FloatBuffer position = BufferUtils.createFloatBuffer(4);
		position.put(new float[] { 0f, 10f, 0f, 1f, });
		position.flip();    

		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
//		glLightModel(GL_LIGHT_MODEL_AMBIENT, ambient);
		
		glLight(GL_LIGHT0, GL_POSITION, position);
		glLight(GL_LIGHT0, GL_DIFFUSE, diffuse);
		glLight(GL_LIGHT0, GL_AMBIENT, ambient);
	}
	
	public void renderTick(float timestep, ViewablePlayer player, ViewableWord world){
		Vec2 playerPos = player.getPosition();
		ViewableRoom currentRoom = world.getRoomAt(playerPos);

		setCamera(new Vec3(playerPos.getX(), player.getEyeHeight(), playerPos.getY()), player.getLookDirection());

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glShadeModel(GL_FLAT);
		glEnable(GL_LIGHTING);
		glEnable(GL_COLOR_MATERIAL);
		setLight(currentRoom);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glPushMatrix();

		renderWalls(currentRoom);


		
		
		glPopMatrix();
	}

	private void renderWalls(ViewableRoom currentRoom) {
		glBegin(GL_QUADS);
		glColor3d(0.5, 0.5, 0.5);
		for (ViewableWall r : currentRoom.getWalls()) {
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
		for (ViewableWall r : currentRoom.getWalls()) {
			glVertex3d(r.getStart().getX(), 0, r.getStart().getY());
			glVertex3d(r.getEnd().getX(), 0, r.getEnd().getY());
		}
		glEnd();

		glColor3d(0.3, 0.3, 0.3);
		glNormal3f(0,-1,0);
		glBegin(GL_TRIANGLE_FAN);
		glVertex3f(0, 10, 0);
		for (ViewableWall r : currentRoom.getWalls()) {
			glVertex3d(r.getStart().getX(), 10, r.getStart().getY());
			glVertex3d(r.getEnd().getX(), 10, r.getEnd().getY());
		}
		glEnd();
	}


	public void setParent(Canvas c) throws LWJGLException {
		Display.setParent(c);
		Display.create();
	}
	
	/*public static void main(String[] args) throws LWJGLException{
		int windowWidth = 1800;
		int windowHeight = 900;
		
		Display.setDisplayMode(new DisplayMode(windowWidth, windowHeight));
		Display.create();

		GameRenderer r = new GameRenderer(windowWidth, windowHeight);
		MockPlayer mockPlayer = new MockPlayer();
		ViewableWord mockWorld = new MockWorld();


		long last = getTime();
		while (!Display.isCloseRequested()) {
			long now = getTime();
			int delta = (int)(now - last);
			last = now;

			mockPlayer.update(delta);
			r.renderTick(delta, mockPlayer, mockWorld);

			Display.update();
			Display.sync(60);

		}
		Display.destroy();
		
	}
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
*/
}
