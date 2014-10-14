package space.serialization;

/**
 * A exception class that is thrown when the save file is not valid or formatted
 * correctly
 * 
 * @author Shweta Barapatre (300287438)
 *
 */
public class SaveFileNotValidException extends Exception {
	public SaveFileNotValidException() {
		super();
	}

	public SaveFileNotValidException(String message, Throwable e) {
		super(message, e);
	}

}
