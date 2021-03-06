package by.epam.payment_system.controller.command;

/**
 * Describes exception in command
 * 
 * @author Aleksandra Podgayskaya
 * @see Exception
 */
public class CommandException extends Exception {

	private static final long serialVersionUID = 1L;

	public CommandException() {
		super();
	}

	public CommandException(String message) {
		super(message);
	}

	public CommandException(Exception e) {
		super(e);
	}

	public CommandException(String message, Exception e) {
		super(message, e);
	}

}
