package space.gui.application;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import space.gui.pipeline.GameRenderer;
import space.gui.pipeline.mock.MockWorld;
import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.math.Vector2D;
import space.network.Client;
import space.world.Player;
import space.world.Room;
import space.world.World;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class Bootstrap {
	
	public final static int WIDTH = 1800;
	public final static int HEIGHT = 900;

	public static void main(String[] args) throws LWJGLException, IOException {
		
		//Load World TODO Load from json
		World world = new World();
		Room r = new Room(LightMode.BASIC_LIGHT, 1, "temp", Arrays.asList(new Vector2D(-20, 20), new Vector2D(20, 20), new Vector2D(20, -20), new Vector2D(-20, -20)));
		world.addRoom(r);
		
		//Create the client TODO use program arguments for host and port
		Client client = new Client("localhost", 1234, world);
	
		Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
		Display.create();
		
		//Load GUI
		HeadsUpDisplay hud = new HeadsUpDisplay();
		LWJGLRenderer renderer = new LWJGLRenderer();
		GUI gui = new GUI(hud, renderer);
		
		URL themeURL = new URL(Bootstrap.class.getResource("resources/gameui.xml").toString());
		
		ThemeManager theme = ThemeManager.createThemeManager(themeURL, renderer);
        gui.applyTheme(theme);
        
        //Load Render Pipeline
		GameRenderer rcp = new GameRenderer(WIDTH, HEIGHT);
		
		rcp.loadModels(world);
		
		
		long last = getTime();
		
		while (!Display.isCloseRequested() && !hud.quit) {
			long now = getTime();
			int delta = (int)(now - last);
			last = now;

			// update the world via the client
			client.update(delta);
			
			// update renderer
			rcp.renderTick(delta, client.getLocalPlayer(), world);

			// update gui
			gui.update();
			
			Display.update();
			Display.sync(60);

		}
		client.shutdown();
		Display.destroy();
	}
	
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
}
