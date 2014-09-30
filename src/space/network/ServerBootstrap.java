package space.network;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * This is a temp class
 * 
 * @author James
 */
public class ServerBootstrap {
	
	private JFrame serverWindow;
	
	private Server server;
	
	public static void main(String[] args){
		//TODO read host and port from arguments
		new ServerBootstrap("localhost", 1234);
	}
	
	private ServerBootstrap(String host, int port){
		//Prepare the window
		serverWindow = new JFrame("Space Server");
		serverWindow.setSize(200, 100);
		serverWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		serverWindow.addWindowListener(new CloseHook());
		
		//Create the sever
		server = new Server("localhost", 1234);
		
		//Display the window
		serverWindow.setVisible(true);
	}
	
	private class CloseHook implements WindowListener {
		
		@Override
		public void windowClosed(WindowEvent e) {
			server.shutdown();
		}
		
		@Override
		public void windowOpened(WindowEvent e) {}
		
		@Override
		public void windowIconified(WindowEvent e) {}
		
		@Override
		public void windowDeiconified(WindowEvent e) {}
		
		@Override
		public void windowDeactivated(WindowEvent e) {}
		
		@Override
		public void windowClosing(WindowEvent e) {}
		
		@Override
		public void windowActivated(WindowEvent e) {}
	}
}
