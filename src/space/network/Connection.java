package space.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import space.network.message.Message;
import space.network.message.TextMessage;

public class Connection {
	
	private static final int TEXT = 0;
	private static final int UNKNOWN = -1;
	
	
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
	
	public Message readMessage(){
		try {
			int type = incoming.read();
			int length = incoming.read();
			int[] data = new int[length];
			for (int i = 0; i < length; ++i){
				data[i] = incoming.read();
			}
			switch (type){
				case TEXT:
					return new TextMessage(data);
				default:
					//TODO: decide how to deal with format error
					return null;
			}
		} catch (IOException e) {
			// TODO decide how to deal with exception
			e.printStackTrace();
			return null;
		}
	}
	
	public void sendMessage(Message message){
		try {
			int type = typeOf(message);
			int[] data = message.toIntArray();
			
			outgoing.write(type);
			outgoing.write(data.length);
			for (int value : data){
				outgoing.write(value);
			}
		} catch (IOException e) {
			// TODO decide how to deal with exception
			e.printStackTrace();
		}
	}
	
	private int typeOf(Message message){
		//TODO: decide how whether this is the best way to determine the type of a message
		if (message instanceof TextMessage) return TEXT;
		else return UNKNOWN;
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
