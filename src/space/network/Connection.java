package space.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection {
	
	private Socket socket;
	private OutputStream outgoing;
	private InputStream incoming;
	
	public Connection(Socket connectedSocket){
		socket = connectedSocket;
		try {
			incoming = connectedSocket.getInputStream();
			outgoing = connectedSocket.getOutputStream();
		} catch (IOException e) {
			//Throw the exception again as it is a critical failure
			throw new RuntimeException(e);
		}
	}
	
	public boolean hasMessage(){
		try {
			return incoming.available() > 0;
		} catch (IOException e) {
			// TODO decide how to deal with exception
			e.printStackTrace();
			return false;
		}
	}
	
	public String readMessage(){
		try {
			int length = incoming.read();
			char[] mess = new char[length];
			for (int i = 0; i < length; ++i){
				mess[i] = (char) incoming.read();
			}
			return String.valueOf(mess);
		} catch (IOException e) {
			// TODO decide how to deal with exception
			e.printStackTrace();
			return null;
		}
	}
	
	public void sendMessage(String message){
		try {
			outgoing.write(message.length());
			for (char c : message.toCharArray()){
				outgoing.write(c);
			}
		} catch (IOException e) {
			// TODO decide how to deal with exception
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
