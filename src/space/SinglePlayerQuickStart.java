package space;

import java.io.IOException;

import org.lwjgl.LWJGLException;

import space.gui.application.Bootstrap;
import space.network.Server;
import space.network.storage.MockStorage;

/**
 * Only exists to be an easy way to start a local server and a client that connects it.
 * 
 * @author James
 */
public class SinglePlayerQuickStart {

	public static void main(String[] args) throws LWJGLException, IOException {
		Server server = new Server("localhost", 1234, new MockStorage(), "savepath");
		Bootstrap.main(args);
		
		//Once this is reached the client is done so close the server
		server.shutdown();
	}

}
