package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.dao.AdditionalClientDataDAO;
import by.epam.payment_system.entity.UserInfo;

public class SQLAdditionalClientDataDAO implements AdditionalClientDataDAO {
	
	private static final Logger logger = LogManager.getLogger();
	
	private static final String INSERT_ADDITIONAL_CLIENT_DATA_SQL = "INSERT INTO CLIENT_DETAILS (USER_ID, SURNAME, NAME, PATRONYMIC, DATE_OF_BIRTH, PERSONAL_NUMBER_PASSPORT, PHONE) VALUES(?, ?, ?, ?, ?, ?, ?) ";
	private static final String SELECT_CLIENT_DATA_SQL = "SELECT * FROM CLIENT_DETAILS WHERE PERSONAL_NUMBER_PASSPORT=? ";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_SURNAME = "surname";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PATRONYMIC = "patronymic";
	private static final String COLUMN_DATE_BIRTH = "date_of_birth";
	private static final String COLUMN_PHONE = "phone";
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
	
	//@Nullable
	public UserInfo findData (String personalNumberPassport) throws DAOException {
		
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		UserInfo userInfo = null;
		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(SELECT_CLIENT_DATA_SQL);
			statement.setString(1, personalNumberPassport);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				userInfo = new UserInfo();
				userInfo.setId(resultSet.getInt(COLUMN_USER_ID));
				userInfo.setSurname(resultSet.getString(COLUMN_SURNAME));
				userInfo.setName(resultSet.getString(COLUMN_NAME));
				userInfo.setPatronymic(resultSet.getString(COLUMN_PATRONYMIC));
				userInfo.setDateBirth(resultSet.getString(COLUMN_DATE_BIRTH));
				userInfo.setPersonalNumberPassport(personalNumberPassport);
				userInfo.setPhone(resultSet.getString(COLUMN_PHONE));
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
		return userInfo;
		
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
