package space.serialization;

/**
 * A exception class that extends exception - indicates that the json save file
 * specified does not exist in that location
 * 
 * @author Shweta Barapatre (300287438)
 *
 */
public class SaveFileNotAccessibleException extends Exception {

	public SaveFileNotAccessibleException() {
		super();
	}

	public SaveFileNotAccessibleException(String message, Throwable e) {
		super(message, e);
	}

}
