package space.network;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import space.network.message.TextMessage;

public class ConnectionTests {

	private static final String host = "localhost";
	private static final int port = 1234;
	
	private static ServerSocket serverSocket;
	
	private Connection serverside;
	private Connection clientside;
	
	/*
	 * Methods that setup and cleanup for the tests
	 */
	
	@BeforeClass
	public static void initialiseServerSocket(){
		try {
			serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Before
	public void initialiseConnections(){
		Thread server = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					serverside = new Connection(serverSocket.accept());
				} catch (IOException e) {
					fail(e.getMessage());
				}
			}
		});
		server.start();
		
		try {
			clientside = new Connection(new Socket(host, port));
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		try {
			server.join();
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
	}
	
	@After
	public void closeConnections(){
		serverside.close();
		clientside.close();
	}
	
	/*
	 * Connection Tests 
	 */
	
	@Test
	public void testServerAndClientConnects() {
		assertNotNull(serverside);
		assertNotNull(clientside);
	}
	
	@Test
	public void testConnectionsStartWithNoMessages() {
		try {
			assertFalse(serverside.hasMessage());
			assertFalse(clientside.hasMessage());
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test
	public void testSendingAndRecievingMessage() {
		String text = "test123";
		TextMessage testMessage = new TextMessage(text);
		try {
			serverside.sendMessage(testMessage);
			assertTrue(clientside.hasMessage());
			String message = ((TextMessage) clientside.readMessage()).getText();
			assertEquals(text, message);
		} catch (IOException e) {
			fail();
		}
	}

	//TODO: Write tests for when client/server disconnects unexpectedly
}
