package space.serialization;

public class SaveFileNotValidException extends Exception {
	public SaveFileNotValidException() {
		super();
	}

	public SaveFileNotValidException(String message, Throwable e) {
		super(message, e);
	}

}
