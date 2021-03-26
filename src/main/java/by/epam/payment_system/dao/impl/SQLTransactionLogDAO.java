package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.TransactionLogDAO;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Transaction;

public class SQLTransactionLogDAO implements TransactionLogDAO {

	public static final Logger logger = LogManager.getLogger();

	private static final String INSERT_TRANSACTION_SQL = "INSERT INTO TRANSACTION_LOG (ACCOUNT, NUMBER_CARD, TYPE_TRANSACTION_ID, SUM, CURRENCY_ID, DATA_AND_TIME, BANK_CODE, SENDER_OR_RECIPIENT_ACCOUNT, YNP, NAME, PURPOSE_OF_PAYMENT) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

	@Override
	public void addTransaction(Transaction transaction) throws DAOException {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(INSERT_TRANSACTION_SQL);

			statement.setString(1, transaction.getTransactionAccount());
			statement.setString(2, transaction.getNumberCard());
			statement.setInt(3, transaction.getTypeTransaction().getId());
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

}
