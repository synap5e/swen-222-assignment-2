package roomtest;

import static org.lwjgl.opengl.GL11.*;

import java.awt.geom.Point2D;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.Point;
import org.lwjgl.util.glu.GLU;

public class Camera {

	private int windowWidth;
	private int windowHeight;
	private Room world;
	
	public Camera(int windowWidth, int windowHeight, Room world) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.world = world;
	}

	public void init() {
		glClearColor(0, 0, 0, 0);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glShadeModel(GL_FLAT);
		glEnable(GL_LIGHTING);
		glEnable(GL_COLOR_MATERIAL);
	}
	
	void setCamera(Vec3 eyepos, Vec3 look) {
		glViewport(0, 0, windowWidth, windowHeight);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(	60.0f, (float)windowWidth/(float)windowHeight, 1f, 1000f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		
		GLU.gluLookAt( 	eyepos.x, eyepos.y, eyepos.z,
						eyepos.x + look.x, eyepos.y+look.y, eyepos.z+look.z,
						0,  1, 0);
	}

	public void render(Vec3 eyepos, Vec3 look) {
		setCamera(eyepos, look);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glShadeModel(GL_FLAT);
		glEnable(GL_LIGHTING);
		glEnable(GL_COLOR_MATERIAL);
		setLight();
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glPushMatrix();
		
		glBegin(GL_QUADS);
		glColor3d(0.5, 0.5, 0.5);
		for (Wall r : world.getWalls()) {
			Vec3 normal = new Vec3(1 * (r.z2 - r.z1), 0, -1 * (r.x2 - r.x1)).normalized();

			glNormal3f(normal.x, normal.y, normal.z);

			glVertex3d(r.x1, 0, r.z1);
			glVertex3d(r.x1, 10, r.z1);
			glVertex3d(r.x2, 10, r.z2);
			glVertex3d(r.x2, 0, r.z2);
		}
		glEnd();
		
		glColor3d(0.3, 0.3, 0.3);
		glNormal3f(0,1,0);
		glBegin(GL_TRIANGLE_FAN);
		glVertex3f(0, 0, 0);
		for (Wall r : world.getWalls()) {
			glVertex3d(r.x1, 0, r.z1);
			glVertex3d(r.x2, 0, r.z2);
		}
		glEnd();

		glPopMatrix();
		

	}

	private void setLight() {
		/* glLightModeli(GL_LIGHT_MODEL_LOCAL_VIEWER, GL_TRUE);
		 glEnable(GL_LIGHTING);
		 glEnable(GL_LIGHT0);

		 FloatBuffer qaAmbientLight  = floatBuffer(0.2f, 0.2f, 0.2f, 1.0f);
		 FloatBuffer qaDiffuseLight  = floatBuffer(1.0f, 1.0f, 1.0f, 1.0f);
		 FloatBuffer qaSpecularLight = floatBuffer(1.0f, 1.0f, 1.0f, 1.0f);

		 glLight(GL_LIGHT0, GL_AMBIENT, qaAmbientLight);
		 glLight(GL_LIGHT0, GL_DIFFUSE, qaDiffuseLight);
		 glLight(GL_LIGHT0, GL_SPECULAR, qaSpecularLight);

		 FloatBuffer qaLightPosition = floatBuffer(0, 5, 5, 1.0f);
		 glLight(GL_LIGHT0, GL_POSITION, qaLightPosition);*/
		
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

	public void drawCube() {
		glBegin(GL_QUADS);
		
		glColor3f(1, 0, 0);
		glVertex3f(-1, -1, 1);
		glVertex3f(1, -1, 1);
		glVertex3f(1, 1, 1);
		glVertex3f(-1, 1, 1);
		//left
		
		glColor3f(0, 1, 0);
		glVertex3f(-1, -1, -1);
		glVertex3f(-1, -1, 1);
		glVertex3f(-1, 1, 1);
		glVertex3f(-1, 1, -1);
		//right
		
		glColor3f(0, 0, 1);
		glVertex3f(1, -1, -1);
		glVertex3f(1, 1, -1);
		glVertex3f(1, 1, 1);
		glVertex3f(1, -1, 1);
		//front
		
		glColor3f(1, 1, 0);
		glVertex3f(-1, -1, -1);
		glVertex3f(1, -1, -1);
		glVertex3f(1, -1, 1);
		glVertex3f(-1, -1, 1);
		//back
		
		glColor3f(1, 0, 1);
		glVertex3f(1, 1, -1);
		glVertex3f(-1, 1, -1);
		glVertex3f(-1, 1, 1);
		glVertex3f(1, 1, 1);
		//bottom
		
		glColor3f(0, 1, 1);
		glVertex3f(1, -1, -1);
		glVertex3f(-1, -1, -1);
		glVertex3f(-1, 1, -1);
		glVertex3f(1, 1, -1);
	
		glEnd();
	}

}
