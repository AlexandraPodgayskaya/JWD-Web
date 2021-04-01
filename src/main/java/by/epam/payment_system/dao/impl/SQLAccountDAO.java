package by.epam.payment_system.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import by.epam.payment_system.dao.AccountDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Account;
import by.epam.payment_system.entity.Currency;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.entity.TransactionType;

public class SQLAccountDAO implements AccountDAO {

	private static final String SELECT_ACCOUNT_SQL = "SELECT * FROM ACCOUNTS JOIN CURRENCIES ON ACCOUNTS.CURRENCY_ID=CURRENCIES.ID WHERE NUMBER_ACCOUNT=? ";
	private static final String INCREASE_BALANCE_SQL = "UPDATE ACCOUNTS SET BALANCE=BALANCE + ? WHERE NUMBER_ACCOUNT=?";
	private static final String DECREASE_BALANCE_SQL = "UPDATE ACCOUNTS SET BALANCE=BALANCE - ? WHERE NUMBER_ACCOUNT=?";
	private static final String COLUMN_BALANCE = "balance";
	private static final String COLUMN_CURRENCY = "currency";
	private static final String COLUMN_CURRENCY_ID = "currency_id";
	private static final String COLUMN_OWNER = "owner";

	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	// @Nullable
	@Override
	public Account getAccount(String numberAccount) throws DAOException {

		Account account = null;
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_SQL);) {

			statement.setString(1, numberAccount);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				BigDecimal balance = resultSet.getBigDecimal(COLUMN_BALANCE);
				int currencyId = resultSet.getInt(COLUMN_CURRENCY_ID);
				String currencyAccount = resultSet.getString(COLUMN_CURRENCY);
				Currency currency = Currency.valueOf(currencyAccount.toUpperCase());
				int owner = resultSet.getInt(COLUMN_OWNER);
				account = new Account(numberAccount, balance, currencyId, currency, owner);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
		return account;
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
}
