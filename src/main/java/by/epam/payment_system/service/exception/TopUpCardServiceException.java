package by.epam.payment_system.service.exception;

public class TopUpCardServiceException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public TopUpCardServiceException() {
		super();
	}

	public TopUpCardServiceException(String message) {
		super(message);
	}

	public TopUpCardServiceException(Exception e) {
		super(e);
	}

	public TopUpCardServiceException(String message, Exception e) {
		super(message, e);
	}
}
