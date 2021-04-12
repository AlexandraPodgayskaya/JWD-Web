package by.epam.payment_system.dao;

/**
 * Describes exception in DAO
 * 
 * @author Aleksandra Podgayskaya
 * @see Exception
 */
public class DAOException extends Exception {

	private static final long serialVersionUID = 1L;

	public DAOException() {
		super();
	}

	public DAOException(String message) {
		super(message);
	}

	public DAOException(Exception e) {
		super(e);
	}

	public DAOException(String message, Exception e) {
		super(message, e);
	}

}
