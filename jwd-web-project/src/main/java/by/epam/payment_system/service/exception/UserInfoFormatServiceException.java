package by.epam.payment_system.service.exception;

import java.util.List;

/**
 * Describes special exception in service
 * 
 * @author Aleksandra Podgayskaya
 * @see ServiceException
 */
public class UserInfoFormatServiceException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public UserInfoFormatServiceException() {
		super();
	}

	public UserInfoFormatServiceException(String message) {
		super(message);
	}

	public UserInfoFormatServiceException(Exception e) {
		super(e);
	}

	public UserInfoFormatServiceException(String message, Exception e) {
		super(message, e);
	}

	public UserInfoFormatServiceException(String message, List<String> errorDescriptions) {
		super(message, errorDescriptions);
	}
}
