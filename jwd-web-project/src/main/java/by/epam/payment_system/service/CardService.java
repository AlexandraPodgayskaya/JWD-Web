package by.epam.payment_system.service;

import java.util.List;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.CardInfo;
import by.epam.payment_system.service.exception.ServiceException;

/**
 * The interface for card operations
 * 
 * @author Aleksandra Podgayskaya
 */
public interface CardService {

	/**
	 * Take all cards by userId
	 * 
	 * @param userId {@link Integer} user id
	 * @return {@link List} of {@link Card} received from database
	 * @throws ServiceException if userId is null or {@link DAOException} occurs
	 */
	List<Card> takeCards(Integer userId) throws ServiceException;

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
	List<Card> takeCardsForTransfer(Integer userId, String numberAccount, String currency) throws ServiceException;

	/**
	 * Set a lock on a card
	 * 
	 * @param numberCard {@link String} card number
	 * @throws ServiceException if numberCard is null, lock is not set or
	 *                          {@link DAOException} occurs
	 */
	void blockCard(String numberCard) throws ServiceException;

	/**
	 * Unblock a card
	 * 
	 * @param numberCard {@link String} card number
	 * @throws ServiceException if numberCard is null, card is not unlocked or
	 *                          {@link DAOException} occurs
	 */
	void unblockCard(String numberCard) throws ServiceException;

	/**
	 * Close a card
	 * 
	 * @param numberCard {@link String} card number
	 * @throws ServiceException if numberCard is null, card is not closed or
	 *                          {@link DAOException} occurs
	 */
	void closeCard(String numberCard) throws ServiceException;

	/**
	 * Take all the parameters of the cards and a list of the user's main cards
	 * 
	 * @param userId {@link Integer} user id
	 * @return {@link CardInfo}
	 * @throws ServiceException if userId is null or {@link DAOException} occurs
	 */
	CardInfo takeAllCardOptions(Integer userId) throws ServiceException;

	/**
	 * Open main card
	 * 
	 * @param card {@link Card} opening data
	 * @throws ServiceException if card is null or {@link DAOException} occurs
	 */
	void openMainCard(Card card) throws ServiceException;

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
	void openAdditionalCard(Card card, String personalNumberPassport, Integer userId) throws ServiceException;

}
