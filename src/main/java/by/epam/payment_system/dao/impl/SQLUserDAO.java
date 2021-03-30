package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.UserDAO;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.entity.UserType;

public class SQLUserDAO implements UserDAO {
	
	private static final Logger logger = LogManager.getLogger();

	private static final String SELECT_USER_SQL = "SELECT USERS.ID, TYPE FROM USERS JOIN USER_TYPES ON USERS.TYPE_ID=USER_TYPES.ID WHERE LOGIN=? AND PASSWORD=? ";
	private static final String SELECT_ID_SQL = "SELECT ID FROM USERS WHERE LOGIN=? ";
	private static final String INSERT_USER_SQL = "INSERT INTO USERS (LOGIN, PASSWORD, TYPE_ID) VALUES(?, ?, ?) ";
	private static final String COLUMN_USER_ID = "id";
	private static final String COLUMN_USER_TYPE = "type";

	@Override
	public void create(UserInfo registrationInfo) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(INSERT_USER_SQL);

			statement.setString(1, registrationInfo.getLogin());
			statement.setString(2, registrationInfo.getPassword());
			statement.setInt(3, registrationInfo.getTypeUserId());
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
	public void update(UserInfo entity) throws DAOException {
		throw new DAOUnsupportedOperationException();
	}

	@Override
	public void delete(String key) throws DAOException {
		throw new DAOUnsupportedOperationException();
	}

	// @Nullable
	@Override
	public User find(UserInfo loginationInfo) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		User user = null;
		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(SELECT_USER_SQL);
			statement.setString(1, loginationInfo.getLogin());
			statement.setString(2, loginationInfo.getPassword());
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				int id = resultSet.getInt(COLUMN_USER_ID);
				String type = resultSet.getString(COLUMN_USER_TYPE);
				UserType userType = UserType.valueOf(type.toUpperCase());
				user = new User(id, loginationInfo.getLogin(), loginationInfo.getPassword(), userType);
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
		return user;
	}

	// @Nullable
	@Override
	public Integer findId(String login) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		Integer id = null;
		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(SELECT_ID_SQL);

			statement.setString(1, login);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				id = resultSet.getInt(COLUMN_USER_ID);
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
