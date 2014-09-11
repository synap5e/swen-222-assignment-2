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

import space.RenderComponent;
import space.gui.pipeline.ViewableRoom.LightMode;
import space.gui.pipeline.mock.MockPlayer;
import space.gui.pipeline.mock.MockWorld;
import space.util.Vec2;
import space.util.Vec3;

public class GameRenderer implements RenderComponent{
	
	private static final float PLAYER_EYE_HEIGHT = 5;
	
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
		GLU.gluPerspective(	50.0f, (float)width/(float)height, 1f, 1000f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		
		GLU.gluLookAt( 	eyepos.getX(), 					eyepos.getY(), 				eyepos.getZ(),
						eyepos.getZ() + look.getX(), 	eyepos.getY()+look.getY(), 	eyepos.getZ()+look.getZ(),
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
		
		setCamera(new Vec3(playerPos.getX(), PLAYER_EYE_HEIGHT, playerPos.getY()), player.getLookDirection());
		
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glShadeModel(GL_FLAT);
		glEnable(GL_LIGHTING);
		glEnable(GL_COLOR_MATERIAL);
		setLight(currentRoom);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glPushMatrix();
		
		
		glPopMatrix();
	}

	

	@Override
	public Canvas createCanvas() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	public static void main(String[] args) throws LWJGLException{
		int windowWidth = 800;
		int windowHeight = 600;
		
		Display.setDisplayMode(new DisplayMode(windowWidth, windowHeight));
		Display.create();
		
		GameRenderer r = new GameRenderer(windowWidth, windowHeight);
		ViewablePlayer mockPlayer = new MockPlayer();
		ViewableWord mockWorld = new MockWorld();
		
			
		float last = getTime();
		while (!Display.isCloseRequested()) {
			float now = getTime();
			int delta = (int)(now = last);
			last = now;
			
			r.renderTick(delta, mockPlayer, mockWorld);
			
			Display.update();
			Display.sync(60);

		}
		Display.destroy();
		
	}
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
}
