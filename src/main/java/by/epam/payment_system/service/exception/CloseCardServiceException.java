package by.epam.payment_system.service.exception;

public class CloseCardServiceException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public CloseCardServiceException() {
		super();
	}

	public CloseCardServiceException(String message) {
		super(message);
	}

	public CloseCardServiceException(Exception e) {
		super(e);
	}

	public CloseCardServiceException(String message, Exception e) {
		super(message, e);
	}
}
