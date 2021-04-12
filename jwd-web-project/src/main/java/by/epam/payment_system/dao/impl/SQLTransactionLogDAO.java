package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.TransactionLogDAO;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.Currency;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.entity.TransactionType;

/**
 * Works with database table transaction_log
 * 
 * @author Aleksandra Podgayskaya
 * @see TransactionLogDAO
 */
public class SQLTransactionLogDAO implements TransactionLogDAO {

	private static final String INSERT_TRANSACTION_SQL = "INSERT INTO TRANSACTION_LOG (ACCOUNT, NUMBER_CARD, TYPE_TRANSACTION, SUM, CURRENCY, DATA_AND_TIME, BANK_CODE, SENDER_OR_RECIPIENT_ACCOUNT, YNP, NAME, PURPOSE_OF_PAYMENT) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	private static final String SELECT_CARD_TRANSACTIONS_SQL = "SELECT * FROM TRANSACTION_LOG WHERE NUMBER_CARD=? ";
	private static final String SELECT_ACCOUNT_TRANSACTIONS_SQL = "SELECT * FROM TRANSACTION_LOG WHERE ACCOUNT=? ";
	private static final String COLUMN_ACCOUNT = "account";
	private static final String COLUMN_NUMBER_CARD = "number_card";
	private static final String COLUMN_TYPE_TRANSACTION = "type_transaction";
	private static final String COLUMN_SUM = "sum";
	private static final String COLUMN_CURRENCY = "currency";
	private static final String COLUMN_DATA_TIME = "data_and_time";
	private static final String COLUMN_BANK_CODE = "bank_code";
	private static final String COLUMN_SENDER_OR_RECIPIENT_ACCOUNT = "sender_or_recipient_account";
	private static final String COLUMN_YNP = "ynp";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PURPOSE_OF_PAYMENT = "purpose_of_payment";

	/**
	 * Instance of {@link ConnectionPool}
	 */
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	/**
	 * Add transaction
	 * 
	 * @param transaction {@link Transaction} all data to add
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public void addTransaction(Transaction transaction) throws DAOException {

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_TRANSACTION_SQL);) {

			statement.setString(1, transaction.getTransactionAccount());
			statement.setString(2, transaction.getNumberCard());
			statement.setString(3, String.valueOf(transaction.getTypeTransaction()));
			statement.setString(4, transaction.getAmount());
			statement.setString(5, String.valueOf(transaction.getCurrency()));
			statement.setTimestamp(6, transaction.getDateTime());
			statement.setString(7, transaction.getBankCode());
			statement.setString(8, transaction.getSenderOrRecipientAccount());
			statement.setString(9, transaction.getYnp());
			statement.setString(10, transaction.getName());
			statement.setString(11, transaction.getPurposePayment());
			statement.executeUpdate();

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}

	/**
	 * Find all card transactions by card number
	 * 
	 * @param numberCard {@link String} card number to search
	 * @return {@link List} of {@link Card} received from database if transactions
	 *         are found, else emptyList
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public List<Transaction> findCardTransactions(String numberCard) throws DAOException {

		List<Transaction> transactionList = new ArrayList<Transaction>();
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_CARD_TRANSACTIONS_SQL)) {

			statement.setString(1, numberCard);
			ResultSet resultSet = statement.executeQuery();

			Transaction transaction;
			String typeTransaction;
			String currency;

			while (resultSet.next()) {

				transaction = new Transaction();

				transaction.setTransactionAccount(resultSet.getString(COLUMN_ACCOUNT));
				transaction.setNumberCard(numberCard);
				typeTransaction = resultSet.getString(COLUMN_TYPE_TRANSACTION);
				transaction.setTypeTransaction(TransactionType.valueOf(typeTransaction.toUpperCase()));
				transaction.setAmount(resultSet.getString(COLUMN_SUM));
				currency = resultSet.getString(COLUMN_CURRENCY);
				transaction.setCurrency(Currency.valueOf(currency.toUpperCase()));
				transaction.setDateTime(resultSet.getTimestamp(COLUMN_DATA_TIME));
				transaction.setBankCode(resultSet.getString(COLUMN_BANK_CODE));
				transaction.setSenderOrRecipientAccount(resultSet.getString(COLUMN_SENDER_OR_RECIPIENT_ACCOUNT));
				transaction.setYnp(resultSet.getString(COLUMN_YNP));
				transaction.setName(resultSet.getString(COLUMN_NAME));
				transaction.setPurposePayment(resultSet.getString(COLUMN_PURPOSE_OF_PAYMENT));

				transactionList.add(transaction);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}

		return transactionList;
	}

	/**
	 * Find all account transactions by account number
	 * 
	 * @param numberAccount {@link String} account number to search
	 * @return {@link List} of {@link Card} received from database if transactions
	 *         are found, else emptyList
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public List<Transaction> findAccountTransactions(String numberAccount) throws DAOException {

		List<Transaction> transactionList = new ArrayList<Transaction>();

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_TRANSACTIONS_SQL)) {

			statement.setString(1, numberAccount);
			ResultSet resultSet = statement.executeQuery();

			Transaction transaction;
			String typeTransaction;
			String currency;

			while (resultSet.next()) {

				transaction = new Transaction();

				transaction.setTransactionAccount(numberAccount);
				transaction.setNumberCard(resultSet.getString(COLUMN_NUMBER_CARD));
				typeTransaction = resultSet.getString(COLUMN_TYPE_TRANSACTION);
				transaction.setTypeTransaction(TransactionType.valueOf(typeTransaction.toUpperCase()));
				transaction.setAmount(resultSet.getString(COLUMN_SUM));
				currency = resultSet.getString(COLUMN_CURRENCY);
				transaction.setCurrency(Currency.valueOf(currency.toUpperCase()));
				transaction.setDateTime(resultSet.getTimestamp(COLUMN_DATA_TIME));
				transaction.setBankCode(resultSet.getString(COLUMN_BANK_CODE));
				transaction.setSenderOrRecipientAccount(resultSet.getString(COLUMN_SENDER_OR_RECIPIENT_ACCOUNT));
				transaction.setYnp(resultSet.getString(COLUMN_YNP));
				transaction.setName(resultSet.getString(COLUMN_NAME));
				transaction.setPurposePayment(resultSet.getString(COLUMN_PURPOSE_OF_PAYMENT));

				transactionList.add(transaction);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
		return transactionList;
	}

}
