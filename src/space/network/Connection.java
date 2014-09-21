package space.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import space.network.message.EntityMovedMessage;
import space.network.message.Message;
import space.network.message.PlayerJoinedMessage;
import space.network.message.TextMessage;

public class Connection {
	
	private static final int TEXT = 0;
	private static final int PLAYER_JOINED = 1;
	private static final int ENTITY_MOVED = 2;
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
			byte[] data = new byte[length];
			incoming.read(data);
			switch (type){
				case TEXT:
					return new TextMessage(data);
				case PLAYER_JOINED:
					return new PlayerJoinedMessage(data);
				case ENTITY_MOVED:
					return new EntityMovedMessage(data);
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
			byte[] data = message.toByteArray();
			
			outgoing.write(type);
			outgoing.write(data.length);
			outgoing.write(data);
		} catch (IOException e) {
			// TODO decide how to deal with exception
			e.printStackTrace();
		}
	}
	
	private int typeOf(Message message){
		//TODO: decide how whether this is the best way to determine the type of a message
		if (message instanceof TextMessage) return TEXT;
		else if (message instanceof PlayerJoinedMessage) return PLAYER_JOINED;
		else if (message instanceof EntityMovedMessage) return ENTITY_MOVED;
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
