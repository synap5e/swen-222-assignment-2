package space;

import java.awt.Canvas;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import space.gui.pipeline.GameRenderer;
import space.gui.pipeline.mock.MockPlayer;
import space.gui.pipeline.mock.MockWorld;
import space.gui.pipeline.viewable.ViewableWord;

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
		
		
		MockPlayer mockPlayer = new MockPlayer();
		MockWorld mockWorld = new MockWorld();

		GameRenderer rcp = new GameRenderer(width, height);
		rcp.setParent(c);
		
		rcp.loadModels(mockWorld);
		
		
		long last = getTime();
		while (true) {
			long now = getTime();
			int delta = (int)(now - last);
			last = now;

			// do world update
			mockPlayer.update(delta);
			mockWorld.update(delta);
			
			// update renderer
			rcp.renderTick(delta, mockPlayer, mockWorld);

			Display.update();
			Display.sync(60);

		}
		//Display.destroy();
	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
}
