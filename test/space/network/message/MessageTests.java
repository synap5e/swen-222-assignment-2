package space.network.message;

import static org.junit.Assert.*;

import org.junit.Test;

public class MessageTests {

	@Test
	public void testTextMessageKeepsText() {
		String text = "test message";
		TextMessage message = new TextMessage(text);
		
		assertEquals(text, message.getText());
	}

	@Test
	public void testTextMessageFromIntArray() {
		String text = "test message";
		TextMessage original = new TextMessage(text);
		
		int[] data = original.toIntArray();
		
		TextMessage message = new TextMessage(data);

		assertEquals(text, message.getText());
	}
}
