package by.epam.payment_system.service;

import by.epam.payment_system.service.impl.AdditionalClientDataServiceImpl;
import by.epam.payment_system.service.impl.CardServiceImpl;
import by.epam.payment_system.service.impl.TransactionServiceImpl;
import by.epam.payment_system.service.impl.UserServiceImpl;

/**
 * The factory is responsible for service instances
 * 
 * @author Aleksandra Podgayskaya
 */
public final class ServiceFactory {

	/**
	 * Instance of {@link ServiceFactory}
	 */
	private static final ServiceFactory instance = new ServiceFactory();

	/**
	 * Instance of {@link UserService}
	 */
	private final UserService userService = new UserServiceImpl();

	/**
	 * Instance of {@link AdditionalClientDataService}
	 */
	private final AdditionalClientDataService additionalClientDataService = new AdditionalClientDataServiceImpl();

	/**
	 * Instance of {@link CardService}
	 */
	private final CardService cardService = new CardServiceImpl();

	/**
	 * Instance of {@link TransactionService}
	 */
	private final TransactionService transactionService = new TransactionServiceImpl();

	/**
	 * Does not create an object of this class
	 */
	private ServiceFactory() {
	}

	/**
	 * Get instance of this class
	 * 
	 * @return {@link ServiceFactory} instance
	 */
	public static ServiceFactory getInstance() {
		return instance;
	}

	/**
	 * Get instance of {@link UserService}
	 * 
	 * @return {@link UserService} instance
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Get instance of {@link AdditionalClientDataService}
	 * 
	 * @return {@link AdditionalClientDataService} instance
	 */
	public AdditionalClientDataService getAdditionalClientDataService() {
		return additionalClientDataService;
	}

	/**
	 * Get instance of {@link CardService}
	 * 
	 * @return {@link CardService} instance
	 */
	public CardService getCardService() {
		return cardService;
	}

	/**
	 * Get instance of {@link TransactionService}
	 * 
	 * @return {@link TransactionService} instance
	 */
	public TransactionService getTransactionService() {
		return transactionService;
	}

}
