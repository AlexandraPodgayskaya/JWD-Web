package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.TransactionLogDAO;
import by.epam.payment_system.dao.TypeTransactionDAO;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Currency;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.entity.TransactionType;

public class SQLTransactionLogDAO implements TransactionLogDAO {

	private static final Logger logger = LogManager.getLogger();

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

	@Override
	public void addTransaction(Transaction transaction) throws DAOException {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;

		DAOFactory factory = DAOFactory.getInstance();
		TypeTransactionDAO typeTransactionDAO = factory.getTypeTransactionDAO();

		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(INSERT_TRANSACTION_SQL);

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
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			try {
				connectionPool.closeConnection(connection, statement);
			} catch (ConnectionPoolException e) {
				logger.error(e.getMessage());
				throw new DAOException(e);
			}
		}
	}

	@Override
	public List<Transaction> findCardTransactions(String numberCard) throws DAOException {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		DAOFactory factory = DAOFactory.getInstance();
		TypeTransactionDAO typeTransactionDAO = factory.getTypeTransactionDAO();

		List<Transaction> transactionList = new ArrayList<Transaction>();
		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(SELECT_CARD_TRANSACTIONS_SQL);

			statement.setString(1, numberCard);
			resultSet = statement.executeQuery();

			String transactionAccount;
			int typeTransactionId;
			TransactionType transactionType;
			String amount;
			String currencyAccount;
			Currency currency;
			Timestamp dataTime;
			String bankCode;
			String senderOrRecipientAccount;
			String ynp;
			String name;
			String purposePayment;

			Transaction transaction;

			while (resultSet.next()) {
				transactionAccount = resultSet.getString(COLUMN_ACCOUNT);
				typeTransactionId = resultSet.getInt(COLUMN_TYPE_TRANSACTION_ID);
				transactionType = typeTransactionDAO.findType(typeTransactionId);
				amount = resultSet.getString(COLUMN_SUM);
				currencyAccount = resultSet.getString(COLUMN_CURRENCY);
				currency = Currency.valueOf(currencyAccount.toUpperCase());
				dataTime = resultSet.getTimestamp(COLUMN_DATA_TIME);
				bankCode = resultSet.getString(COLUMN_BANK_CODE);
				senderOrRecipientAccount = resultSet.getString(COLUMN_SENDER_OR_RECIPIENT_ACCOUNT);
				ynp = resultSet.getString(COLUMN_YNP);
				name = resultSet.getString(COLUMN_NAME);
				purposePayment = resultSet.getString(COLUMN_PURPOSE_OF_PAYMENT);
				transaction = new Transaction(transactionAccount, numberCard, transactionType, amount, currency,
						dataTime, bankCode, senderOrRecipientAccount, ynp, name, purposePayment);
				transactionList.add(transaction);
			}

		} catch (ConnectionPoolException | SQLException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			try {
				connectionPool.closeConnection(connection, statement);
			} catch (ConnectionPoolException e) {
				logger.error(e.getMessage());
				throw new DAOException(e);
			}
		}
		return transactionList;
	}

	@Override
	public List<Transaction> findAccountTransactions(String numberAccount) throws DAOException {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		DAOFactory factory = DAOFactory.getInstance();
		TypeTransactionDAO typeTransactionDAO = factory.getTypeTransactionDAO();

		List<Transaction> transactionList = new ArrayList<Transaction>();
		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(SELECT_ACCOUNT_TRANSACTIONS_SQL);

			statement.setString(1, numberAccount);
			resultSet = statement.executeQuery();

			String numberCard;
			int typeTransactionId;
			TransactionType transactionType;
			String amount;
			String currencyAccount;
			Currency currency;
			Timestamp dataTime;
			String bankCode;
			String senderOrRecipientAccount;
			String ynp;
			String name;
			String purposePayment;

			Transaction transaction;

			while (resultSet.next()) {
				numberCard = resultSet.getString(COLUMN_NUMBER_CARD);
				typeTransactionId = resultSet.getInt(COLUMN_TYPE_TRANSACTION_ID);
				transactionType = typeTransactionDAO.findType(typeTransactionId);
				amount = resultSet.getString(COLUMN_SUM);
				currencyAccount = resultSet.getString(COLUMN_CURRENCY);
				currency = Currency.valueOf(currencyAccount.toUpperCase());
				dataTime = resultSet.getTimestamp(COLUMN_DATA_TIME);
				bankCode = resultSet.getString(COLUMN_BANK_CODE);
				senderOrRecipientAccount = resultSet.getString(COLUMN_SENDER_OR_RECIPIENT_ACCOUNT);
				ynp = resultSet.getString(COLUMN_YNP);
				name = resultSet.getString(COLUMN_NAME);
				purposePayment = resultSet.getString(COLUMN_PURPOSE_OF_PAYMENT);
				transaction = new Transaction(numberAccount, numberCard, transactionType, amount, currency, dataTime,
						bankCode, senderOrRecipientAccount, ynp, name, purposePayment);
				transactionList.add(transaction);
			}

		} catch (ConnectionPoolException | SQLException e) {
			logger.error(e.getMessage());
			throw new DAOException(e);
		} finally {
			try {
				connectionPool.closeConnection(connection, statement);
			} catch (ConnectionPoolException e) {
				logger.error(e.getMessage());
				throw new DAOException(e);
			}
		}
		return transactionList;
	}

}
