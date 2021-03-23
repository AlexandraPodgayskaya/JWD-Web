package by.epam.payment_system.service.exception;

public class BlockCardServiceException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public BlockCardServiceException() {
		super();
	}

	public BlockCardServiceException(String message) {
		super(message);
	}

	public BlockCardServiceException(Exception e) {
		super(e);
	}

	public BlockCardServiceException(String message, Exception e) {
		super(message, e);
	}

}
