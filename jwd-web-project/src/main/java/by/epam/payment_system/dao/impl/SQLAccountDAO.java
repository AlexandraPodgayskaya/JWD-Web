package by.epam.payment_system.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import by.epam.payment_system.dao.AccountDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Account;
import by.epam.payment_system.entity.Currency;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.entity.TransactionType;
import by.epam.payment_system.entity.Transfer;

/**
 * Works with database table accounts
 * 
 * @author Aleksandra Podgayskaya
 * @see AccountDAO
 */
public class SQLAccountDAO implements AccountDAO {

	private static final String INSERT_ACCOUNT_SQL = "INSERT INTO ACCOUNTS (NUMBER_ACCOUNT, CURRENCY, OWNER) VALUES(?, ?, ?)";
	private static final String SELECT_ACCOUNT_SQL = "SELECT * FROM ACCOUNTS WHERE NUMBER_ACCOUNT=? ";
	private static final String SELECT_LAST_ACCOUNT_NUMBER_SQL = "SELECT MAX(NUMBER_ACCOUNT) AS last_account FROM ACCOUNTS";
	private static final String INCREASE_BALANCE_SQL = "UPDATE ACCOUNTS SET BALANCE=BALANCE + ? WHERE NUMBER_ACCOUNT=?";
	private static final String DECREASE_BALANCE_SQL = "UPDATE ACCOUNTS SET BALANCE=BALANCE - ? WHERE NUMBER_ACCOUNT=?";
	private static final String COLUMN_BALANCE = "balance";
	private static final String COLUMN_CURRENCY = "currency";
	private static final String COLUMN_OWNER = "owner";
	private static final String COLUMN_LAST_ACCOUNT = "last_account";

	/**
	 * Instance of {@link ConnectionPool}
	 */
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	/**
	 * Create account
	 * 
	 * @param account {@link Account} all data to create
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public void create(Account account) throws DAOException {
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT_SQL);) {

			statement.setString(1, account.getNumberAccount());
			statement.setString(2, String.valueOf(account.getCurrency()));
			statement.setInt(3, account.getOwnerId());

			statement.executeUpdate();

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}

	/**
	 * Get account by account number
	 * 
	 * @param numberAccount {@link String} account number
	 * @return {@link Optional} of {@link Account} received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public Optional<Account> getAccount(String numberAccount) throws DAOException {

		Optional<Account> accountOptional = Optional.empty();
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_SQL);) {

			statement.setString(1, numberAccount);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				BigDecimal balance = resultSet.getBigDecimal(COLUMN_BALANCE);
				String currencyAccount = resultSet.getString(COLUMN_CURRENCY);
				Currency currency = Currency.valueOf(currencyAccount.toUpperCase());
				int owner = resultSet.getInt(COLUMN_OWNER);
				Account account = new Account(numberAccount, balance, currency, owner);
				accountOptional = Optional.of(account);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
		return accountOptional;
	}

	/**
	 * Get last account number
	 * 
	 * @return {@link Optional} of {@link Long} last account number
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public Optional<Long> getLastAccountNumber() throws DAOException {

		Optional<Long> lastAccountOptional = Optional.empty();
		try (Connection connection = connectionPool.takeConnection();
				Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(SELECT_LAST_ACCOUNT_NUMBER_SQL);

			if (resultSet.next()) {
				Long lastAccountNumber = resultSet.getLong(COLUMN_LAST_ACCOUNT);
				lastAccountOptional = Optional.of(lastAccountNumber);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
		return lastAccountOptional;
	}

	/**
	 * Update balance
	 * 
	 * @param transaction {@link Transaction} data to update
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public boolean updateBalance(Transaction transaction) throws DAOException {

		String operation;
		if (transaction.getTypeTransaction() == TransactionType.RECEIPT) {
			operation = INCREASE_BALANCE_SQL;
		} else if (transaction.getTypeTransaction() == TransactionType.EXPENDITURE) {
			operation = DECREASE_BALANCE_SQL;
		} else {
			throw new DAOException("not data for update");
		}

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(operation)) {

			statement.setString(1, transaction.getAmount());
			statement.setString(2, transaction.getTransactionAccount());

			return statement.executeUpdate() != 0;

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}

	/**
	 * Do transfer
	 * 
	 * @param transfer {@link Transfer} details
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public boolean doTransfer(Transfer transfer) throws DAOException {
		boolean result = false;
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement descreaseStatement = connection.prepareStatement(DECREASE_BALANCE_SQL);
				PreparedStatement increaseStatement = connection.prepareStatement(INCREASE_BALANCE_SQL)) {
			connection.setAutoCommit(false);

			descreaseStatement.setString(1, transfer.getAmount());
			descreaseStatement.setString(2, transfer.getSenderAccountNumber());

			if (descreaseStatement.executeUpdate() != 0) {
				increaseStatement.setString(1, transfer.getAmount());
				increaseStatement.setString(2, transfer.getRecipientAccountNumber());
				result = increaseStatement.executeUpdate() != 0;
			}

			if (result) {
				connection.commit();
			} else {
				connection.rollback();
			}
			connection.setAutoCommit(true);

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
		return result;
	}

}
