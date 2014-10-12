package space.gui.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import space.gui.pipeline.mock.Bullet;
import space.gui.pipeline.models.BulletModel;
import space.gui.pipeline.models.RenderModel;
import space.gui.pipeline.models.WavefrontModel;
import space.gui.pipeline.viewable.OpenableContainer;
import space.gui.pipeline.viewable.ViewableObject;
import space.math.Vector3D;
import space.world.Key;
import space.world.Player;

public class ModelFlyweight {

	private HashMap<String, RenderModel> models;
	private WavefrontModel openContainer;
	private WavefrontModel closedContainer;

	public ModelFlyweight() {
		models = new HashMap<>();
		try {
			//models.put(Robot.class, new WavefrontModel(new File("./assets/models/character_model.obj"), new Vector3D(-0.5f,0,0.160f), new Vector3D(0,270,0), 0.23f, Material.bronze));

			models.put("Key", new WavefrontModel(		new File("./assets/models/key.obj"), 	// model
														new Vector3D(0, 0, 0),						// model offset
														new Vector3D(0, 0, 90), 								// model rotation
														1.f,												// model scale
														new Vector3D(0.65f,0.3f,0.15f),						// model color
														Material.bronze										// model material properties
												));
			models.put("Player", new WavefrontModel(	new File("./assets/models/character_model.obj"),
														new Vector3D(1.0f,0,-0.160f),
														new Vector3D(0,90,0), 0.23f,
														new Vector3D(0.35f,0.3f,0.15f),
														Material.bronze
													));
			models.put("Bullet", new BulletModel());

			openContainer = new WavefrontModel(			new File("./assets/models/teapot.obj"),
														new Vector3D(-0.5f,0,0.160f),
														new Vector3D(0,270,0), 0.23f,
														new Vector3D(0.15f,0.6f,0.15f),
														Material.bronze
											   );

			closedContainer = new WavefrontModel(		new File("./assets/models/teapot.obj"),
														new Vector3D(-0.5f,0,0.160f),
														new Vector3D(0,270,0), 0.23f,
														new Vector3D(0.65f,0.3f,0.75f),
														Material.bronze
												);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public RenderModel get(ViewableObject type) {
		if (type instanceof OpenableContainer){
			OpenableContainer cont = (OpenableContainer)type;
			if (cont.isOpen()){
				return openContainer;
			} else {
				return closedContainer;
			}
		}
		return models.get(type.getName());
	}

}
