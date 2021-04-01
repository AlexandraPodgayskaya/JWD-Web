package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.TypeTransactionDAO;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.TransactionType;

public class SQLTypeTransactionDAO implements TypeTransactionDAO {

	private static final String SELECT_TYPE_SQL = "SELECT * FROM TRANSACTION_TYPES WHERE ID=? ";
	private static final String SELECT_ID_SQL = "SELECT * FROM TRANSACTION_TYPES WHERE TYPE=? ";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_ID = "id";

	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	// @Nullable
	@Override
	public TransactionType findType(int id) throws DAOException {

		TransactionType transactionType = null;

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_TYPE_SQL);) {

			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String type = resultSet.getString(COLUMN_TYPE);
				transactionType = TransactionType.valueOf(type.toUpperCase());
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
		return transactionType;
	}

	// @Nullable
	@Override
	public Integer findId(TransactionType type) throws DAOException {

		Integer id = null;
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ID_SQL)) {
			
			statement.setString(1, String.valueOf(type).toLowerCase());
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				id = resultSet.getInt(COLUMN_ID);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
		return id;
	}
}
