package space.gui.application;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import space.gui.pipeline.GameRenderer;
import space.network.Client;
import space.network.ClientListener;
import space.network.Server;
import space.network.storage.MockStorage;
import space.network.storage.WorldLoader;
import space.serialization.JsonToModel;
import space.serialization.ModelToJson;
import space.world.World;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class GameApplication implements ClientListener{
	protected static final int QUIT = 0;
	
	protected static final int SINGLEPLAYER = 1;
	protected static final int MAINMENU = 1;
	
	protected static final int MULTIPLAYER = 2;
	
	int width;
	int height;
	
	int state;
	boolean end;
	
	long lastTick;
	
	Server server;
	Client client;
	GameRenderer gameRenderer;
	
	LWJGLRenderer renderer;
	GUI gui;
	GUIWrapper guiWrapper;
	
	HeadsUpDisplay headsUpDisplay;
	
	MainMenu mainMenu;
	IngameMenu ingameMenu;
	
	String serverAddress;
	
	public GameApplication(int width, int height) throws LWJGLException, IOException{
		this.width = width;
		this.height = height;
		
		this.end = false;
		
		this.server = null;
		this.client = null;
		
		serverAddress = Client.DEFAULT_HOST;
		
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.create();
		
		renderer = new LWJGLRenderer();
		gui = new GUI(renderer);
		
		mainMenu();
		
		quit();
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void mainMenu() throws IOException, LWJGLException{
		this.end = false;
		this.state = 0;
		
		mainMenu = new MainMenu(this);
		gui.setRootPane(mainMenu);
		
		ThemeManager theme = ThemeManager.createThemeManager(Bootstrap.class.getResource("resources/gameui.xml"), renderer);
        gui.applyTheme(theme);
        

        clearGLBuffer();
        Display.swapBuffers();
        clearGLBuffer();
        
		while (!Display.isCloseRequested() && !end) {
			clearGLBuffer();
			
			mainMenu.update();
			gui.update();
		
			Display.update();
			Display.sync(60);
		}
		
		switch(state){
			case SINGLEPLAYER:
				server = new Server(Client.DEFAULT_HOST, Client.DEFAULT_PORT, new MockStorage(), new ModelToJson(), "temp");
			case MULTIPLAYER:
				startGame();
			default:
				break;
		}
	}
	
	public void startGame() throws LWJGLException, IOException{
		
		//Create the client TODO use program arguments for host and port
		client = new Client(serverAddress, Client.DEFAULT_PORT, new JsonToModel());
		client.addListener(this);
		
		guiWrapper = new GUIWrapper(this);
		
		//Load GUI
		headsUpDisplay = new HeadsUpDisplay(this);
		guiWrapper.add(headsUpDisplay);
		
		ingameMenu = new IngameMenu(this);
		guiWrapper.add(ingameMenu);
		
		gui.setRootPane(guiWrapper);
		gui.reapplyTheme();
        
        //Load Render Pipeline
		gameRenderer = new GameRenderer(width, height);
		
		gameRenderer.loadModels(client.getWorld());
		
		this.end = false;
		this.state = 0;
		this.lastTick = getTime();
		
		//setMenuVisible(false);
		captureMouse(true);
		
		
		while (!Display.isCloseRequested() && !end) {
			gameTick();
		}
		
		closeConnections();
		
		switch(state){
			case MAINMENU:
				mainMenu();
			default:
				break;
		}
		
		
	}
	
	public void gameTick(){
		long now = getTime();
		int delta = (int)(now - lastTick);
		lastTick = now;
		
		// update the world via the client
		client.update(delta);
		
		// update renderer
		gameRenderer.renderTick(delta, client.getLocalPlayer(), client.getWorld());
		
		// update gui
		headsUpDisplay.update(client);
		gui.update();
		
		Display.update();
		Display.sync(60);
	}
	
	public void stop(){
		end = true;
	}
	
	public void setGameState(int i){
		if(i >= 0){
			state = i;
			end = true;
		}
	}
	
	public void quit(){
		Display.destroy();
	}
	
	public void closeConnections(){
		if(client != null){
			client.shutdown();
		}
		if(server != null){
			server.shutdown();
		}
	}

    public void captureMouse(boolean flag) {
    	if(!Display.isActive()){
    		return;
    	}
    	Mouse.setGrabbed(flag);
    	client.setActive(flag);
	}
    
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public void openPopup(Event evt) {
		headsUpDisplay.createRadialMenu().openPopup(evt);
	}

	public void setMenuVisible(boolean flag) {
		captureMouse(!flag);
		ingameMenu.setVisible(flag);
	}

	public boolean isMenuVisible() {
		return ingameMenu.isVisible();
	}
	
	public void clearGLBuffer(){
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);	
	}

	@Override
	public void onConnectionClose(String reason) {
		setGameState(MAINMENU);
	}

	public void setupMultiplayer(String address, int id) {
		serverAddress = address;
	}
	
}
