package space.gui.pipeline;

import static org.lwjgl.opengl.GL11.GL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.glMaterial;
import static org.lwjgl.opengl.GL11.glMaterialf;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import space.math.Vector3D;

public class Material {

	// materials defined at http://devernay.free.fr/cours/opengl/materials.html

	public static Material emerald = new Material(
			new Vector3D(0.0215f, 0.1745f, 0.0215f),
			new Vector3D(0.07568f, 0.61424f, 0.07568f),
			new Vector3D(0.633f, 0.727811f, 0.633f),
			0.6f
	);
	public static Material jade = new Material(
				new Vector3D(0.135f, 0.2225f, 0.1575f),
				new Vector3D(0.54f, 0.89f, 0.63f),
				new Vector3D(0.316228f, 0.316228f, 0.316228f),
				0.1f
		);
	public static Material obsidian = new Material(
				new Vector3D(0.05375f, 0.05f, 0.06625f),
				new Vector3D(0.18275f, 0.17f, 0.22525f),
				new Vector3D(0.332741f, 0.328634f, 0.346435f),
				0.3f
		);
	public static Material pearl = new Material(
				new Vector3D(0.25f, 0.20725f, 0.20725f),
				new Vector3D(1f, 0.829f, 0.829f),
				new Vector3D(0.296648f, 0.296648f, 0.296648f),
				0.088f
		);
	public static Material ruby = new Material(
				new Vector3D(0.1745f, 0.01175f, 0.01175f),
				new Vector3D(0.61424f, 0.04136f, 0.04136f),
				new Vector3D(0.727811f, 0.626959f, 0.626959f),
				0.6f
		);
	public static Material turquoise = new Material(
				new Vector3D(0.1f, 0.18725f, 0.1745f),
				new Vector3D(0.396f, 0.74151f, 0.69102f),
				new Vector3D(0.297254f, 0.30829f, 0.306678f),
				0.1f
		);
	public static Material brass = new Material(
				new Vector3D(0.329412f, 0.223529f, 0.027451f),
				new Vector3D(0.780392f, 0.568627f, 0.113725f),
				new Vector3D(0.992157f, 0.941176f, 0.807843f),
				0.21794872f
		);
	public static Material bronze = new Material(
				new Vector3D(0.2125f, 0.1275f, 0.054f),
				new Vector3D(0.714f, 0.4284f, 0.18144f),
				new Vector3D(0.393548f, 0.271906f, 0.166721f),
				0.2f
		);
	public static Material chrome = new Material(
				new Vector3D(0.25f, 0.25f, 0.25f),
				new Vector3D(0.4f, 0.4f, 0.4f),
				new Vector3D(0.774597f, 0.774597f, 0.774597f),
				0.6f
		);
	public static Material copper = new Material(
				new Vector3D(0.19125f, 0.0735f, 0.0225f),
				new Vector3D(0.7038f, 0.27048f, 0.0828f),
				new Vector3D(0.256777f, 0.137622f, 0.086014f),
				0.1f
		);
	public static Material gold = new Material(
				new Vector3D(0.24725f, 0.1995f, 0.0745f),
				new Vector3D(0.75164f, 0.60648f, 0.22648f),
				new Vector3D(0.628281f, 0.555802f, 0.366065f),
				0.4f
		);
	public static Material silver = new Material(
				new Vector3D(0.19225f, 0.19225f, 0.19225f),
				new Vector3D(0.50754f, 0.50754f, 0.50754f),
				new Vector3D(0.508273f, 0.508273f, 0.508273f),
				0.4f
		);
	public static Material black_plastic = new Material(
				new Vector3D(0.0f, 0.0f, 0.0f),
				new Vector3D(0.01f, 0.01f, 0.01f),
				new Vector3D(0.50f, 0.50f, 0.50f),
				.25f
		);
	public static Material cyan_plastic = new Material(
				new Vector3D(0.0f, 0.1f, 0.06f),
				new Vector3D(0.0f, 0.50980392f, 0.50980392f),
				new Vector3D(0.50196078f, 0.50196078f, 0.50196078f),
				.25f
		);
	public static Material green_plastic = new Material(
				new Vector3D(0.0f, 0.0f, 0.0f),
				new Vector3D(0.1f, 0.35f, 0.1f),
				new Vector3D(0.45f, 0.55f, 0.45f),
				.25f
		);
	public static Material red_plastic = new Material(
				new Vector3D(0.0f, 0.0f, 0.0f),
				new Vector3D(0.5f, 0.0f, 0.0f),
				new Vector3D(0.7f, 0.6f, 0.6f),
				.25f
		);
	public static Material white_plastic = new Material(
				new Vector3D(0.0f, 0.0f, 0.0f),
				new Vector3D(0.55f, 0.55f, 0.55f),
				new Vector3D(0.70f, 0.70f, 0.70f),
				.25f
		);
	public static Material yellow_plastic = new Material(
				new Vector3D(0.0f, 0.0f, 0.0f),
				new Vector3D(0.5f, 0.5f, 0.0f),
				new Vector3D(0.60f, 0.60f, 0.50f),
				.25f
		);
	public static Material black_rubber = new Material(
				new Vector3D(0.02f, 0.02f, 0.02f),
				new Vector3D(0.01f, 0.01f, 0.01f),
				new Vector3D(0.4f, 0.4f, 0.4f),
				.078125f
		);
	public static Material cyan_rubber = new Material(
				new Vector3D(0.0f, 0.05f, 0.05f),
				new Vector3D(0.4f, 0.5f, 0.5f),
				new Vector3D(0.04f, 0.7f, 0.7f),
				.078125f
		);
	public static Material green_rubber = new Material(
				new Vector3D(0.0f, 0.05f, 0.0f),
				new Vector3D(0.4f, 0.5f, 0.4f),
				new Vector3D(0.04f, 0.7f, 0.04f),
				.078125f
		);
	public static Material red_rubber = new Material(
				new Vector3D(0.05f, 0.0f, 0.0f),
				new Vector3D(0.5f, 0.4f, 0.4f),
				new Vector3D(0.7f, 0.04f, 0.04f),
				.078125f
		);
	public static Material white_rubber = new Material(
				new Vector3D(0.05f, 0.05f, 0.05f),
				new Vector3D(0.5f, 0.5f, 0.5f),
				new Vector3D(0.7f, 0.7f, 0.7f),
				.078125f
		);
	public static Material yellow_rubber = new Material(
				new Vector3D(0.05f, 0.05f, 0.0f),
				new Vector3D(0.5f, 0.5f, 0.4f),
				new Vector3D(0.7f, 0.7f, 0.04f),
				.078125f
		);
	public static Material simple = new Material(
			new Vector3D(0.2f, 0.2f, 0.2f),
			new Vector3D(1, 1, 1),
			new Vector3D(0, 0, 0),
			0
	);


	private float shininess;
	private FloatBuffer ambient;
	private FloatBuffer diffuse;
	private FloatBuffer specular;

	public Material(Vector3D ambientV, Vector3D diffuseV, Vector3D specularV, float shininess){
		this.ambient = BufferUtils.createFloatBuffer(4);
		ambient.put(new float[] { ambientV.getX(), ambientV.getY(), ambientV.getZ(), 1f});
		ambient.flip();

		this.diffuse = BufferUtils.createFloatBuffer(4);
		diffuse.put(new float[] { diffuseV.getX(), diffuseV.getY(), diffuseV.getZ(), 1f});
		diffuse.flip();

		this.specular = BufferUtils.createFloatBuffer(4);
		specular.put(new float[] { specularV.getX(), specularV.getY(), specularV.getZ(), 1f});
		specular.flip();

		this.shininess = shininess;
	}

	public void apply(){
		glMaterial(GL_FRONT, GL_AMBIENT, ambient);

		glMaterial(GL_FRONT, GL_DIFFUSE, diffuse);

		glMaterial(GL_FRONT, GL_SPECULAR, specular);

		assert (shininess <= 1);
		glMaterialf(GL_FRONT, GL_SHININESS, shininess * 128.0f);
	}

}
