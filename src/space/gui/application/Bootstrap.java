package space.gui.application;

import java.io.IOException;
import java.net.URL;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import space.gui.pipeline.GameRenderer;
import space.gui.pipeline.mock.MockPlayer;
import space.gui.pipeline.mock.MockWorld;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class Bootstrap {
	
	public final static int WIDTH = 1800;
	public final static int HEIGHT = 900;

	public static void main(String[] args) throws LWJGLException, IOException {
		
		//Load World
		MockWorld mockWorld = new MockWorld();

		//Load Player
		MockPlayer mockPlayer = new MockPlayer();
	
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
		
		rcp.loadModels(mockWorld);
		
		
		long last = getTime();
		
		while (!Display.isCloseRequested() && !hud.quit) {
			long now = getTime();
			int delta = (int)(now - last);
			last = now;

			// do world update
			mockPlayer.update(delta);
			mockWorld.update(delta);
			
			// update renderer
			rcp.renderTick(delta, mockPlayer, mockWorld);

			// update gui
			gui.update();
			
			Display.update();
			Display.sync(60);

		}
		
		Display.destroy();
	}
	
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
}
