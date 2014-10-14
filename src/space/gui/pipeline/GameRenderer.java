package space.gui.pipeline;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import space.gui.pipeline.models.RoomModel;
import space.gui.pipeline.viewable.ViewableBeam;
import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewablePlayer;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWorld;
import space.math.Vector2D;
import space.math.Vector3D;

/** The renderer for the game. Uses a ViewableWorld and a ViewablePlayer to output the view from
 * the ViewablePlayer's eyes to the lwjgl Display
 *
 * @author Simon Pinfold (300280028)
 *
 */
public class GameRenderer {

	/**
	 * The verticle field of view in degrees
	 */
	private static final float VERTICAL_FIELD_OF_VIEW = 50.0f;

	/**
	 * Whether to debug the collision "hulls" and centres of objects in their models
	 */
	public static final boolean DEBUG_MODELS = false;

	private int height;
	private int width;

	/**
	 * The ViewableObject.type -> RenderModel mapping
	 */
	private ModelFlyweight models;
	private Map<ViewableRoom, RoomModel> roomModels;

	public GameRenderer(int width, int height) {
		this.width = width;
		this.height = height;

	}

	/**
	 * Load the models that are required for the rendering.
	 *
	 * This requires that the OpenGL context has been created, so will not be called from the constructor
	 * @param world
	 */
	public void loadModels(ViewableWorld world) {
		this.models = new ModelFlyweight();

		roomModels = new HashMap<>();
		for (ViewableRoom room : world.getViewableRooms()){
			roomModels.put(room, new RoomModel(room, models));
		}
	}

	/**
	 * Set the camera to the eye position looking in the specified direction
	 * @param eyepos the eye position of the character
	 * @param look the look direction of the character
	 */
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

	/**
	 * Get the horizontal field of view in degrees
	 * @return the horzontal FOV
	 */
	public float getHorizontalFOV(){
		float verticalPixels = (float) Math.tan( Math.toRadians(VERTICAL_FIELD_OF_VIEW) / 2f);
		float horizontalPixels = ((float)width/(float)height) * verticalPixels;
		return (float) Math.toDegrees(Math.atan( horizontalPixels ) * 2);
	}

	/**
	 * Set the lighting parameters to the head of the attribute stack to that of the currect room,
	 * and the player's torch
	 * @param player the player who's torch to use for the spotlight
	 * @param currentRoom the room to use the light for
	 */
	private void setLight(ViewablePlayer player, ViewableRoom currentRoom) {
		FloatBuffer zeroBuff = BufferUtils.createFloatBuffer(4);
		zeroBuff.put(new float[] {0,0,0, 1f });
		zeroBuff.flip();

		// no universal ambient light
		glLightModel(GL_LIGHT_MODEL_AMBIENT, zeroBuff);

		// the ambient light for light0, the room's light
		FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
		float r = currentRoom.getLight().getX();
		float g = currentRoom.getLight().getY();
		float b = currentRoom.getLight().getZ();
		ambient.put(new float[] { r/2.f, g/2.f, b/2.f, 1f });
		ambient.flip();

		// the difuse light for light0, the room's light
		FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
		diffuse.put(new float[] { r, g, b, 1f });
		diffuse.flip();

		// the position of the light in the room
		FloatBuffer position = BufferUtils.createFloatBuffer(4);
		position.put(new float[] { currentRoom.getCentre().getX(), 9, currentRoom.getCentre().getY(), 1f });
		position.flip();

		glLight(GL_LIGHT0, GL_POSITION, position);
		glLight(GL_LIGHT0, GL_DIFFUSE, diffuse);
		glLight(GL_LIGHT0, GL_AMBIENT, ambient);

		glEnable(GL_LIGHT0);

		// if we have a player then set up light1 as a spotlight to be their torch
		if (player != null){
			Vector3D dir = player.getLookDirection().normalized();
			Vector2D pos = player.getPosition();

			// the position of the light is the player's eye
			FloatBuffer spotlightPosition = BufferUtils.createFloatBuffer(4);
			spotlightPosition.put(new float[] { pos.getX(), player.getEyeHeight(), pos.getY(), 1 });
			spotlightPosition.flip();

			// the direction is the way the player is facing
			FloatBuffer spotlightDirection = BufferUtils.createFloatBuffer(4);
			spotlightDirection.put(new float[] { dir.getX(),dir.getY(),dir.getZ(), 0 });
			spotlightDirection.flip();

			// high intensity
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

			// only enable the spotlight if the torch is on
			if (player.isTorchOn()){
				glEnable(GL_LIGHT1);
			} else {
				glDisable(GL_LIGHT1);
			}
		} else {
			glDisable(GL_LIGHT1);
		}


	}


	/**
	 * Render the current state of the provided world from the viewpoint of the provided player
	 * @param player the player to view the world from, and who has the torch that we will use
	 * @param world the world the player is in that we want to render
	 */
	public void renderTick(ViewablePlayer player, ViewableWorld world){
		if (models == null) throw new IllegalStateException("models have not yet been loaded");

		Vector2D playerPos = player.getPosition();
		ViewableRoom currentRoom = world.getRoomAt(playerPos);

		// black background. Seen when we look thought multiple levels of doors
		glClearColor(0, 0, 0, 0);

		// global attributes
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glShadeModel(GL_SMOOTH);
		glEnable(GL_LIGHTING);
		glEnable(GL_CULL_FACE);
		glTexEnvi (GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);

		// set the camera to the player's eye
		setCamera(new Vector3D(playerPos.getX(), player.getEyeHeight(), playerPos.getY()), player.getLookDirection());

		// clear the screen
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glPushMatrix();

		// work out what rooms we need to render
		// this is the current room and all rooms with an open door to that room
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
				// set the light for the current room
				setLight(player, room);

				// render the room and all objects inside it
				roomModels.get(room).render(models);
			}
		}

		// draw beams
		// they get drawn without lighting and semi-transparent
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

					// color intensity dependant on beam life
					glColor4f(1,0,0, 0.4f * Math.max(0, Math.min(1, beam.getRemainingLife())));

					// transform so the new Z axis is the beams's direction
					// then draw a long cylinder along z
					glPushMatrix();
					glTranslatef(beam.getPosition().getX(), beam.getElevation(), beam.getPosition().getY());
					glRotatef((float)Math.toDegrees(angle), axis.getX(), axis.getY(), axis.getZ());
					c.draw(0.02f, 0.02f, 50, 10, 10);
					glPopMatrix();
				}
			}
		}

		// restore the state of opengl to application code is not affected by the game renderer
		glPopAttrib();
		glPopMatrix();
	}


}
