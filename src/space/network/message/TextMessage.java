package space.network.message;

import java.nio.ByteBuffer;

/**
 * TextMessage represents text message that is able to be sent or received over the network.
 * 
 * @author James Greenwood-Thessman (greenwjame1)
 */
public class TextMessage implements Message {

	/**
	 * The text of the message
	 */
	private String text;
	
	/**
	 * Creates the text message from text
	 * 
	 * @param text the message
	 */
	public TextMessage(String text){
		this.text = text;
	}
	
	/**
	 * Creates the text message from data received over the network.
	 * 
	 * @param data the data received. Should be a byte array that holds chars.
	 */
	public TextMessage(byte[] data){
		text = ByteBuffer.wrap(data).asCharBuffer().toString();
	}
	
	/**
	 * Gets the text of the message
	 * @return The text.
	 */
	public String getText(){
		return text;
	}
	
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(text.length()*Character.SIZE/8);
		for (char c : text.toCharArray()){
			buffer.putChar(c);
		}
		return buffer.array();
	}
	
	@Override
	public String toString(){
		return "[TextMessage]: \"" + text + "\"";
	}

}
