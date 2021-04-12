package by.epam.payment_system.service.exception;

import java.util.List;

/**
 * Describes special exception in service
 * 
 * @author Aleksandra Podgayskaya
 * @see ServiceException
 */
public class TransactionDataServiceException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public TransactionDataServiceException() {
		super();
	}

	public TransactionDataServiceException(String message) {
		super(message);
	}

	public TransactionDataServiceException(Exception e) {
		super(e);
	}

	public TransactionDataServiceException(String message, Exception e) {
		super(message, e);
	}

	public TransactionDataServiceException(String message, List<String> errorDescriptions) {
		super(message, errorDescriptions);
	}

}
