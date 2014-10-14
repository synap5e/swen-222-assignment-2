package space.gui.application;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import space.gui.application.widget.HeadsUpDisplay;
import space.gui.application.widget.IngameMenu;
import space.gui.application.widget.InventoryExchangeWidget;
import space.gui.application.widget.InventoryWidget;
import space.gui.application.widget.label.KeyEntry;
import space.gui.application.widget.wrapper.GUIWrapper;
import space.gui.application.widget.wrapper.GameController;
import space.gui.application.widget.wrapper.MainMenu;
import space.gui.pipeline.GameRenderer;
import space.network.Client;
import space.network.ClientListener;
import space.network.Server;
import space.serialization.JsonToModel;
import space.serialization.ModelToJson;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

/**
 * The GameApplication class is the main runtime of the game,
 * holding the various game loops for the different sections of the game's flow.
 *
 * @author Matt Graham 300211545
 */

public class GameApplication implements ClientListener{
	public static final int QUIT = 0;

	public static final int SINGLEPLAYER = 1;
	public static final int MAINMENU = 1;

	public static final int MULTIPLAYER = 2;

	private int width;
	private int height;

	private int state;
	private boolean end;

	private long lastTick;

	private Server server;
	private Client client;
	private GameRenderer gameRenderer;

	private LWJGLRenderer renderer;
	private GUI gui;

	private GameController gameController;
	private InventoryExchangeWidget inventoryExchangeWidget;
	private InventoryWidget inventoryWidget;

	private HeadsUpDisplay headsUpDisplay;

	private MainMenu mainMenu;
	private IngameMenu ingameMenu;

	private String serverAddress;
	private String saveName;

	private KeyBinding keyBinding;
	
	private int playerId;

	/**
	 * Creates a new Game Application window of the given width and height.
	 * This class allows exceptions from the various game components to bubble through.
	 *
	 * @param width
	 * @param height
	 * @throws LWJGLException
	 * @throws IOException
	 */
	public GameApplication(int width, int height) throws LWJGLException, IOException{
		this.width = width;
		this.height = height;

		this.end = false;
		this.server = null;
		this.client = null;
		this.serverAddress = Client.DEFAULT_HOST;

		// Initialise LWJGWL display
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.create();

		// Setup TWL Renderer
		renderer = new LWJGLRenderer();
		gui = new GUI(renderer);

		// Setup Key Bindings
		keyBinding = new KeyBinding();

		// Go to main menu
		mainMenu();

		quit();
	}

	/**
	 * Get the width of the game application
	 *
	 * @return width
	 */
	public int getWidth(){
		return width;
	}

	/**
	 * Get the height of the game application
	 *
	 * @return height
	 */
	public int getHeight(){
		return height;
	}

	/**
	 * Displays the main menu.
	 *
	 * @throws IOException
	 * @throws LWJGLException
	 */
	private void mainMenu() throws IOException, LWJGLException{

		// Reset state
		this.end = false;
		this.state = 0;

		mainMenu = new MainMenu(this);
		gui.setRootPane(mainMenu);

		ThemeManager theme = ThemeManager.createThemeManager(Bootstrap.class.getResource("resources/gameui.xml"), renderer);
        gui.applyTheme(theme);

        // Clear screen twice as OpenGL is double buffered.
        clearGLBuffer();
        Display.swapBuffers();
        clearGLBuffer();

        // Game loop
		while (!Display.isCloseRequested() && !end) {
			clearGLBuffer();

			mainMenu.update();
			gui.update();

			Display.update();
			Display.sync(60);
		}

		// Requested action
		switch(state){
			case SINGLEPLAYER:
				server = new Server(Client.DEFAULT_HOST, Client.DEFAULT_PORT, new JsonToModel(), new ModelToJson(), saveName);
			case MULTIPLAYER:
				startGame();
			default:
				break;
		}
	}

	/**
	 * Loads and starts the game.
	 *
	 * @throws IOException
	 * @throws LWJGLException
	 */
	private void startGame() throws LWJGLException, IOException{

		//Create the client TODO use program arguments for host and port
		client = new Client(serverAddress, Client.DEFAULT_PORT, new JsonToModel(), keyBinding, playerId);
		client.addListener(this);

		gameController = new GameController(this);

		//Load GUI
		ingameMenu = new IngameMenu(this);
		gameController.insertChild(ingameMenu, 0);

		headsUpDisplay = new HeadsUpDisplay(this);
		gameController.insertChild(headsUpDisplay, 0);

		inventoryExchangeWidget = new InventoryExchangeWidget(this);
		gameController.add(inventoryExchangeWidget);
		
		inventoryWidget  = new InventoryWidget(this);
		gameController.add(inventoryWidget);

		gui.setRootPane(gameController);
		gui.reapplyTheme();

        //Load Render Pipeline
		gameRenderer = new GameRenderer(width, height);

		gameRenderer.loadModels(client.getWorld());

		// Reset state
		this.end = false;
		this.state = 0;
		this.lastTick = getTime();

		//setMenuVisible(false);
		captureMouse(true);

		//game loop
		while (!Display.isCloseRequested() && !end) {
			gameTick();
		}

		//game has ended
		closeConnections();

		switch(state){
			case MAINMENU:
				mainMenu();
			default:
				break;
		}


	}

	/**
	 * This method contains the game logic which is executed at each game tick.
	 */
	private void gameTick(){
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

	/**
	 * This method will cause the current game loop to end.
	 */
	public void stop(){
		end = true;
	}

	/**
	 * This method will cause the game to exit a game loop with a set state to define further actions.
	 *
	 * @param state
	 */
	public void setGameState(int state){
		if(state >= 0){
			this.state = state;
			stop();
		}
	}

	/**
	 * Cleans up the display, usually on quit.
	 */
	public void quit(){
		Display.destroy();
	}


	/**
	 * Closes all active connections for this application (client and/or server)
	 */
	public void closeConnections(){
		if(client != null){
			client.shutdown();
		}
		if(server != null){
			server.shutdown();
		}
	}

    /**
     * Sets whether the mouse should be captured to the player view
     *
     * @param flag
     */
    public void captureMouse(boolean flag) {
    	if(!Display.isActive()){
    		return;
    	}
    	Mouse.setGrabbed(flag);
    	client.setActive(flag);
	}

	/**
	 * Gets the current game time in milliseconds
	 *
	 * @return current game time
	 */
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Sets the in-game menu to be shown or hidden.
	 * Showing the in-game menu will release the mouse,
	 * hiding the menu will recapture the mouse.
	 *
	 * @param flag
	 */
	public void setMenuVisible(boolean flag) {
		captureMouse(!flag);
		ingameMenu.setVisible(flag);
	}

	/**
	 * Gets whether the in-game menu is open or not.
	 *
	 * @return in-game menu visibility
	 */
	public boolean isMenuVisible() {
		return ingameMenu.isVisible();
	}

	/**
	 * CLears the OpenGL screen buffer.
	 */
	private void clearGLBuffer(){
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void onConnectionClose(String reason) {
		setGameState(MAINMENU);
	}

	/**
	 * Sets up the multi-player environment for connecting to a game.
	 *
	 * @param address the address of the server to connect
	 * @param id your given player to load in the remote game
	 */
	public void setupMultiplayer(String address, String idString) {
		serverAddress = address;
		
		int id = Client.DEFAULT_ID;
		try{
			id = Integer.valueOf(idString);
		} catch(NumberFormatException e){ }
		
		playerId = id;
	}
	
	/**
	 * Sets up the multi-player environment for connecting to a game.
	 *
	 * @param id your given player to load in the remote game
	 */
	public void setupMultiplayer(String idString) {
		setupMultiplayer(Client.DEFAULT_HOST, idString);
	}


	/**
	 * Gets the game client
	 * 
	 * @return the game client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Gets the key bindings
	 * 
	 * @return the key bindings model
	 */
	public KeyBinding getKeyBinding(){
		return keyBinding;
	}

	/**
	 * Indicates that the next key press will be intercepted separately from the game.
	 * 
	 *  @param keyEntry
	 */
	public void captureKey(KeyEntry keyEntry) {
		Widget root = gui.getRootPane();

		if(root instanceof GUIWrapper){
			((GUIWrapper) root).captureKey(keyEntry);
		}
	}

	/**
	 * Sets up the single player by selecting the desired save file
	 * 
	 * @param text file name
	 */
	public void setupSingleplayer(String text) {
		this.saveName = text;
	}

	/**
	 * Sets the inventory exchange visibility and handles the various input states.
	 * 
	 * @param flag
	 */
	public void setInventoryExchangeVisible(boolean flag){
		if(flag && inventoryExchangeWidget.update(getClient().getViewedEntity())){
			captureMouse(false);
			inventoryExchangeWidget.setVisible(true);
			
		} else {
			captureMouse(true);
			inventoryExchangeWidget.setVisible(false);
		}
	}

	/**
	 * Gets the visibility of the inventory exchange interface
	 * 
	 * @return whether the inventory exchange is visible
	 */
	public boolean isInventoryExchangeVisible(){
		return inventoryExchangeWidget.isVisible();
	}
	
	/**
	 * Sets the inventory visibility and handles the various input states.
	 * 
	 * @param flag
	 */
	public void setInventoryVisible(boolean flag){
		if(flag){
			inventoryWidget.update();
		}
		captureMouse(!flag);
		inventoryWidget.setVisible(flag);
	}

	/**
	 * Gets the visibility of the inventory interface
	 * 
	 * @return whether the inventory is visible
	 */
	public boolean isInventoryVisible(){
		return inventoryWidget.isVisible();
	}

}
