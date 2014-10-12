package space.gui.application;

import org.lwjgl.opengl.Display;


public class Bootstrap {

	public final static int WIDTH = 1800;
	public final static int HEIGHT = 900;

	public static void main(String[] args){
		int width = Math.min(Display.getDesktopDisplayMode().getWidth(), WIDTH);
		int height = Math.min(Display.getDesktopDisplayMode().getHeight() - 50, HEIGHT);

		try{
			new GameApplication(width, height);
		} catch(Exception e){
			Display.destroy();
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
