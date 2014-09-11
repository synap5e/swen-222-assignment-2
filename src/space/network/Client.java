package space.network;

import java.io.IOException;
import java.net.Socket;

public class Client {

	
	public Client(String host, int port){
		try {
			Connection connection = new Connection(new Socket(host, port));
			System.out.println(connection.readMessage());
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Client("localhost", 1234);
	}
}
