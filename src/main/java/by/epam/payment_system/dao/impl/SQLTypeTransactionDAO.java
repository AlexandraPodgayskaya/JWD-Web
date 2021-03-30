package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.TypeTransactionDAO;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.TransactionType;

public class SQLTypeTransactionDAO implements TypeTransactionDAO {
	
	private static final Logger logger = LogManager.getLogger();
	
	private static final String SELECT_TYPE_SQL = "SELECT * FROM TRANSACTION_TYPES WHERE ID=? ";
	private static final String SELECT_ID_SQL = "SELECT * FROM TRANSACTION_TYPES WHERE TYPE=? ";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_ID = "id";

	//@Nullable
	@Override
	public TransactionType findType(Integer id) throws DAOException {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		TransactionType transactionType = null;
		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(SELECT_TYPE_SQL);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String type = resultSet.getString(COLUMN_TYPE);
				transactionType = TransactionType.valueOf(type.toUpperCase());
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
		return transactionType;
	}

	//@Nullable
	@Override
	public Integer findId(TransactionType type) throws DAOException {
		
		if (type == null) {
			throw new DAOException("null transaction type value to find");
		}
		
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		Integer id = null;
		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(SELECT_ID_SQL);
			statement.setString(1, String.valueOf(type).toLowerCase());
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				id = resultSet.getInt(COLUMN_ID);
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
		return id;
	}

}
