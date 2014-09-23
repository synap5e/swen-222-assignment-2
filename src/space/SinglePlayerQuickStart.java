package space;

import java.io.IOException;
import org.lwjgl.LWJGLException;
import space.gui.application.Bootstrap;
import space.network.ServerBootstrap;

/**
 * Only exists to be an easy way to start a local server and a client that connects it.
 * 
 * @author James
 */
public class SinglePlayerQuickStart {

	public static void main(String[] args) throws LWJGLException, IOException {
		ServerBootstrap.main(args);
		Bootstrap.main(args);
	}

}
