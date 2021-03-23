package by.epam.payment_system.dao.impl;

import by.epam.payment_system.dao.DAOException;

public class DAOUnsupportedOperationException extends DAOException {
	
	private static final long serialVersionUID = 1L;

	public DAOUnsupportedOperationException() {
		super();
	}
	
	public DAOUnsupportedOperationException(String message) {
		super(message);
	}
	
	public DAOUnsupportedOperationException(Exception e) {
		super(e);
	}

	public DAOUnsupportedOperationException(String message, Exception e) {
		super(message, e);
	}
}
