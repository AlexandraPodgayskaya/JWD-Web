package by.epam.payment_system.service.exception;

import java.util.List;

/**
 * Describes exception in service
 * 
 * @author Aleksandra Podgayskaya
 * @see Exception
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Keeps {@link List} of {@link String} error names
	 */
	private List<String> errorDescription;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Exception e) {
		super(e);
	}

	public ServiceException(String message, Exception e) {
		super(message, e);
	}

	public ServiceException(String message, List<String> errorDescription) {
		super(message);
		this.errorDescription = errorDescription;
	}

	/**
	 * Get error names
	 * 
	 * @return {@link List} of {@link String} error names
	 */
	public List<String> getErrorDescription() {
		return errorDescription;
	}

}
