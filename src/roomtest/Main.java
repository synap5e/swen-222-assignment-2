package roomtest;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.glu.GLU;

public class Main {

	private static final Vec3 FORWARDS = new Vec3(0,0,1);
	private static final Vec3 LEFT = new Vec3(1,0,0);
	private static final Vec3 RIGHT = new Vec3(-1,0,0);
	private static final Vec3 BACK = new Vec3(0,0,-1);
	
	long lastFrame;
	int fps;
	long lastFPS;
	int windowWidth = 800;
	int windowHeight = 600;
	
	private int lasty=-1;
	private int lastx=-1;
	private Camera camera;
	private Vec3 playerPos = new Vec3(0,5,0);
	
	
	float xRotation = 300f;
	float yRotation = 90f;
	private static float DEGREES_TO_RADIANS(float degrees){
		return (float) ((degrees) * (Math.PI / 180.0));
	}
	private Vec3 getLook(){
		float x_circ = (float) (Math.cos(DEGREES_TO_RADIANS(yRotation)) * Math.sin(DEGREES_TO_RADIANS(xRotation)));
		float y_circ = (float) (  										  Math.cos(DEGREES_TO_RADIANS(xRotation)));
		float z_circ = (float) (Math.sin(DEGREES_TO_RADIANS(yRotation)) * Math.sin(DEGREES_TO_RADIANS(xRotation)));
		return new Vec3(x_circ, y_circ, z_circ);
	}
	
	public void start() {
		
	
		try {
			Display.setDisplayMode(new DisplayMode(windowWidth, windowHeight));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		this.camera = new Camera(windowWidth, windowHeight, Room.generateRandomRoom(0, 0));
		camera.init();
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime();
		
		while (!Display.isCloseRequested()) {
			int delta = getDelta();

			update(delta);
			
			//renderGL();
			camera.render(playerPos, getLook());

			Display.update();
			Display.sync(60); // cap fps to 60fps
		}

		Display.destroy();
	}
	
	boolean grap = true;
	public void update(int delta) {	
		
		int x = Mouse.getX();
		int y = Mouse.getY();
		
		
		/*if (Keyboard.isKeyDown(Keyboard.KEY_F11)){
			grap = !grap;
		}*/
		Mouse.setGrabbed(true);
		Mouse.setClipMouseCoordinatesToWindow(false);
		
		if (lastx != -1){
			yRotation += (x-lastx)/8.0;
			xRotation += (y-lasty)/8.0;
			
			if (xRotation >= 360) xRotation = 359.9f;
			if (xRotation <= 180) xRotation = 180.1f;
		}
		
		Vec3 move = getLook().mul(delta/100f);
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			playerPos.addLocal(move);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			playerPos.subLocal(Vec3.cross(move, new Vec3(0,1,0)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			playerPos.addLocal(Vec3.cross(move, new Vec3(0,1,0)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			playerPos.subLocal(move);
		}
		
		
		lastx = Mouse.getX();
		lasty = Mouse.getY();
		
		updateFPS(); // update FPS Counter
	}
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		      
		return delta;
	}
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	public void initGL() {
		glClearColor(0, 0, 0, 0);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
	//	glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_FLAT);
	}
	public static void main(String [] args)
	{
		Main m = new Main();
		m.start();
	}
}
