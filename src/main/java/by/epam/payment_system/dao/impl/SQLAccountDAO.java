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

	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

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

	@Override
	public boolean updateBalance(Transaction transaction) throws DAOException {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = connectionPool.takeConnection();

			if (transaction.getTypeTransaction() == TransactionType.RECEIPT) {
				statement = connection.prepareStatement(INCREASE_BALANCE_SQL);
			} else if (transaction.getTypeTransaction() == TransactionType.EXPENDITURE) {
				statement = connection.prepareStatement(DECREASE_BALANCE_SQL);
			}
			statement.setString(1, transaction.getAmount());
			statement.setString(2, transaction.getTransactionAccount());

			return statement.executeUpdate() != 0;

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}

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
}
