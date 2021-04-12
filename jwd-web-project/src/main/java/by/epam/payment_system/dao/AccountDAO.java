package by.epam.payment_system.dao;

import java.sql.SQLException;
import java.util.Optional;

import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Account;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.entity.Transfer;

/**
 * The interface for working with database table accounts
 * 
 * @author Aleksandra Podgayskaya
 */
public interface AccountDAO {

	/**
	 * Create account
	 * 
	 * @param account {@link Account} all data to create
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	void create(Account account) throws DAOException;

	/**
	 * Get account by account number
	 * 
	 * @param numberAccount {@link String} account number
	 * @return {@link Optional} of {@link Account} received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	Optional<Account> getAccount(String numberAccount) throws DAOException;

	/**
	 * Get last account number
	 * 
	 * @return {@link Optional} of {@link Long} last account number
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	Optional<Long> getLastAccountNumber() throws DAOException;

	/**
	 * Update balance
	 * 
	 * @param transaction {@link Transaction} data to update
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	boolean updateBalance(Transaction transaction) throws DAOException;

	/**
	 * Do transfer
	 * 
	 * @param transfer {@link Transfer} details
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	boolean doTransfer(Transfer transfer) throws DAOException;

}
