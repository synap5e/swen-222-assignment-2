package space.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import space.network.message.DisconnectMessage;
import space.network.message.DropPickupMessage;
import space.network.message.EntityMovedMessage;
import space.network.message.InteractionMessage;
import space.network.message.JumpMessage;
import space.network.message.Message;
import space.network.message.PlayerJoiningMessage;
import space.network.message.EntityRotationMessage;
import space.network.message.ShutdownMessage;
import space.network.message.TextMessage;
import space.network.message.TransferMessage;

/**
 * Connection wraps a socket, providing a clean interface for reading and sending messages via the socket.
 * 
 * @author James Greenwood-Thessman (300289004)
 */
public class Connection {
	
	/*
	 * Message Types 
	 */
	private static final int TEXT = 0;
	private static final int PLAYER_JOINING = 1;
	private static final int ENTITY_MOVED = 2;
	private static final int ENTITY_ROTATED = 3;
	private static final int DISCONNECT = 4;
	private static final int SHUTDOWN = 5;
	private static final int JUMP = 6;
	private static final int INTERACTION = 7;
	private static final int DROP_PICKUP = 8;
	private static final int TRANSFER = 9;
	private static final int UNKNOWN = -1;

	/**
	 * The socket to send and receive from.
	 */
	private Socket socket;
	
	/**
	 * The output stream to send messages on.
	 */
	private OutputStream outgoing;
	
	/**
	 * The input stream to read messages from.
	 */
	private InputStream incoming;
	
	/**
	 * Create a connection that wraps the socket.
	 * 
	 * @param connectedSocket the socket to wrap
	 */
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
	
	/**
	 * Whether a message has been received, and is able to be read.
	 * 
	 * @return Whether a message has been received. Only the header must have been received before it is able to be read.
	 * @throws IOException if an I/O error occurs.
	 */
	public boolean hasMessage() throws IOException{
			return incoming.available() > 5;
	}
	
	/**
	 * Whether the socket has been closed.
	 * @return Whether the socket is closed.
	 */
	public boolean isClosed(){
		return socket.isClosed();
	}
	
	/**
	 * Reads a message that has been received from the socket.
	 * 
	 * @return The received message.
	 * @throws IOException if an I/O error occurs.
	 */
	public Message readMessage() throws IOException{
		int type = incoming.read();
		byte[] rawLength = new byte[4];
		incoming.read(rawLength);
		int length = ByteBuffer.wrap(rawLength).getInt();
		byte[] data = new byte[length];
		
		//Wait until the entire message has been received
		while (incoming.available() < length);
		
		incoming.read(data);
		switch (type){
			case TEXT:
				return new TextMessage(data);
			case PLAYER_JOINING:
				return new PlayerJoiningMessage(data);
			case ENTITY_MOVED:
				return new EntityMovedMessage(data);
			case ENTITY_ROTATED:
				return new EntityRotationMessage(data);
			case DISCONNECT:
				return new DisconnectMessage(data);
			case SHUTDOWN:
				return new ShutdownMessage();
			case JUMP:
				return new JumpMessage(data);
			case INTERACTION:
				return new InteractionMessage(data);
			case DROP_PICKUP:
				return new DropPickupMessage(data);
			case TRANSFER:
				return new TransferMessage(data);
			default:
				return null;
		}
	}
	
	/**
	 * Sends a message via the socket.
	 * 
	 * @param message the message to send.
	 * @throws IOException if an I/O error occurs.
	 */
	public void sendMessage(Message message) throws IOException{
		int type = typeOf(message);
		byte[] data = message.toByteArray();
		
		//Send the header
		outgoing.write(type);
		ByteBuffer length = ByteBuffer.allocate(4);
		length.putInt(data.length);
		outgoing.write(length.array());
		
		//Send the data
		outgoing.write(data);
	}
	
	/**
	 * Closes the connection by closing the socket.
	 */
	public void close(){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Determines the type of a message.
	 * @param message the message to determine the type of
	 * @return An integer representation of the type.
	 */
	private int typeOf(Message message){
		if (message instanceof TextMessage) return TEXT;
		else if (message instanceof PlayerJoiningMessage) return PLAYER_JOINING;
		else if (message instanceof EntityMovedMessage) return ENTITY_MOVED;
		else if (message instanceof EntityRotationMessage) return ENTITY_ROTATED;
		else if (message instanceof DisconnectMessage) return DISCONNECT;
		else if (message instanceof ShutdownMessage) return SHUTDOWN;
		else if (message instanceof JumpMessage) return JUMP;
		else if (message instanceof InteractionMessage) return INTERACTION;
		else if (message instanceof DropPickupMessage) return DROP_PICKUP;
		else if (message instanceof TransferMessage) return TRANSFER;
		else return UNKNOWN;
	}
}
