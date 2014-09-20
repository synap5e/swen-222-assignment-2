package space.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	private Thread connectionHandler;
	private boolean stillAlive;
	private ServerSocket socket;
	private List<Connection> connections;
	
	public Server(String host, int port){
		//Create the list of client connections
		connections = new ArrayList<Connection>();
		
		//Create the socket for clients to connect to
		try {
			socket = new ServerSocket(port, 50, InetAddress.getByName(host));
		} catch (IOException e) {
			//Throw the exception again as it is a critical failure
			throw new RuntimeException(e);
		}
		
		//Create Connection Handler
		stillAlive = true;
		connectionHandler = new Thread(new ConnectionHandler());
		connectionHandler.start();
		
		//TODO: Make the server actually do stuff
	}
	
	public void shutdown(){
		//Stop the connection handler
		stillAlive = false;
		connectionHandler.interrupt();
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Connection c : connections){
			//TODO: use a better way to tell the client the server is shutting down
			c.sendMessage("Server Shutting Down");
			c.close();
		}
	}
	
	private class ConnectionHandler implements Runnable {

		@Override
		public void run() {
			Socket socketConnection;
			while(stillAlive){
				try {
					socketConnection = socket.accept();
					synchronized (connections) {
						connections.add(new Connection(socketConnection));
					}
				} catch (IOException e) {
					System.err.println("Connection Attempt Failed");
				}
			}
		}
	}

}
