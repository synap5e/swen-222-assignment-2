package space.gui.pipeline;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Map;

import org.lwjgl.BufferUtils;

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
				40f
		);
		ceilingMaterial = new Material(
				new Vector3D(0.4f, 0.4f, 0.4f),
				new Vector3D(1, 1, 1),
				new Vector3D(0.3f, 0.3f, 0.3f),
				40f
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
	
	public static int createDisplayList(ViewableRoom room, Map<Class<? extends ViewableObject>, Integer> models){
		int displayList = glGenLists(1);
		glNewList(displayList, GL_COMPILE);
		
		glEnable(GL_LIGHTING);
		
		
		
		wallMaterial.apply();
		//Material.simple.apply(); //silver.apply();
		
		glColor3d(0.20, 0.2, 0.22);
		
		
		glDisable(GL_COLOR_MATERIAL);
		glEnable(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, wallTexture);
		
		glBegin(GL_QUADS);
		float tex_step = 1/TEXTURE_TESSELLATION_MULTIPLE;
		for (ViewableWall r : room.getWalls()) {
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
		
		
		// TODO: to make specular work on the floor it needs to be tessellated.
		// This method just draws a 100x100 tesselated square on the floor.
		// instead it should only go to the walls
		//glBindTexture(GL_TEXTURE_2D, floorTexture);
		ceilingMaterial.apply();
		glNormal3f(0,1,0);
		glBegin(GL_QUADS);
		//floor
		for (float xt = -50;xt<50;xt+=TEXTURE_TESSELLATION_MULTIPLE){
			for (float yt = -50;yt<50;yt+=TEXTURE_TESSELLATION_MULTIPLE){
				
				for (float x=0;x<TEXTURE_TESSELLATION_MULTIPLE/TESSELLATION_SIZE;x++){
					for (float y=0;y<TEXTURE_TESSELLATION_MULTIPLE/TESSELLATION_SIZE;y++){
						glTexCoord2f(tex_step*x, tex_step*(y+1));
						glVertex3f(xt+TESSELLATION_SIZE*x, 0, yt+TESSELLATION_SIZE*(y+1));
						
						glTexCoord2f(tex_step*(x+1), tex_step*(y+1));
						glVertex3f(xt+TESSELLATION_SIZE*(x+1), 0, yt+TESSELLATION_SIZE*(y+1));
						
						glTexCoord2f(tex_step*(x+1), tex_step*y);
						glVertex3f(xt+TESSELLATION_SIZE*(x+1), 0, yt+TESSELLATION_SIZE*y);
						
						glTexCoord2f(tex_step*x, tex_step*y);
						glVertex3f(xt+TESSELLATION_SIZE*x, 0, yt+TESSELLATION_SIZE*y);
					}
				}
			}
		}
		
		
		// ceiling
		//glBindTexture(GL_TEXTURE_2D, ceilingTexture);
	//	Material.simple.apply();
		ceilingMaterial.apply();
		glNormal3f(0,-1,0);
		for (float xt = -50;xt<50;xt+=TEXTURE_TESSELLATION_MULTIPLE){
			for (float yt = -50;yt<50;yt+=TEXTURE_TESSELLATION_MULTIPLE){
				
				for (float x=0;x<TEXTURE_TESSELLATION_MULTIPLE/TESSELLATION_SIZE;x++){
					for (float y=0;y<TEXTURE_TESSELLATION_MULTIPLE/TESSELLATION_SIZE;y++){
						glTexCoord2f(tex_step*x, tex_step*y);
						glVertex3f(xt+TESSELLATION_SIZE*x, WALL_HEIGHT, yt+TESSELLATION_SIZE*y);
						
						glTexCoord2f(tex_step*(x+1), tex_step*y);
						glVertex3f(xt+TESSELLATION_SIZE*(x+1), WALL_HEIGHT, yt+TESSELLATION_SIZE*y);
						
						glTexCoord2f(tex_step*(x+1), tex_step*(y+1));
						glVertex3f(xt+TESSELLATION_SIZE*(x+1), WALL_HEIGHT, yt+TESSELLATION_SIZE*(y+1));
						
						glTexCoord2f(tex_step*x, tex_step*(y+1));
						glVertex3f(xt+TESSELLATION_SIZE*x, WALL_HEIGHT, yt+TESSELLATION_SIZE*(y+1));
					}
				}
			}
		}
		glEnd();	
		
	/*	// floor
		glColor3d(0.3, 0.3, 0.3);
		glNormal3f(0,1,0);
		glBegin(GL_TRIANGLE_FAN);
		glVertex3f(0, 0, 0);
		for (ViewableWall r : room.getWalls()) {
			glVertex3d(r.getStart().getX(), 0, r.getStart().getY());
			glVertex3d(r.getEnd().getX(), 0, r.getEnd().getY());
		}
		glEnd();*/
/*
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_COLOR_MATERIAL);
		// ceiling
		Material.simple.apply();
		glColor3d(0.3, 0.3, 0.3);
		glNormal3f(0,-1,0);
		glBegin(GL_TRIANGLE_FAN);
		glVertex3f(0, WALL_HEIGHT, 0);
		for (ViewableWall r : room.getWalls()) {
			// reverse winding (end -> start) to the backface is above
			glVertex3d(r.getEnd().getX(), WALL_HEIGHT, r.getEnd().getY());
			glVertex3d(r.getStart().getX(), WALL_HEIGHT, r.getStart().getY());
		}
		glEnd();*/
		
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

}
