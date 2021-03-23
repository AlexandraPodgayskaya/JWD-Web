package by.epam.payment_system.dao;

import by.epam.payment_system.dao.impl.SQLAccountDAO;
import by.epam.payment_system.dao.impl.SQLAdditionalClientDataDAO;
import by.epam.payment_system.dao.impl.SQLCardDAO;
import by.epam.payment_system.dao.impl.SQLTransactionLogDAO;
import by.epam.payment_system.dao.impl.SQLUserDAO;

public final class DAOFactory {

	private static final DAOFactory instance = new DAOFactory();

	private final UserDAO userDAO = new SQLUserDAO();
	private final AdditionalClientDataDAO additionalClientDataDAO = new SQLAdditionalClientDataDAO();
	private final CardDAO cardDAO = new SQLCardDAO();
	private final AccountDAO accountDAO = new SQLAccountDAO();
	private final TransactionLogDAO transactionLogDAO = new SQLTransactionLogDAO();

	private DAOFactory() {
	}

	public static DAOFactory getInstance() {
		return instance;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public AdditionalClientDataDAO getAdditionalClientDataDAO() {
		return additionalClientDataDAO;
	}

	public CardDAO getCardDAO() {
		return cardDAO;
	}

	public AccountDAO getAccountDAO() {
		return accountDAO;
	}

	public TransactionLogDAO getTransactionLogDAO() {
		return transactionLogDAO;
	}
}
