package space.gui.application;

import java.io.IOException;

import org.lwjgl.LWJGLException;

public class Bootstrap {
	
	public final static int WIDTH = 1800;
	public final static int HEIGHT = 900;

	public static void main(String[] args) throws LWJGLException, IOException {
		new GameApplication(WIDTH, HEIGHT);
	}
}
