package space.gui.application;

import java.io.IOException;
import java.util.Arrays;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import space.gui.pipeline.GameRenderer;
import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.math.Vector2D;
import space.network.Client;
import space.network.Server;
import space.world.Room;
import space.world.World;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class GameApplication {
	protected static final int QUIT = 0;
	protected static final int SINGLEPLAYER = 1;
	protected static final int MULTIPLAYER = 2;
	
	int width;
	int height;
	
	int state;
	boolean end;
	
	long lastTick;
	
	Server server;
	Client client;
	World world;
	GameRenderer gameRenderer;
	
	LWJGLRenderer renderer;
	GUI gui;
	GUIWrapper guiWrapper;
	
	IngameController ingameController;
	
	HeadsUpDisplay hud;
	
	MainMenu mainMenu;
	
	public GameApplication(int width, int height) throws LWJGLException, IOException{
		this.width = width;
		this.height = height;
		
		this.end = false;
		
		this.server = null;
		this.client = null;
		
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.create();
		
		renderer = new LWJGLRenderer();
		gui = new GUI(renderer);
		
		mainMenu = new MainMenu(this);
		gui.setRootPane(mainMenu);
		
		ThemeManager theme = ThemeManager.createThemeManager(Bootstrap.class.getResource("resources/gameui.xml"), renderer);
        gui.applyTheme(theme);
        
		while (!Display.isCloseRequested() && !end) {
			
			gui.update();
			
			Display.update();
			Display.sync(60);
		}
		
		switch(state){
			case 1:
				server = new Server("localhost", 1234);
			case 2:
				startGame();
			default:
				break;
		}
		
		quit();
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void startGame() throws LWJGLException, IOException{
		//Load World TODO Load from json
		world = new World();
		Room r = new Room(LightMode.BASIC_LIGHT, 1, "temp", Arrays.asList(new Vector2D(-20, 20), new Vector2D(20, 20), new Vector2D(20, -20), new Vector2D(-20, -20)));
		world.addRoom(r);
		
		//Create the client TODO use program arguments for host and port
		client = new Client("localhost", 1234, world);
		
		guiWrapper = new GUIWrapper(this);
		
		//Load GUI
		guiWrapper.add(new HeadsUpDisplay(this));
		guiWrapper.add(new IngameController(this));
		
		gui.setRootPane(guiWrapper);
		gui.reapplyTheme();
        
        //Load Render Pipeline
		gameRenderer = new GameRenderer(width, height);
		
		gameRenderer.loadModels(world);
		
		this.end = false;
		this.lastTick = getTime();
		
		captureMouse(true);
		
		
		while (!Display.isCloseRequested() && !end) {
			gameTick();
		}
	}
	
	public void gameTick(){
		long now = getTime();
		int delta = (int)(now - lastTick);
		lastTick = now;
		
		// update the world via the client
		client.update(delta);
		
		// update renderer
		gameRenderer.renderTick(delta, client.getLocalPlayer(), world);
		
		// update inputs
		gui.update();
		
		
		Display.update();
		Display.sync(60);
	}
	
	public void stopGame(){
		end = true;
	}
	
	public void setState(int i){
		if(i >= 0){
			state = i;
			end = true;
		}
	}
	
	public void quit(){
		Display.destroy();
		
		if(client != null){
			client.shutdown();
		}
		if(server != null){
			server.shutdown();
		}
	}

    public void captureMouse(boolean flag) {
    	Mouse.setGrabbed(flag);
    	client.setActive(flag);
	}
    
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public void openPopup(Event evt) {
		hud.createRadialMenu().openPopup(evt);
	}
	
}
