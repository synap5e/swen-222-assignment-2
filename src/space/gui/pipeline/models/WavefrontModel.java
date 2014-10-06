package space.gui.pipeline.models;


import static org.lwjgl.opengl.GL11.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.Util;
import org.lwjgl.util.glu.Sphere;

import space.gui.pipeline.Material;
import space.math.Vector3D;

/**
 * 
 * @author Simon Pinfold
 *
 */
public class WavefrontModel implements RenderModel{

	private static class Face {
		int v1, v2, v3;
		int n1, n2, n3;
	}

	private static final boolean DEBUG_MODEL_CENTER = false;

	private ArrayList<Vector3D> vertices = new ArrayList<Vector3D>();
	private ArrayList<Vector3D> normals = new ArrayList<Vector3D>();
	private ArrayList<Face> faces = new ArrayList<Face>();
	
	private float scale;
	private Vector3D eulerRotation;
	private Vector3D offset;
	private Material mat;
	
	private int displayList;

	public WavefrontModel(File f, Vector3D offset, Vector3D eulerRotation, float scale, Material mat) throws IOException {
		this.offset = offset;
		this.eulerRotation = eulerRotation;
		this.scale = scale;
		this.mat = mat;
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		while ((line = br.readLine()) != null) {
		   parseLine(line);
		}
		br.close();
		
		this.createDisplayList();
	}
	
	@Override
	public void render() {
		glCallList(displayList);
	}

	private void parseLine(String line) {
		String[] elems = line.trim().split(" ");
		if (elems[0].equals("v")){
			addVertex(elems);
		} else if (elems[0].equals("vn")){
			addNormal(elems);
		} else if (elems[0].equals("f")){
			addFace(elems);
		}
	}

	private void addFace(String[] elems) {
		Face f = new Face();

		String[] t1 = elems[1].split("/");
		String[] t2 = elems[2].split("/");
		String[] t3 = elems[3].split("/");

		// vertex is always defined first
		f.v1 = Integer.parseInt(t1[0]);
		f.v2 = Integer.parseInt(t2[0]);
		f.v3 = Integer.parseInt(t3[0]);

		if (t1.length >= 2){
			// tn[1] is texture - not implemented yet
		}

		if (t1.length == 3){
			// tn[2] is normal

			f.n1 = Integer.parseInt(t1[2]);
			f.n2 = Integer.parseInt(t2[2]);
			f.n3 = Integer.parseInt(t3[2]);
		} else {

			System.err.println("NO NORMALS");
			// TODO: do we want to generate normals or fail
		}

		faces.add(f);
	}

	private void addNormal(String[] elems) {
		normals.add(parseVec3(elems));
	}

	private void addVertex(String[] elems) {
		Vector3D v = parseVec3(elems);
		v.mulLocal(scale);
		vertices.add(v);
	}

	private Vector3D parseVec3(String[] elems) {
		return new 	Vector3D(
						Float.parseFloat(elems[1]),
						Float.parseFloat(elems[2]),
						Float.parseFloat(elems[3])
					);
	}

	private void createDisplayList(){
		this.displayList = glGenLists(1);
		glNewList(displayList, GL_COMPILE);
		glPushAttrib(GL_ALL_ATTRIB_BITS);
		
		if (DEBUG_MODEL_CENTER){
			glPushAttrib(GL_ALL_ATTRIB_BITS);
			glDisable(GL_TEXTURE_2D);
			glEnable(GL_COLOR_MATERIAL);
			glColor3f(1,0,0);
			Sphere s = new Sphere();
			s.draw(0.1f, 10, 10);
			glPopAttrib();
		}
		
		glTranslatef(offset.getX(), offset.getY(), offset.getZ());
		
		glRotatef(eulerRotation.getZ(), 0, 0, 1);
		glRotatef(eulerRotation.getY(), 0, 1, 0);
		glRotatef(eulerRotation.getX(), 1, 0, 0);
		
		mat.apply();
		
		// TODO: use a given texture
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_COLOR_MATERIAL);
		glBegin(GL_TRIANGLES);
		for (Face f : faces) {

			{
				Vector3D n1 = normals.get(f.n1 - 1);
				Vector3D v1 = vertices.get(f.v1 - 1);
				glNormal3f(n1.getX(), n1.getY(), n1.getZ());
				glVertex3f(v1.getX(), v1.getY(), v1.getZ());
			}

			{
				Vector3D n2 = normals.get(f.n2 - 1);
				Vector3D v2 = vertices.get(f.v2 - 1);
				glNormal3f(n2.getX(), n2.getY(), n2.getZ());
				glVertex3f(v2.getX(), v2.getY(), v2.getZ());
			}

			{
				Vector3D n3 = normals.get(f.n3 - 1);
				Vector3D v3 = vertices.get(f.v3 - 1);
				glNormal3f(n3.getX(), n3.getY(), n3.getZ());
				glVertex3f(v3.getX(), v3.getY(), v3.getZ());
			}
		}
		
		glEnd();

		glPopAttrib();
		glEndList();
	}

}
