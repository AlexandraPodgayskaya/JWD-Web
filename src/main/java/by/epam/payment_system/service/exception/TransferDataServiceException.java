package by.epam.payment_system.service.exception;

import java.util.List;

public class TransferDataServiceException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public TransferDataServiceException() {
		super();
	}

	public TransferDataServiceException(String message) {
		super(message);
	}

	public TransferDataServiceException(Exception e) {
		super(e);
	}

	public TransferDataServiceException(String message, Exception e) {
		super(message, e);
	}
	
	public TransferDataServiceException(String message, List <String> errorDescriptions) {
		super(message, errorDescriptions);
	}

}
