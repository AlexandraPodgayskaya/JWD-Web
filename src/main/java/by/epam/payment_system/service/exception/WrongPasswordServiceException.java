package by.epam.payment_system.service.exception;

public class WrongPasswordServiceException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public WrongPasswordServiceException() {
		super();
	}

	public WrongPasswordServiceException(String message) {
		super(message);
	}

	public WrongPasswordServiceException(Exception e) {
		super(e);
	}

	public WrongPasswordServiceException(String message, Exception e) {
		super(message, e);
	}
}
