package by.epam.payment_system.service.exception;

public class BusyLoginServiceException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public BusyLoginServiceException() {
		super();
	}

	public BusyLoginServiceException(String message) {
		super(message);
	}

	public BusyLoginServiceException(Exception e) {
		super(e);
	}

	public BusyLoginServiceException(String message, Exception e) {
		super(message, e);
	}
}
