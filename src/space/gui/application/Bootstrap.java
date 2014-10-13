package space.gui.application;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lwjgl.opengl.Display;


public class Bootstrap {

	public final static int WIDTH = 1800;
	public final static int HEIGHT = 900;

	public static void main(String[] args){

		int width = Math.min(Display.getDesktopDisplayMode().getWidth(), WIDTH);
		int height = Math.min(Display.getDesktopDisplayMode().getHeight() - 50, HEIGHT);
		
		Options options = new Options();
		options.addOption("width", true, "set display width");
		options.addOption("height", true, "set display height");
		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd;
		
		try {
			cmd = parser.parse( options, args);
			
			String argWidth = cmd.getOptionValue("width");
			String argHeight = cmd.getOptionValue("height");

			if(argWidth != null) {
				try{
					width = Integer.valueOf(argWidth);
				} catch(NumberFormatException e){
					System.out.println("Invalid width parameter.");
				}
			}
			if(argHeight != null) {
				try{
					height = Integer.valueOf(argHeight);
				} catch(NumberFormatException e){
					System.out.println("Invalid height parameter.");
				}
			}
		} catch (ParseException e) {
			System.out.println("Arguments parsing error, using default.");
		}
		
		try{
			new GameApplication(width, height);
		} catch(Exception e){
			Display.destroy();
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
