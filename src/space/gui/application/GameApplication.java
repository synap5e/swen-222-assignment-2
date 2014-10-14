package space.gui.application;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import space.gui.pipeline.GameRenderer;
import space.network.Client;
import space.network.ClientListener;
import space.network.Server;
import space.serialization.JsonToModel;
import space.serialization.ModelToJson;

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
	
	private long lastTick;

	private Server server;
	private Client client;
	private GameRenderer gameRenderer;
	private GameDisplay gameDisplay;

	private String serverAddress;
	private String saveName;
	
	private int playerId;
	
	private int state;

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
		this.server = null;
		this.client = null;
		this.serverAddress = Client.DEFAULT_HOST;

		// Initialise LWJGWL display
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.create();
		
		gameDisplay = new GameDisplay(width, height);
		
		gameRenderer = new GameRenderer(width, height);

		// Go to main menu
		mainMenu();

		quit();
	}

	/**
	 * Displays the main menu.
	 *
	 * @throws IOException
	 * @throws LWJGLException
	 */
	private void mainMenu() throws IOException, LWJGLException{
		reset();
		
		gameDisplay.displayMenu(this);

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
		reset();
		
		//Create the client TODO use program arguments for host and port
		client = new Client(serverAddress, Client.DEFAULT_PORT, new JsonToModel(), gameDisplay.getKeyBinding(), playerId);
		client.addListener(this);
		client.addDisplayListener(gameDisplay);

		// Setup GUI elements
		gameDisplay.displayGame(this);
		
        //Load Render Pipeline
		gameRenderer.loadModels(client.getWorld());

		// Reset state
		this.lastTick = getTime();

		gameDisplay.captureMouse(true);

		//game loop
		while (!gameDisplay.isEnd()) {
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
		gameDisplay.update(client);

		Display.update();
		Display.sync(60);
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
	 * Gets the current game time in milliseconds
	 *
	 * @return current game time
	 */
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
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
	 * Sets up the single player by selecting the desired save file.
	 * 
	 * @param text file name
	 */
	public void setupSingleplayer(String text) {
		this.saveName = text;
	}
	
	/**
	 * Resets the state back to its original value.
	 */
	public void reset(){
		this.state = 0;
		gameDisplay.reset();
	}
	

	/**
	 * This method will cause the game to exit a game loop with a set state to define further actions.
	 *
	 * @param state
	 */
	public void setGameState(int state){
		if(state >= 0){
			this.state = state;
			gameDisplay.stop();
		}
	}
}
