package space.gui.pipeline;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Canvas;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import space.gui.pipeline.mock.Bunny;
import space.gui.pipeline.mock.MockPlayer;
import space.gui.pipeline.mock.MockWorld;
import space.gui.pipeline.viewable.ViewableObject;
import space.gui.pipeline.viewable.ViewablePlayer;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.gui.pipeline.viewable.ViewableWord;
import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.gui.pipeline.wavefront.WavefrontModel;
import space.math.Vector2D;
import space.math.Vector3D;

public class GameRenderer {

	private static final float VERTICAL_FIELD_OF_VIEW = 50.0f;

	private int height;
	private int width;
	private int test;

	private Map<Class<? extends ViewableObject>, Integer> models;
	private Map<ViewableRoom, Integer> roomModels;
	
	public GameRenderer(int width, int height) {
		this.width = width;
		this.height = height;
		
		System.out.println(getHorizontalFOV());
	}

	public void loadModels(ViewableWord world) {
		this.models = new HashMap<Class<? extends ViewableObject>, Integer>();
		try {
			models.put(Bunny.class, WavefrontModel.loadDisplayList(new File("./assets/models/bunny_new.obj"), new Vector3D(0,0,0), new Vector3D(0,180,0), 0.2f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		roomModels = new HashMap<>();
		for (ViewableRoom room : world.getViewableRooms()){
			roomModels.put(room, RoomModel.createDisplayList(room, models));
		}
		
	}

	private void setCamera(Vector3D eyepos, Vector3D look) {
		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(	VERTICAL_FIELD_OF_VIEW, (float)width/(float)height, 1f, 1000f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		GLU.gluLookAt( 	eyepos.getX(), 					eyepos.getY(), 				eyepos.getZ(),
						eyepos.getX() + look.getX(), 	eyepos.getY()+look.getY(), 	eyepos.getZ()+look.getZ(),
						0,  							1, 							0);
	}
	
	public float getHorizontalFOV(){
		float verticalPixels = (float) Math.tan( Math.toRadians(VERTICAL_FIELD_OF_VIEW) / 2f);
		float horizontalPixels = ((float)width/(float)height) * verticalPixels;
		return (float) Math.toDegrees(Math.atan( horizontalPixels ) * 2);
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
		if (models == null) throw new IllegalStateException("models have not yet been loaded");
		
		Vector2D playerPos = player.getPosition();
		ViewableRoom currentRoom = world.getRoomAt(playerPos);
		
		glClearColor(0, 0, 0, 0);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glShadeModel(GL_SMOOTH);
		glEnable(GL_LIGHTING);
		glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_CULL_FACE);
	

		setCamera(new Vector3D(playerPos.getX(), player.getEyeHeight(), playerPos.getY()), player.getLookDirection());
		setLight(currentRoom);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glPushMatrix();

		glCallList(roomModels.get(currentRoom));

		//glEnable(GL_NORMALIZE);
		glEnable(GL_LIGHTING);
		glShadeModel(GL_SMOOTH);
		
		for (ViewableObject vob : currentRoom.getContainedObjects()){
			if (vob.canMove()){
				drawObject(vob, models);
			}
		}
		
		//glDisable(GL_NORMALIZE);
		
		glPopMatrix();
	}

	public static void drawObject(ViewableObject vob, Map<Class<? extends ViewableObject>, Integer> models) {
		glPushMatrix();
		glTranslatef(vob.getPosition().getX(), vob.getElevation(), vob.getPosition().getY());
		glRotated(vob.getAngle(), 0, -1, 0);
		
		// TODO remove this when we have textures
		getAssignedColor(vob);
		
		
		glCallList(models.get(vob.getClass()));
		glPopMatrix();
	}

	private static HashMap<ViewableObject, Vector3D> colors = new HashMap<ViewableObject, Vector3D>();
	private static void getAssignedColor(ViewableObject vob) {
		if (!colors.containsKey(vob)){
			float r = (float) Math.random();
			float b = (float) Math.random();
			float g = (float) Math.random();
			
			colors.put(vob, new Vector3D(r,g,b));
		}
		Vector3D c = colors.get(vob);
		glColor3f(c.getX(), c.getY(), c.getZ());
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
