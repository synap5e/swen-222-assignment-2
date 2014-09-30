package space.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import space.network.message.DisconnectMessage;
import space.network.message.EntityMovedMessage;
import space.network.message.JumpMessage;
import space.network.message.Message;
import space.network.message.PlayerJoiningMessage;
import space.network.message.PlayerRotatedMessage;
import space.network.message.ShutdownMessage;
import space.network.message.TextMessage;

public class Connection {
	
	private static final int TEXT = 0;
	private static final int PLAYER_JOINING = 1;
	private static final int ENTITY_MOVED = 2;
	private static final int ENTITY_ROTATED = 3;
	private static final int DISCONNECT = 4;
	private static final int SHUTDOWN = 5;
	private static final int JUMP = 6;
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
	
	public boolean hasMessage() throws IOException{
			return incoming.available() > 0;
	}
	
	public boolean isClosed(){
		return socket.isClosed();
	}
	
	public Message readMessage() throws IOException{
		int type = incoming.read();
		int length = incoming.read();
		byte[] data = new byte[length];
		incoming.read(data);
		switch (type){
			case TEXT:
				return new TextMessage(data);
			case PLAYER_JOINING:
				return new PlayerJoiningMessage(data);
			case ENTITY_MOVED:
				return new EntityMovedMessage(data);
			case ENTITY_ROTATED:
				return new PlayerRotatedMessage(data);
			case DISCONNECT:
				return new DisconnectMessage(data);
			case SHUTDOWN:
				return new ShutdownMessage();
			case JUMP:
				return new JumpMessage(data);
			default:
				//TODO: decide how to deal with format error
				return null;
		}
	}
	
	public void sendMessage(Message message) throws IOException{
		int type = typeOf(message);
		byte[] data = message.toByteArray();
		
		outgoing.write(type);
		outgoing.write(data.length);
		outgoing.write(data);
	}
	
	private int typeOf(Message message){
		//TODO: decide how whether this is the best way to determine the type of a message
		if (message instanceof TextMessage) return TEXT;
		else if (message instanceof PlayerJoiningMessage) return PLAYER_JOINING;
		else if (message instanceof EntityMovedMessage) return ENTITY_MOVED;
		else if (message instanceof PlayerRotatedMessage) return ENTITY_ROTATED; //TODO: Check actual class when it exists
		else if (message instanceof DisconnectMessage) return DISCONNECT;
		else if (message instanceof ShutdownMessage) return SHUTDOWN;
		else if (message instanceof JumpMessage) return JUMP;
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
