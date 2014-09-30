package space.network.message;

import static org.junit.Assert.*;

import org.junit.Test;

import space.math.Vector2D;

public class MessageTests {

	@Test
	public void testTextMessageKeepsText() {
		String text = "test message";
		TextMessage message = new TextMessage(text);
		
		assertEquals(text, message.getText());
	}

	@Test
	public void testTextMessageFromByteArray() {
		String text = "test message";
		TextMessage original = new TextMessage(text);
		
		byte[] data = original.toByteArray();
		
		TextMessage message = new TextMessage(data);

		assertEquals(text, message.getText());
	}
	
	@Test
	public void testPlayerJoinedKeepID() {
		int id = 1234;
		
		PlayerJoiningMessage message = new PlayerJoiningMessage(id);
		
		assertEquals(id, message.getPlayerID());
	}

	@Test
	public void testPlayerMovedFromByteArray() {
		int id = 1234;
		
		PlayerJoiningMessage original = new PlayerJoiningMessage(id);
		byte[] data = original.toByteArray();
		PlayerJoiningMessage message = new PlayerJoiningMessage(data);
		
		assertEquals(id, message.getPlayerID());
	}
	
	@Test
	public void testEntityMovedKeepsValues() {
		int id = 1234;
		float x = 22;
		float y = 42;
		
		EntityMovedMessage message = new EntityMovedMessage(id, new Vector2D(x, y));
		
		assertEquals(id, message.getEntityID());
		assertEquals(x, message.getNewPosition().getX(), 0);
		assertEquals(y, message.getNewPosition().getY() , 0);
	}

	@Test
	public void testEntityMovedFromByteArray() {
		int id = 1234;
		float x = 22;
		float y = 42;
		
		EntityMovedMessage original = new EntityMovedMessage(id, new Vector2D(x, y));
		byte[] data = original.toByteArray();
		EntityMovedMessage message = new EntityMovedMessage(data);
		
		assertEquals(id, message.getEntityID());
		assertEquals(x, message.getNewPosition().getX(), 0);
		assertEquals(y, message.getNewPosition().getY() , 0);
	}
}
