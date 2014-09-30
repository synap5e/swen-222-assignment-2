package space.network.message;

/**
 * ShutdownMessage is the message used for informing clients that the server has shutdown and that they should also shutdown.
 * 
 * @author James Greenwood-Thessman (greenwjame1)
 */
public class ShutdownMessage implements Message {
	
	@Override
	public byte[] toByteArray() {
		return new byte[0];
	}
	
	@Override
	public String toString(){
		return "[Shutdown]: The server has shutdown";
	}
}
