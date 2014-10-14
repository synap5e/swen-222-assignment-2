package space.gui.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import space.gui.pipeline.models.BulletModel;
import space.gui.pipeline.models.DoorFrameModel;
import space.gui.pipeline.models.DoorSurfaceModel;
import space.gui.pipeline.models.RenderModel;
import space.gui.pipeline.models.WavefrontModel;
import space.gui.pipeline.viewable.OpenableContainer;
import space.gui.pipeline.viewable.ViewableObject;
import space.math.Vector3D;

/** The set of models that are required for the rendering and a mapping from each objects
 * type string to that mode.
 *
 * ModelFlyweight also loads the textures used by the room model.
 *
 * @author Simon Pinfold (300280028)
 *
 */
public class ModelFlyweight {

	private HashMap<String, RenderModel> models;
	private WavefrontModel openChest;
	private WavefrontModel closedChest;
	private DoorFrameModel doorFrame;
	private DoorSurfaceModel doorSurface;
	private int floorTexture;
	private int wallTexture;
	private int ceilingTexture;

	/**
	 * Create the mapping to moodels and load the models into memory. This is an expensive operation and
	 * need only be done once for a GameRenderer instance.
	 */
	public ModelFlyweight() {
		models = new HashMap<>();
		try {
			models.put("Key", new WavefrontModel(		new File("./assets/models/key.obj"), 	// model
														new Vector3D(0, 0, 0),					// model offset
														new Vector3D(0, 0, 90), 				// model rotation
														1.f,									// model scale
														new Vector3D(0.65f,0.3f,0.15f),			// model color
														Material.bronze							// model material properties
												));
			models.put("Player", new WavefrontModel(	new File("./assets/models/character_model.obj"),
														new Vector3D(1.0f,0,-0.160f),
														new Vector3D(0,90,0), 0.23f,
														new Vector3D(0.35f,0.3f,0.15f),
														Material.bronze
													));
			models.put("Table", new WavefrontModel(	new File("./assets/models/table.obj"),
														new Vector3D(0, 0, 0),
														new Vector3D(0, 0, 0), 0.75f,
														new Vector3D(0.35f,0.35f,0.35f),
														Material.bronze
				));

			models.put("Turret", new WavefrontModel(	new File("./assets/models/turret.obj"),
														new Vector3D(0, 0, 0),
														new Vector3D(0, 90, 0), 2,
														new Vector3D(0.65f,0.35f,0.35f),
														Material.bronze
													));

			models.put("Teleporter", new WavefrontModel(new File("./assets/models/teleporter.obj"),
														new Vector3D(0, 0, 0),
														new Vector3D(0, 90, 0), 0.5f,
														new Vector3D(0.35f,0.35f,0.35f),
														Material.bronze
													));
			models.put("Wallet", new WavefrontModel(new File("./assets/models/teapot.obj"),
					new Vector3D(0, 0, 0),
					new Vector3D(0, 90, 0), 0.5f,
					new Vector3D(0.35f,0.35f,0.35f),
					Material.bronze
				));


			models.put("Button", new WavefrontModel(	new File("./assets/models/button.obj"),
														new Vector3D(0, 0, 0),
														new Vector3D(0, 90, 0), 1f,
														new Vector3D(0.35f,0.65f,0.35f),
														Material.simple
													));

			models.put("Bullet", new BulletModel());

			openChest = new WavefrontModel(			new File("./assets/models/chest.obj"),
														new Vector3D(0, 0, 0),
														new Vector3D(0, 90, 0), 2,
														new Vector3D(0.35f,0.35f,0.65f),
														Material.bronze
											   );

			closedChest = new WavefrontModel(		new File("./assets/models/chestclosed.obj"),
														new Vector3D(0, 0, 0),
														new Vector3D(0, 90, 0), 2,
														new Vector3D(0.35f,0.35f,0.65f),
														Material.bronze
												);

			doorFrame = new DoorFrameModel();
			doorSurface = new DoorSurfaceModel();

			wallTexture = TextureLoader.loadTexture(new File("./assets/interior_wall.png"));
			floorTexture = TextureLoader.loadTexture(new File("./assets/floor maybs.png"));
			ceilingTexture = TextureLoader.loadTexture(new File("./assets/shiphull.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the RenderModel associated with the passed in ViewableObject
	 * @param type the object to get the model for
	 * @return the model associated with the object type
	 */
	public RenderModel get(ViewableObject type) {
		// chests are the one exception in that they have 2 models.
		// one for open and for closed.
		if (type.getType() == "Chest" && type instanceof OpenableContainer){
			OpenableContainer cont = (OpenableContainer)type;
			if (cont.isOpen()){
				return openChest;
			} else {
				return closedChest;
			}
		}

		// if there is no model the error that gets thrown up is rather cryptic
		// instead we hard-fail here saying what model is missing
		if (!models.containsKey(type.getType())){
			System.err.println("ModelFlyweight does not have a model for \"" + type.getType() + "\"");
			System.out.println("Loaded models are " + models.keySet());
			System.exit(-1);
		}
		return models.get(type.getType());
	}

	public int getWallTexture() {
		return wallTexture;
	}

	public int getCeilingTexture() {
		return ceilingTexture;
	}

	public int getFloorTexture() {
		return floorTexture;
	}

	public DoorSurfaceModel getDoorSurface() {
		return doorSurface;
	}

	public DoorFrameModel getDoorFrame() {
		return doorFrame;
	}



}
