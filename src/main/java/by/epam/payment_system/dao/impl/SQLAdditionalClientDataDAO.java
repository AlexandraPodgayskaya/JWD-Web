package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.dao.AdditionalClientDataDAO;
import by.epam.payment_system.entity.UserInfo;

public class SQLAdditionalClientDataDAO implements AdditionalClientDataDAO {
	private static final String INSERT_ADDITIONAL_CLIENT_DATA_SQL = "INSERT INTO CLIENT_DETAILS (USER_ID, SURNAME, NAME, PATRONYMIC, DATE_OF_BIRTH, PERSONAL_NUMBER_PASSPORT, PHONE) VALUES(?, ?, ?, ?, ?, ?, ?) ";

	@Override
	public void create(UserInfo additionalClientInfo) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(INSERT_ADDITIONAL_CLIENT_DATA_SQL);

			statement.setInt(1, additionalClientInfo.getId());
			statement.setString(2, additionalClientInfo.getSurname());
			statement.setString(3, additionalClientInfo.getName());
			statement.setString(4, additionalClientInfo.getPatronymic());
			statement.setString(5, additionalClientInfo.getDateBirth());
			statement.setString(6, additionalClientInfo.getPersonalNumberPassport());
			statement.setString(7, additionalClientInfo.getPhone());
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

	@Override
	public void update(UserInfo entity) throws DAOException {
		throw new DAOUnsupportedOperationException();

	}

	@Override
	public void delete(String key) throws DAOException {
		throw new DAOUnsupportedOperationException();

	}

	@Override
	public Integer find(UserInfo entity) throws DAOException {
		throw new DAOUnsupportedOperationException();
	}

	@Override
	public Integer findId(String key) throws DAOException {
		throw new DAOUnsupportedOperationException();
	}

}
