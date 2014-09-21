package space.network.message;

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
	 * @param data the data received. Each integer must be a char in the text.
	 */
	public TextMessage(int[] data){
		char[] mess = new char[data.length];
		for (int i = 0; i < data.length; ++i){
			mess[i] = (char) data[i];
		}
		text = String.valueOf(mess);
	}
	
	/**
	 * Gets the text of the message
	 * @return The text.
	 */
	public String getText(){
		return text;
	}
	
	@Override
	public int[] toIntArray() {
		int[] data = new int[text.length()];
		char[] chars =  text.toCharArray();
		for (int i = 0; i < chars.length; ++i){
			data[i] = chars[i];
		}
		return data;
	}
	
	@Override
	public String toString(){
		return "[TextMessage]: \"" + text + "\"";
	}

}
