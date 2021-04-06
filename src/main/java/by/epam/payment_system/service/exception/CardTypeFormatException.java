package by.epam.payment_system.service.exception;

public class CardTypeFormatException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public CardTypeFormatException() {
		super();
	}

	public CardTypeFormatException(String message) {
		super(message);
	}

	public CardTypeFormatException(Exception e) {
		super(e);
	}

	public CardTypeFormatException(String message, Exception e) {
		super(message, e);
	}
}
