package space.serialization;

public class SaveFileNotAccessibleException extends Exception {

	public SaveFileNotAccessibleException() {
		super();
	}

	public SaveFileNotAccessibleException(String message, Throwable e) {
		super(message, e);
	}

}
