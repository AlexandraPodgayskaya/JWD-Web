package by.epam.payment_system.service.impl;

import java.util.Arrays;

import java.util.List;
import java.util.Optional;

import by.epam.payment_system.dao.AccountDAO;
import by.epam.payment_system.dao.CardDAO;
import by.epam.payment_system.dao.CardTypeDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.entity.Account;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.CardInfo;
import by.epam.payment_system.entity.CardStatus;
import by.epam.payment_system.entity.CardType;
import by.epam.payment_system.entity.Currency;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.AdditionalClientDataService;
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.ServiceException;

/**
 * The service is responsible for card operations
 * 
 * @author Aleksandra Podgayskaya
 * @see CardService
 */
public class CardServiceImpl implements CardService {

	/**
	 * Instance of {@link DAOFactory}
	 */
	private static final DAOFactory factory = DAOFactory.getInstance();

	private static final int FIRST_ACCOUNT_NUMBER = 21081001;
	private static final long FIRST_CARD_NUMBER = 5489333344441111L;
	private static final int INCREMENT = 1;

	/**
	 * Take all cards by userId
	 * 
	 * @param userId {@link Integer} user id
	 * @return {@link List} of {@link Card} received from database
	 * @throws ServiceException if userId is null or {@link DAOException} occurs
	 */
	@Override
	public List<Card> takeCards(Integer userId) throws ServiceException {

		if (userId == null) {
			throw new ImpossibleOperationServiceException("no user id to take cards");
		}

		CardDAO cardDAO = factory.getCardDAO();
		AccountDAO accountDAO = factory.getAccountDAO();

		List<Card> cardList;
		try {
			cardList = cardDAO.findCards(userId);
			Optional<Account> accountOptional;
			Account account;
			for (Card card : cardList) {
				accountOptional = accountDAO.getAccount(card.getNumberAccount());
				if (!accountOptional.isEmpty()) {
					account = accountOptional.get();
					card.setBalance(account.getBalance());
					card.setCurrency(account.getCurrency());
				}
			}
		} catch (DAOException e) {
			throw new ServiceException("card search error", e);
		}
		return cardList;
	}

	/**
	 * Take cards to which you can make a transfer
	 * 
	 * @param userId        {@link Integer} user id of the card opening initiator
	 * @param numberAccount {@link String} sender's account number
	 * @param currency      {@link String} transfer currency
	 * @return {@link List} of {@link Card} received from database and sorted for
	 *         transfer
	 * @throws ServiceException if parameters are null or
	 *                          {@link IllegalArgumentException} occurs
	 */
	@Override
	public List<Card> takeCardsForTransfer(Integer userId, String numberAccount, String currency)
			throws ServiceException {
		if (userId == null || numberAccount == null || currency == null) {
			throw new ImpossibleOperationServiceException("incorrect data");
		}

		List<Card> cardList = takeCards(userId);

		try {
			cardList.removeIf(card -> numberAccount.equals(card.getNumberAccount())
					|| card.getCurrency() != Currency.valueOf(currency.toUpperCase()) || card.getIsBlocked() == true
					|| card.getIsClosed() == true);
		} catch (IllegalArgumentException e) {
			throw new ServiceException("incorrect currency", e);
		}

		return cardList;
	}

	/**
	 * Set a lock on a card
	 * 
	 * @param numberCard {@link String} card number
	 * @throws ServiceException if numberCard is null, lock is not set or
	 *                          {@link DAOException} occurs
	 */
	@Override
	public void blockCard(String numberCard) throws ServiceException {

		if (numberCard == null) {
			throw new ImpossibleOperationServiceException("no card number to block");
		}

		CardDAO cardDAO = factory.getCardDAO();

		Card card = new Card(numberCard, Boolean.TRUE);

		try {
			if (!cardDAO.updateBlocking(card)) {
				throw new ImpossibleOperationServiceException("card blocking error");
			}

		} catch (DAOException e) {
			throw new ServiceException("card blocking error", e);
		}
	}

	/**
	 * Unblock a card
	 * 
	 * @param numberCard {@link String} card number
	 * @throws ServiceException if numberCard is null, card is not unlocked or
	 *                          {@link DAOException} occurs
	 */
	@Override
	public void unblockCard(String numberCard) throws ServiceException {
		if (numberCard == null) {
			throw new ImpossibleOperationServiceException("no card number to unblock");
		}

		CardDAO cardDAO = factory.getCardDAO();

		Card card = new Card(numberCard, Boolean.FALSE);

		try {
			if (!cardDAO.updateBlocking(card)) {
				throw new ImpossibleOperationServiceException("card unblocking error");
			}

		} catch (DAOException e) {
			throw new ServiceException("card unblocking error", e);
		}

	}

	/**
	 * Close a card
	 * 
	 * @param numberCard {@link String} card number
	 * @throws ServiceException if numberCard is null, card is not closed or
	 *                          {@link DAOException} occurs
	 */
	@Override
	public void closeCard(String numberCard) throws ServiceException {

		if (numberCard == null) {
			throw new ImpossibleOperationServiceException("no card number to close");
		}

		CardDAO cardDAO = factory.getCardDAO();

		try {
			if (!cardDAO.setClosed(numberCard)) {
				throw new ImpossibleOperationServiceException("card closing error");
			}

		} catch (DAOException e) {
			throw new ServiceException("card closing error", e);
		}

	}

	/**
	 * Take all the parameters of the cards and a list of the user's main cards
	 * 
	 * @param userId {@link Integer} user id
	 * @return {@link CardInfo}
	 * @throws ServiceException if userId is null or {@link DAOException} occurs
	 */
	@Override
	public CardInfo takeAllCardOptions(Integer userId) throws ServiceException {
		if (userId == null) {
			throw new ImpossibleOperationServiceException("no data to take all card options");
		}
		CardInfo cardInfo = new CardInfo();

		List<Card> cardList = takeCards(userId);
		cardList.removeIf(card -> card.getStatus() == CardStatus.ADDITIONAL);
		cardInfo.setCardList(cardList);

		CardTypeDAO cardTypeDAO = factory.getCardTypeDAO();
		List<CardType> cardTypeList;

		try {
			cardTypeList = cardTypeDAO.findAll();
			cardInfo.setCardTypeList(cardTypeList);
			cardInfo.setCurrencyList(Arrays.asList(Currency.values()));
		} catch (DAOException e) {
			throw new ServiceException("card type search error", e);
		}

		return cardInfo;
	}

	/**
	 * Open main card
	 * 
	 * @param card {@link Card} opening data
	 * @throws ServiceException if card is null or {@link DAOException} occurs
	 */
	@Override
	public void openMainCard(Card card) throws ServiceException {
		if (card == null) {
			throw new ImpossibleOperationServiceException("no card data to open");
		}

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		AdditionalClientDataService additionalClientDataService = serviceFactory.getAdditionalClientDataService();
		additionalClientDataService.getData(card.getOwnerId());

		Account account = new Account(generateAccountNumber(), card.getCurrency(), card.getOwnerId());
		AccountDAO accountDAO = factory.getAccountDAO();
		CardDAO cardDAO = factory.getCardDAO();
		try {
			accountDAO.create(account);
			card.setNumberAccount(account.getNumberAccount());
			card.setNumberCard(generateCardNumber());

			cardDAO.create(card);
		} catch (DAOException e) {
			throw new ServiceException("card openning error", e);
		}
	}

	/**
	 * Open additional card
	 * 
	 * @param card                   {@link Card} opening data
	 * @param personalNumberPassport {@link String} personal number passport of the
	 *                               additional card holder
	 * @param userId                 {@link Integer} user id of the card opening
	 *                               initiator.
	 * @throws ServiceException if data to open a card is null, incorrect account
	 *                          number or {@link DAOException} occurs
	 */
	@Override
	public void openAdditionalCard(Card card, String personalNumberPassport, Integer userId) throws ServiceException {
		if (card == null || card.getNumberAccount() == null || card.getCardType() == null
				|| card.getCardType().getId() == null) {
			throw new ImpossibleOperationServiceException("no card data to open");
		}

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		AdditionalClientDataService additionalClientDataService = serviceFactory.getAdditionalClientDataService();
		UserInfo userInfo = additionalClientDataService.search(personalNumberPassport);
		card.setOwnerId(userInfo.getId());

		AccountDAO accountDAO = factory.getAccountDAO();
		CardDAO cardDAO = factory.getCardDAO();
		try {
			Optional<Account> accountOptional = accountDAO.getAccount(card.getNumberAccount());
			if (accountOptional.isEmpty() || !accountOptional.get().getOwnerId().equals(userId)) {
				throw new ImpossibleOperationServiceException("impossible operation for an account");
			}
			card.setNumberCard(generateCardNumber());
			cardDAO.create(card);
		} catch (DAOException e) {
			throw new ServiceException("card openning error", e);
		}

	}

	/**
	 * Generate account number
	 * 
	 * @return {@link String} new number account
	 * @throws ServiceException if {@link DAOException} occurs
	 */
	private String generateAccountNumber() throws ServiceException {
		AccountDAO accountDAO = factory.getAccountDAO();
		Long newAccountNumber;
		try {
			Optional<Long> lastAccountOptional = accountDAO.getLastAccountNumber();
			newAccountNumber = lastAccountOptional.isEmpty() ? FIRST_ACCOUNT_NUMBER
					: Long.sum(lastAccountOptional.get(), INCREMENT);
		} catch (DAOException e) {
			throw new ServiceException("generate account number error", e);
		}
		return String.valueOf(newAccountNumber);
	}

	/**
	 * Generate card number
	 * 
	 * @return {@link String} new number card
	 * @throws ServiceException if {@link DAOException} occurs
	 */
	private String generateCardNumber() throws ServiceException {
		CardDAO cardDAO = factory.getCardDAO();
		Long newCardNumber;
		try {
			Optional<Long> lastCardOptional = cardDAO.getLastCardNumber();
			newCardNumber = lastCardOptional.isEmpty() ? FIRST_CARD_NUMBER
					: Long.sum(lastCardOptional.get(), INCREMENT);
		} catch (DAOException e) {
			throw new ServiceException("generate card number error", e);
		}
		return String.valueOf(newCardNumber);

	}

}
