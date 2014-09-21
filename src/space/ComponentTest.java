package space;

import java.awt.Canvas;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import space.gui.pipeline.GameRenderer;
import space.gui.pipeline.mock.MockWorld;
import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.math.Vector2D;
import space.network.Client;
import space.network.Server;
import space.world.Player;
import space.world.Room;
import space.world.World;

/**
 * 
 * @author Simon Pinfold, James Greenwood-Thessman
 *
 */
public class ComponentTest extends JFrame {
	
	public static void main(String[] args) throws LWJGLException{
		new ComponentTest().create();
	}
	
	private void create() throws LWJGLException {
		int width = 1800;
		int height = 900;	
		
		this.setSize(width, height);
		this.setVisible(true);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Canvas c = new Canvas();
		c.setIgnoreRepaint(true);
		c.setFocusable(true);
		this.getContentPane().add(c);
		
		
		Player mockPlayer = new Player(new Vector2D(0, 0), 4321);
		World world = new World();
		Room r = new Room(LightMode.BASIC_LIGHT, 1, "temp", Arrays.asList(new Vector2D(-20, 20), new Vector2D(20, 20), new Vector2D(20, -20), new Vector2D(-20, -20)));
		world.addRoom(r);
		
		GameRenderer rcp = new GameRenderer(width, height);
		rcp.setParent(c);
		
		rcp.loadModels(world);
		
		//Start a server so a client can connect to it
		new Server("localhost", 1234);
		Client client = new Client("localhost", 1234, world, mockPlayer);
		
		Mouse.setGrabbed(true);
		Mouse.setClipMouseCoordinatesToWindow(false);
		
		long last = getTime();
		while (true) {
			long now = getTime();
			int delta = (int)(now - last);
			last = now;

			// do world update
			client.update(delta);
			
			// update renderer
			rcp.renderTick(delta, mockPlayer, world);

			Display.update();
			Display.sync(60);

		}
		//Display.destroy();
	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
}
