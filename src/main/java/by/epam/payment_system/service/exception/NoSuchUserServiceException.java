package by.epam.payment_system.service.exception;

/**
 * Describes special exception in service
 * 
 * @author Aleksandra Podgayskaya
 * @see ServiceException
 */
public class NoSuchUserServiceException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public NoSuchUserServiceException() {
		super();
	}

	public NoSuchUserServiceException(String message) {
		super(message);
	}

	public NoSuchUserServiceException(Exception e) {
		super(e);
	}

	public NoSuchUserServiceException(String message, Exception e) {
		super(message, e);
	}
}
