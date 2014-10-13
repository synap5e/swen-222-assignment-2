package space.gui.pipeline;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Canvas;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import space.gui.pipeline.models.BulletModel;
import space.gui.pipeline.models.RenderModel;
import space.gui.pipeline.models.RoomModel;
import space.gui.pipeline.models.WavefrontModel;
import space.gui.pipeline.viewable.ViewableBeam;
import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewableNonStationary;
import space.gui.pipeline.viewable.ViewableObject;
import space.gui.pipeline.viewable.ViewablePlayer;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWorld;
import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.math.Vector2D;
import space.math.Vector3D;
import space.world.Key;
import space.world.Player;

/**
 *
 * @author Simon Pinfold
 *
 */
public class GameRenderer {

	private static final float VERTICAL_FIELD_OF_VIEW = 50.0f;

	public static final boolean DEBUG_MODELS = false;

	private int height;
	private int width;

	private ModelFlyweight models;
	private Map<ViewableRoom, RoomModel> roomModels;

	public GameRenderer(int width, int height) {
		this.width = width;
		this.height = height;

		System.out.println(getHorizontalFOV());

	}

	public void loadModels(ViewableWorld world) {
		this.models = new ModelFlyweight();// new HashMap<Class<? extends ViewableObject>, RenderModel>();
		
		RoomModel.loadModels();
		roomModels = new HashMap<>();
		for (ViewableRoom room : world.getViewableRooms()){
			roomModels.put(room, new RoomModel(room, models));
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

	private void setLight(ViewablePlayer player, ViewableRoom currentRoom) {
		FloatBuffer zeroBuff = BufferUtils.createFloatBuffer(4);
		zeroBuff.put(new float[] {0,0,0, 1f });
		zeroBuff.flip();

		glLightModel(GL_LIGHT_MODEL_AMBIENT, zeroBuff);

		FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
		float r = currentRoom.getLight().getX();
		float g = currentRoom.getLight().getY();
		float b = currentRoom.getLight().getZ();
		ambient.put(new float[] { r/2.f, g/2.f, b/2.f, 1f });
		ambient.flip();    

		FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
		diffuse.put(new float[] { r, g, b, 1f });
		diffuse.flip();   
		
		FloatBuffer position = BufferUtils.createFloatBuffer(4);
		position.put(new float[] { currentRoom.getCentre().getX(), 9, currentRoom.getCentre().getY(), 1f });
		position.flip();

		glLight(GL_LIGHT0, GL_POSITION, position);
		glLight(GL_LIGHT0, GL_DIFFUSE, diffuse);
		glLight(GL_LIGHT0, GL_AMBIENT, ambient);

		glEnable(GL_LIGHT0);

		if (player != null){
			Vector3D dir = player.getLookDirection().normalized();
			Vector2D pos = player.getPosition();
	
			FloatBuffer spotlightPosition = BufferUtils.createFloatBuffer(4);
			spotlightPosition.put(new float[] { pos.getX(), player.getEyeHeight(), pos.getY(), 1 });
			spotlightPosition.flip();
	
			FloatBuffer spotlightDirection = BufferUtils.createFloatBuffer(4);
			spotlightDirection.put(new float[] { dir.getX(),dir.getY(),dir.getZ(), 0 });
			spotlightDirection.flip();
	
			FloatBuffer spotlightIntensity = BufferUtils.createFloatBuffer(4);
			spotlightIntensity.put(new float[] { 10,10,10, 1 });
			spotlightIntensity.flip();
	
	
			glLight(GL_LIGHT1, GL_POSITION, position);
			glLight(GL_LIGHT1, GL_DIFFUSE, spotlightIntensity);
			glLight(GL_LIGHT1, GL_SPECULAR, spotlightIntensity);
			glLight(GL_LIGHT1, GL_POSITION, spotlightPosition);
			glLight(GL_LIGHT1, GL_AMBIENT, zeroBuff);
			glLightf(GL_LIGHT1,GL_SPOT_CUTOFF,90.0f);
			glLight(GL_LIGHT1, GL_SPOT_DIRECTION, spotlightDirection);
			glLightf(GL_LIGHT1, GL_SPOT_EXPONENT, 100f);
	
			if (player.isTorchOn()){
				glEnable(GL_LIGHT1);
			} else {
				glDisable(GL_LIGHT1);
			}
		} else {
			glDisable(GL_LIGHT1);
		}


	}


	public void renderTick(float timestep, ViewablePlayer player, ViewableWorld world){
		if (models == null) throw new IllegalStateException("models have not yet been loaded");

		Vector2D playerPos = player.getPosition();
		ViewableRoom currentRoom = world.getRoomAt(playerPos);

		glClearColor(0, 0, 0, 0);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glShadeModel(GL_SMOOTH);
		glEnable(GL_LIGHTING);
		//glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_CULL_FACE);
		glTexEnvi (GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);

		setCamera(new Vector3D(playerPos.getX(), player.getEyeHeight(), playerPos.getY()), player.getLookDirection());
		setLight(player, currentRoom);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glPushMatrix();

		Set<ViewableRoom> roomsToRender = new HashSet<>();
		roomsToRender.add(currentRoom);
		for (ViewableDoor door : currentRoom.getAllDoors()){
			if (door.getOpenPercent() > 0){
				roomsToRender.add(door.getRoom1());
				roomsToRender.add(door.getRoom2());
			}
		}
		
		
		for (ViewableRoom room : roomsToRender){
			if (room != null){
				setLight(player, room);
				roomModels.get(room).render(models);
			}
		}
		
		
		glPushAttrib(GL_ALL_ATTRIB_BITS);
		glDisable(GL_LIGHTING);
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		for (ViewableRoom room : roomsToRender){
			if (room != null){
				for (ViewableBeam beam : room.getBeams()){
					Vector3D kUnit = new Vector3D(0, 0, 1);
					Vector3D beamDirection = beam.getBeamDirection();
					
					Vector3D axis = kUnit.cross(beamDirection);
					float angle = kUnit.angleTo(beamDirection);
					
					Cylinder c = new Cylinder();
					
					glColor4f(1,0,0, 0.4f * Math.max(0, Math.min(1, beam.getRemainingLife())));
					glPushMatrix();
					glTranslatef(beam.getPosition().getX(), beam.getElevation(), beam.getPosition().getY());
					glRotatef((float)Math.toDegrees(angle), axis.getX(), axis.getY(), axis.getZ());
					c.draw(0.02f, 0.02f, 50, 10, 10);
					glPopMatrix();
				}
			}
		}
		

		glPopAttrib();

		glPopMatrix();
	}


}
