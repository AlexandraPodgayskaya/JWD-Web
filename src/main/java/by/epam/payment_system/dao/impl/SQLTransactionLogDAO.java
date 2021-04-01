package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.TransactionLogDAO;
import by.epam.payment_system.dao.TypeTransactionDAO;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Currency;
import by.epam.payment_system.entity.Transaction;

public class SQLTransactionLogDAO implements TransactionLogDAO {

	private static final String INSERT_TRANSACTION_SQL = "INSERT INTO TRANSACTION_LOG (ACCOUNT, NUMBER_CARD, TYPE_TRANSACTION_ID, SUM, CURRENCY_ID, DATA_AND_TIME, BANK_CODE, SENDER_OR_RECIPIENT_ACCOUNT, YNP, NAME, PURPOSE_OF_PAYMENT) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	private static final String SELECT_CARD_TRANSACTIONS_SQL = "SELECT * FROM TRANSACTION_LOG JOIN CURRENCIES ON TRANSACTION_LOG.CURRENCY_ID=CURRENCIES.ID WHERE NUMBER_CARD=? ";
	private static final String SELECT_ACCOUNT_TRANSACTIONS_SQL = "SELECT * FROM TRANSACTION_LOG JOIN CURRENCIES ON TRANSACTION_LOG.CURRENCY_ID=CURRENCIES.ID WHERE ACCOUNT=? ";
	private static final String COLUMN_ACCOUNT = "account";
	private static final String COLUMN_NUMBER_CARD = "number_card";
	private static final String COLUMN_TYPE_TRANSACTION_ID = "type_transaction_id";
	private static final String COLUMN_SUM = "sum";
	private static final String COLUMN_CURRENCY = "currency";
	private static final String COLUMN_DATA_TIME = "data_and_time";
	private static final String COLUMN_BANK_CODE = "bank_code";
	private static final String COLUMN_SENDER_OR_RECIPIENT_ACCOUNT = "sender_or_recipient_account";
	private static final String COLUMN_YNP = "ynp";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PURPOSE_OF_PAYMENT = "purpose_of_payment";

	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	@Override
	public void addTransaction(Transaction transaction) throws DAOException {

		DAOFactory factory = DAOFactory.getInstance();
		TypeTransactionDAO typeTransactionDAO = factory.getTypeTransactionDAO();

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_TRANSACTION_SQL);) {

			statement.setString(1, transaction.getTransactionAccount());
			statement.setString(2, transaction.getNumberCard());
			statement.setInt(3, typeTransactionDAO.findId(transaction.getTypeTransaction()));
			statement.setString(4, transaction.getAmount());
			statement.setInt(5, transaction.getCurrencyId());
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

	@Override
	public List<Transaction> findCardTransactions(String numberCard) throws DAOException {

		DAOFactory factory = DAOFactory.getInstance();
		TypeTransactionDAO typeTransactionDAO = factory.getTypeTransactionDAO();

		List<Transaction> transactionList = new ArrayList<Transaction>();
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_CARD_TRANSACTIONS_SQL)) {

			statement.setString(1, numberCard);
			ResultSet resultSet = statement.executeQuery();

			Transaction transaction;
			int typeTransactionId;
			String currency;

			while (resultSet.next()) {

				transaction = new Transaction();

				transaction.setTransactionAccount(resultSet.getString(COLUMN_ACCOUNT));
				transaction.setNumberCard(numberCard);
				typeTransactionId = resultSet.getInt(COLUMN_TYPE_TRANSACTION_ID);
				transaction.setTypeTransaction(typeTransactionDAO.findType(typeTransactionId));
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

	@Override
	public List<Transaction> findAccountTransactions(String numberAccount) throws DAOException {

		DAOFactory factory = DAOFactory.getInstance();
		TypeTransactionDAO typeTransactionDAO = factory.getTypeTransactionDAO();

		List<Transaction> transactionList = new ArrayList<Transaction>();

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_TRANSACTIONS_SQL)) {

			statement.setString(1, numberAccount);
			ResultSet resultSet = statement.executeQuery();

			Transaction transaction;
			int typeTransactionId;
			String currency;

			while (resultSet.next()) {
				
				transaction = new Transaction();

				transaction.setTransactionAccount(numberAccount);
				transaction.setNumberCard(resultSet.getString(COLUMN_NUMBER_CARD));
				typeTransactionId = resultSet.getInt(COLUMN_TYPE_TRANSACTION_ID);
				transaction.setTypeTransaction(typeTransactionDAO.findType(typeTransactionId));
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
