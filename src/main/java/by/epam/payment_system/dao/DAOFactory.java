package by.epam.payment_system.dao;

import by.epam.payment_system.dao.impl.SQLAccountDAO;
import by.epam.payment_system.dao.impl.SQLAdditionalClientDataDAO;
import by.epam.payment_system.dao.impl.SQLCardDAO;
import by.epam.payment_system.dao.impl.SQLCardTypeDAO;
import by.epam.payment_system.dao.impl.SQLTransactionLogDAO;
import by.epam.payment_system.dao.impl.SQLUserDAO;

/**
 * The factory is responsible for DAO instances
 * 
 * @author Aleksandra Podgayskaya
 */
public final class DAOFactory {

	/**
	 * Instance of {@link DAOFactory}
	 */
	private static final DAOFactory instance = new DAOFactory();

	/**
	 * Instance of {@link UserDAO}
	 */
	private final UserDAO userDAO = new SQLUserDAO();

	/**
	 * Instance of {@link AdditionalClientDataDAO}
	 */
	private final AdditionalClientDataDAO additionalClientDataDAO = new SQLAdditionalClientDataDAO();

	/**
	 * Instance of {@link CardDAO}
	 */
	private final CardDAO cardDAO = new SQLCardDAO();

	/**
	 * Instance of {@link AccountDAO}
	 */
	private final AccountDAO accountDAO = new SQLAccountDAO();

	/**
	 * Instance of {@link TransactionLogDAO}
	 */
	private final TransactionLogDAO transactionLogDAO = new SQLTransactionLogDAO();

	/**
	 * Instance of {@link CardTypeDAO}
	 */
	private final CardTypeDAO cardTypeDAO = new SQLCardTypeDAO();

	/**
	 * Does not create an object of this class
	 */
	private DAOFactory() {
	}

	/**
	 * Get instance of this class
	 * 
	 * @return {@link DAOFactory} instance
	 */
	public static DAOFactory getInstance() {
		return instance;
	}

	/**
	 * Get instance of {@link UserDAO}
	 * 
	 * @return {@link UserDAO} instance
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * Get instance of {@link AdditionalClientDataDAO}
	 * 
	 * @return {@link AdditionalClientDataDAO} instance
	 */
	public AdditionalClientDataDAO getAdditionalClientDataDAO() {
		return additionalClientDataDAO;
	}

	/**
	 * Get instance of {@link CardDAO}
	 * 
	 * @return {@link CardDAO} instance
	 */
	public CardDAO getCardDAO() {
		return cardDAO;
	}

	/**
	 * Get instance of {@link AccountDAO}
	 * 
	 * @return {@link AccountDAO} instance
	 */
	public AccountDAO getAccountDAO() {
		return accountDAO;
	}

	/**
	 * Get instance of {@link TransactionLogDAO}
	 * 
	 * @return {@link TransactionLogDAO} instance
	 */
	public TransactionLogDAO getTransactionLogDAO() {
		return transactionLogDAO;
	}

	/**
	 * Get instance of {@link CardTypeDAO}
	 * 
	 * @return {@link CardTypeDAO} instance
	 */
	public CardTypeDAO getCardTypeDAO() {
		return cardTypeDAO;
	}

}
