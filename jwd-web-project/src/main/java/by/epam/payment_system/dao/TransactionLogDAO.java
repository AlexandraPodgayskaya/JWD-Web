package by.epam.payment_system.dao;

import java.sql.SQLException;
import java.util.List;

import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.Transaction;

/**
 * The interface for working with database table transaction_log
 * 
 * @author Aleksandra Podgayskaya
 */
public interface TransactionLogDAO {

	/**
	 * Add transaction
	 * 
	 * @param transaction {@link Transaction} all data to add
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	void addTransaction(Transaction transaction) throws DAOException;

	/**
	 * Find all card transactions by card number
	 * 
	 * @param numberCard {@link String} card number to search
	 * @return {@link List} of {@link Card} received from database if transactions
	 *         are found, else emptyList
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	List<Transaction> findCardTransactions(String numberCard) throws DAOException;

	/**
	 * Find all account transactions by account number
	 * 
	 * @param numberAccount {@link String} account number to search
	 * @return {@link List} of {@link Card} received from database if transactions
	 *         are found, else emptyList
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	List<Transaction> findAccountTransactions(String numberAccount) throws DAOException;

}
