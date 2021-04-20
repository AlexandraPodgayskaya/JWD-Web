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

	private static final String INSERT_ACCOUNT_SQL = "INSERT INTO ACCOUNTS (CURRENCY, OWNER) VALUES(?, ?)";
	private static final String SELECT_ACCOUNT_SQL = "SELECT * FROM ACCOUNTS WHERE NUMBER_ACCOUNT=? ";
	private static final String INCREASE_BALANCE_SQL = "UPDATE ACCOUNTS SET BALANCE=BALANCE + ? WHERE NUMBER_ACCOUNT=?";
	private static final String DECREASE_BALANCE_SQL = "UPDATE ACCOUNTS SET BALANCE=BALANCE - ? WHERE NUMBER_ACCOUNT=?";
	private static final String COLUMN_BALANCE = "balance";
	private static final String COLUMN_CURRENCY = "currency";
	private static final String COLUMN_OWNER = "owner";

	/**
	 * Instance of {@link ConnectionPool}
	 */
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	/**
	 * Create account
	 * 
	 * @param account {@link Account} all data to create
	 * @return {@link String} new account number
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public String create(Account account) throws DAOException {
		String numberAccount;
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT_SQL,
						Statement.RETURN_GENERATED_KEYS)) {

			statement.setString(1, String.valueOf(account.getCurrency()));
			statement.setLong(2, account.getOwnerId());
			statement.executeUpdate();

			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				numberAccount = resultSet.getString(1);
			} else {
				throw new DAOException("database error");
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
		return numberAccount;
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
				long owner = resultSet.getLong(COLUMN_OWNER);
				Account account = new Account(numberAccount, balance, currency, owner);
				accountOptional = Optional.of(account);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
		return accountOptional;
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
