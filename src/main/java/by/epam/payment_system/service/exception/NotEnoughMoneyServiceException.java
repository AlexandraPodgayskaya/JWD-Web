package by.epam.payment_system.service.exception;

/**
 * Describes special exception in service
 * 
 * @author Aleksandra Podgayskaya
 * @see ServiceException
 */
public class NotEnoughMoneyServiceException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public NotEnoughMoneyServiceException() {
		super();
	}

	public NotEnoughMoneyServiceException(String message) {
		super(message);
	}

	public NotEnoughMoneyServiceException(Exception e) {
		super(e);
	}

	public NotEnoughMoneyServiceException(String message, Exception e) {
		super(message, e);
	}
}
