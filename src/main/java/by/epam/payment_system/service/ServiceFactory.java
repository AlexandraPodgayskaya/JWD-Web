package by.epam.payment_system.service;

import by.epam.payment_system.service.impl.AdditionalClientDataServiceImpl;
import by.epam.payment_system.service.impl.CardServiceImpl;
import by.epam.payment_system.service.impl.UserServiceImpl;

public final class ServiceFactory {

	private static final ServiceFactory instance = new ServiceFactory();

	private final UserService userService = new UserServiceImpl();
	private final AdditionalClientDataService additionalClientDataService = new AdditionalClientDataServiceImpl();
	private final CardService cardService = new CardServiceImpl();
	
	private ServiceFactory() {
	}

	public static ServiceFactory getInstance() {
		return instance;
	}

	public UserService getUserService() {
		return userService;
	}
	
	public AdditionalClientDataService getAdditionalClientDataService() {
		return additionalClientDataService;
	}
	
	public CardService getCardService() {
		return cardService;
	}

}
