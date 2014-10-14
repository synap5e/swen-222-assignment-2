package space.gui.application;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.io.IOException;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import space.gui.application.widget.HeadsUpDisplay;
import space.gui.application.widget.IngameMenu;
import space.gui.application.widget.InventoryExchangeWidget;
import space.gui.application.widget.InventoryWidget;
import space.gui.application.widget.label.KeyEntry;
import space.gui.application.widget.wrapper.GUIWrapper;
import space.gui.application.widget.wrapper.GameController;
import space.gui.application.widget.wrapper.MainMenu;
import space.network.Client;
import space.network.DisplayListener;
import space.world.Container;
import space.world.Pickup;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

/**
 * Display class which controls the viewport of the game.
 * 
 * @author Matt Graham 300211545
 *
 */
public class GameDisplay implements DisplayListener {

	private int width;
	private int height;

	private boolean end;

	private LWJGLRenderer renderer;
	private GUI gui;

	private GameController gameController;
	private InventoryExchangeWidget inventoryExchangeWidget;
	private InventoryWidget inventoryWidget;

	private HeadsUpDisplay headsUpDisplay;

	private MainMenu mainMenu;
	private IngameMenu ingameMenu;
	
	private KeyBinding keyBinding;
	
	public GameDisplay(int width, int height) throws LWJGLException{
		this.width = width;
		this.height = height;

		// Setup TWL Renderer
		renderer = new LWJGLRenderer();
		gui = new GUI(renderer);
		
		// Setup Key Bindings
		keyBinding = new KeyBinding();
		
		reset();
	}
	
	/**
	 * Updates the various display elements,
	 * usually during a game loop.
	 * 
	 * @param client
	 */
	public void update(Client client){
		headsUpDisplay.update(client);
		gui.update();
	}
	

	/**
	 * This method will cause the current game loop to end.
	 */
	public void stop(){
		end = true;
	}
	
	/**
	 * This method will allow a new game loop to occur.
	 */
	public void reset() {
		end = false;
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
	 * Determines if user input has caused the current loop to end.
	 * 
	 * @return whether a game loop should finish
	 */
	public boolean isEnd() {
		return Display.isCloseRequested() || end;
	}
	
	/**
	 * DIsplay the main menu onto the screen.
	 * 
	 * @param gameApplication
	 * @throws LWJGLException
	 * @throws IOException
	 */
	protected void displayMenu(GameApplication gameApplication) throws LWJGLException, IOException{
		mainMenu = new MainMenu(gameApplication, this);
		gui.setRootPane(mainMenu);

		ThemeManager theme = ThemeManager.createThemeManager(Bootstrap.class.getResource("resources/gameui.xml"), renderer);
        gui.applyTheme(theme);

        // Clear screen twice as OpenGL is double buffered.
        clearGLBuffer();
        Display.swapBuffers();
        clearGLBuffer();

        // Game loop
		while (!isEnd()) {
			clearGLBuffer();

			mainMenu.update();
			gui.update();

			Display.update();
			Display.sync(60);
		}
	}
	
	/**
	 * CLears the OpenGL screen buffer.
	 */
	private void clearGLBuffer(){
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);
	}
	
	/**
	 * Displays the game onto the screen.
	 * 
	 * @param gameApplication
	 */
	protected void displayGame(GameApplication gameApplication){
		gameController = new GameController(gameApplication, this);

		//Load GUI
		ingameMenu = new IngameMenu(gameApplication, this);
		gameController.insertChild(ingameMenu, 0);

		headsUpDisplay = new HeadsUpDisplay(this);
		gameController.insertChild(headsUpDisplay, 0);

		inventoryExchangeWidget = new InventoryExchangeWidget(gameApplication, this);
		gameController.add(inventoryExchangeWidget);
		
		inventoryWidget  = new InventoryWidget(gameApplication, this);
		gameController.add(inventoryWidget);

		gui.setRootPane(gameController);
		gui.reapplyTheme();
	}
	

    /**
     * Sets whether the mouse should be captured to the player view
     *
     * @param flag
     */
	protected void captureMouse(boolean flag) {
    	if(!Display.isActive()){
    		return;
    	}
    	Mouse.setGrabbed(flag);
    	keyBinding.setActive(flag);
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
	 * Sets the inventory exchange visibility and handles the various input states.
	 * 
	 * @param flag
	 */
	public void setInventoryExchangeVisible(boolean flag){
		if(flag){
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
	public void showInventory(List<Pickup> items){
		inventoryWidget.update(items);
		setInventoryVisible(true);
	}
	
	/**
	 * Sets the inventory visibility and handles the various input states.
	 * 
	 * @param flag
	 */
	public void setInventoryVisible(boolean flag){
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
	 * Gets the key bindings
	 * 
	 * @return the key bindings model
	 */
	public KeyBinding getKeyBinding(){
		return keyBinding;
	}

	@Override
	public void onInventoryExchange(Container container, List<Pickup> pickups) {
		inventoryExchangeWidget.update(container, pickups);
		setInventoryExchangeVisible(true);
	}

}
