package space.gui.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import space.gui.pipeline.mock.Bullet;
import space.gui.pipeline.mock.Robot;
import space.gui.pipeline.models.BulletModel;
import space.gui.pipeline.models.RenderModel;
import space.gui.pipeline.models.WavefrontModel;
import space.gui.pipeline.viewable.ViewableObject;
import space.math.Vector3D;
import space.world.Key;
import space.world.Player;

public class ModelFlyweight {

	HashMap<Class<? extends ViewableObject>, RenderModel> models;
	
	public ModelFlyweight() {
		models = new HashMap<>();
		try {
			models.put(Robot.class, new WavefrontModel(new File("./assets/models/character_model.obj"), new Vector3D(-0.5f,0,0.160f), new Vector3D(0,270,0), 0.23f, Material.bronze));
			models.put(Key.class, new WavefrontModel(new File("./assets/models/character_model.obj"), new Vector3D(-0.5f,0,0.160f), new Vector3D(0,270,0), 0.23f, Material.bronze));
			models.put(Bullet.class, new BulletModel());
			models.put(Player.class, new WavefrontModel(new File("./assets/models/character_model.obj"), new Vector3D(-0.5f,0,0.160f), new Vector3D(0,270,0), 0.23f, Material.bronze));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public RenderModel get(Class<? extends ViewableObject> type) {
		return models.get(type);
	}

}
