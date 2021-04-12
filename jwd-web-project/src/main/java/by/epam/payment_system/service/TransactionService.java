package by.epam.payment_system.service;

import java.util.List;
import java.util.Map;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.service.exception.ServiceException;

/**
 * The interface for transactions
 * 
 * @author Aleksandra Podgayskaya
 */
public interface TransactionService {

	/**
	 * Top up card
	 * 
	 * @param transferDetails {@link Map} data for card replenishment
	 * @throws ServiceException if transferDetails is incorrect, transaction is not
	 *                          possible or {@link DAOException} occurs
	 */
	void topUpCard(Map<String, String> transferDetails) throws ServiceException;

	/**
	 * Make payment
	 * 
	 * @param paymentDetails {@link Map} payment data
	 * @throws ServiceException if password or paymentDetails are incorrect,
	 *                          transaction is not possible, amount more than
	 *                          balance or {@link DAOException} occurs
	 */
	void makePayment(Map<String, String> paymentDetails) throws ServiceException;

	/**
	 * Make transfer
	 * 
	 * @param transferDetails {@link Map} data for transfer
	 * @throws ServiceException if password or transferDetails are incorrect,
	 *                          transaction is not possible, amount more than
	 *                          balance or {@link DAOException} occurs
	 */
	void makeTransfer(Map<String, String> transferDetails) throws ServiceException;

	/**
	 * Take all account transactions
	 * 
	 * @param numberCard {@link String} number card
	 * @return {@link List} of {@link Transaction} received from database
	 * @throws ServiceException if numberCard is null, transaction is not possible
	 *                          or {@link DAOException} occurs
	 */
	List<Transaction> takeAccountTransactions(String numberCard) throws ServiceException;

	/**
	 * Take all card transactions
	 * 
	 * @param numberCard {@link String} number card
	 * @return {@link List} of {@link Transaction} received from database
	 * @throws ServiceException if numberCard is null or {@link DAOException} occurs
	 */
	List<Transaction> takeCardTransactions(String numberCard) throws ServiceException;
}
