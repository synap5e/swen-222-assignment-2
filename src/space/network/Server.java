package space.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.Sys;

import space.gui.pipeline.viewable.ViewableRoom.LightMode;
import space.math.Vector2D;
import space.network.message.Message;
import space.network.message.PlayerJoinedMessage;
import space.network.message.TextMessage;
import space.world.Player;
import space.world.Room;
import space.world.World;

//TODO: Work out a way to let the server be shutdown nicely
public class Server {
	
	private Thread connectionHandler;
	private Thread gameLoop;
	
	private boolean stillAlive;
	private ServerSocket socket;
	private Map<Integer, Connection> connections;
	
	private World world;
	
	//TODO: Work out better way of coming up with an ID
	private int availableId = 9001;
	
	public Server(String host, int port){
		//Create the list of client connections
		connections = new HashMap<Integer, Connection>();
		
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
		
		//Load the World
		//TODO: Load world from file
		world = new World();
		Room r = new Room(LightMode.BASIC_LIGHT, 1, "temp", Arrays.asList(new Vector2D(-20, 20), new Vector2D(20, 20), new Vector2D(20, -20), new Vector2D(-20, -20)));
		world.addRoom(r);
		
		//Start accepting connections
		connectionHandler.start();
		
		//Start the game logic
		gameLoop = new Thread(new ServerGameLoop());
		gameLoop.start();
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
		synchronized (connections) {
			for (Connection c : connections.values()){
				//TODO: use a better way to tell the client the server is shutting down
				c.sendMessage(new TextMessage("Server Shutting Down"));
				c.close();
			}
		}
	}
	
	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	private class ServerGameLoop implements Runnable {
		
		@Override
		public void run() {
			long last = getTime();
			while (stillAlive){
				long now = getTime();
				long delta = now - last;
				
				synchronized (connections) {
					for (Map.Entry<Integer, Connection> cons : connections.entrySet()){
						Connection con = cons.getValue();
						int id = cons.getKey();
						
						while (con.hasMessage()){
							//TODO: Actually apply updates
							System.out.println(con.readMessage());
							
							//TODO: Send out updates to other clients
						}
					}
				}
				
				//Update world TODO: World needs an update method that updates all updatable entities
				//world.update(delta);
				
				last = now;
			}
		}
	}
	
	private class ConnectionHandler implements Runnable {
		
		@Override
		public void run() {
			Socket socketConnection;
			while(stillAlive){
				try {
					socketConnection = socket.accept();
					int id = availableId++;
					synchronized (connections) {
						connections.put(id, new Connection(socketConnection));
					}
					synchronized (world){
						world.addEntity(new Player(new Vector2D(0, 0), id));
						
						//Tell clients about new player. The new client will use the id given.
						Message playerJoined = new PlayerJoinedMessage(id);
						for (Connection con : connections.values()){
							con.sendMessage(playerJoined);
						}
					}
				} catch (IOException e) {
					System.err.println("Connection Attempt Failed");
				}
			}
		}
	}
}
