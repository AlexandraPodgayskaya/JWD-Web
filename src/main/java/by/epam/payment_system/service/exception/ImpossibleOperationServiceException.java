package by.epam.payment_system.service.exception;

public class ImpossibleOperationServiceException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public ImpossibleOperationServiceException() {
		super();
	}

	public ImpossibleOperationServiceException(String message) {
		super(message);
	}

	public ImpossibleOperationServiceException(Exception e) {
		super(e);
	}

	public ImpossibleOperationServiceException(String message, Exception e) {
		super(message, e);
	}
}
