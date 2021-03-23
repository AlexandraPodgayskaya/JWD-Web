package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.TransactionLogDAO;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Transaction;

public class SQLTransactionLogDAO implements TransactionLogDAO {

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
			statement.setInt(3, transaction.getTypeTransactionId());
			statement.setString(4, transaction.getAmount());
			statement.setInt(5, transaction.getCurrencyId());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			statement.setTimestamp(6, timestamp);
			statement.setString(7, transaction.getBankCode());
			statement.setString(8, transaction.getSenderOrRecipientAccount());
			statement.setString(9, transaction.getYnp());
			statement.setString(10, transaction.getName());
			statement.setString(11, transaction.getPurposePayment());
			statement.executeUpdate();

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		} finally {
			try {
				connectionPool.closeConnection(connection, statement);
			} catch (ConnectionPoolException e) {
				throw new DAOException(e);
			}
		}
	}

	public static void main(String[] args) throws DAOException {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connectionPool.init();
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(INSERT_TRANSACTION_SQL);

			statement.setString(1, "21081001");
			statement.setString(2, "5489333344441111");
			statement.setInt(3, 1);
			statement.setString(4, "15.00");
			statement.setInt(5, 1);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			statement.setTimestamp(6, timestamp);
			statement.setString(7, "code");
			statement.setString(8, "321");
			statement.setString(9, "115125364");
			statement.setString(10, "Campany");
			statement.setString(11, "top up card");
			statement.executeUpdate();

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		} finally {
			try {
				connectionPool.closeConnection(connection, statement);
				connectionPool.destroy();
			} catch (ConnectionPoolException e) {
				throw new DAOException(e);
			}
		}
	}
}
