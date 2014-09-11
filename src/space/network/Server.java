package space.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private ServerSocket socket;
	private List<Connection> connections;
	
	public Server(String host, int port){
		connections = new ArrayList<Connection>();
		try {
			socket = new ServerSocket(port, 50, InetAddress.getByName(host));
			
			Socket socketConnection = socket.accept();
			connections.add(new Connection(socketConnection));
			
		} catch (IOException e) {
			//Throw the exception again as it is a critical failure
			throw new RuntimeException(e);
		}
		for (Connection c : connections){
			c.sendMessage("Test");
			c.close();
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Server("localhost", 1234);
	}

}
