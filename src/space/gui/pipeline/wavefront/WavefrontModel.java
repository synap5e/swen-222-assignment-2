package space.gui.pipeline.wavefront;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import space.util.Vec3;

public class WavefrontModel {

	private static class Face {
		int v1, v2, v3;
		int n1, n2, n3;
	}

	private ArrayList<Vec3> vertices = new ArrayList<Vec3>();
	private ArrayList<Vec3> normals = new ArrayList<Vec3>();
	private ArrayList<Face> faces = new ArrayList<Face>();
	
	public WavefrontModel(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		while ((line = br.readLine()) != null) {
		   parseLine(line);
		}
		br.close();
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
			// TODO: do we want to generate normals or fail
		}
		
		faces.add(f);
	}

	private void addNormal(String[] elems) {
		normals.add(parseVec3(elems));
	}

	private void addVertex(String[] elems) {
		normals.add(parseVec3(elems));
	}
	
	private Vec3 parseVec3(String[] elems) {
		return new 	Vec3(
						Float.parseFloat(elems[1]),
						Float.parseFloat(elems[2]),
						Float.parseFloat(elems[3])
					);
	}
	
}
