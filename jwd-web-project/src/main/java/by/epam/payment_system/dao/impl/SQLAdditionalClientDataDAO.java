package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.dao.AdditionalClientDataDAO;
import by.epam.payment_system.entity.UserInfo;

/**
 * Works with database table client_details
 * 
 * @author Aleksandra Podgayskaya
 * @see AdditionalClientDataDAO
 */
public class SQLAdditionalClientDataDAO implements AdditionalClientDataDAO {

	private static final String INSERT_ADDITIONAL_CLIENT_DATA_SQL = "INSERT INTO CLIENT_DETAILS (USER_ID, SURNAME, NAME, PATRONYMIC, DATE_OF_BIRTH, PERSONAL_NUMBER_PASSPORT, PHONE) VALUES(?, ?, ?, ?, ?, ?, ?) ";
	private static final String SELECT_CLIENT_DATA_BY_PASSPORT_SQL = "SELECT * FROM CLIENT_DETAILS WHERE PERSONAL_NUMBER_PASSPORT=? ";
	private static final String SELECT_CLIENT_DATA_BY_USER_ID_SQL = "SELECT * FROM CLIENT_DETAILS WHERE USER_ID=? ";
	private static final String UPDATE_ADDITIONAL_CLIENT_DATA_SQL = "UPDATE CLIENT_DETAILS SET SURNAME=?, NAME=?, PATRONYMIC=?, DATE_OF_BIRTH=?, PERSONAL_NUMBER_PASSPORT=?, PHONE=? WHERE USER_ID=? ";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_SURNAME = "surname";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PATRONYMIC = "patronymic";
	private static final String COLUMN_DATE_BIRTH = "date_of_birth";
	private static final String COLUMN_PERSONAL_NUMBER_PASSPORT = "personal_number_passport";
	private static final String COLUMN_PHONE = "phone";

	/**
	 * Instance of {@link ConnectionPool}
	 */
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	/**
	 * Create client details
	 * 
	 * @param additionalClientInfo {@link UserInfo} all data to create
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public void create(UserInfo additionalClientInfo) throws DAOException {

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_ADDITIONAL_CLIENT_DATA_SQL)) {

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
		}
	}

	/**
	 * Find client details by personal number passport
	 * 
	 * @param personalNumberPassport {@link String} personal number passport to
	 *                               search
	 * @return {@link Optional} of {@link UserInfo} received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	public Optional<UserInfo> findDataByPassport(String personalNumberPassport) throws DAOException {

		Optional<UserInfo> userInfoOptional = Optional.empty();

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_CLIENT_DATA_BY_PASSPORT_SQL)) {

			statement.setString(1, personalNumberPassport);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				UserInfo userInfo = new UserInfo();
				userInfo.setId(resultSet.getInt(COLUMN_USER_ID));
				userInfo.setSurname(resultSet.getString(COLUMN_SURNAME));
				userInfo.setName(resultSet.getString(COLUMN_NAME));
				userInfo.setPatronymic(resultSet.getString(COLUMN_PATRONYMIC));
				userInfo.setDateBirth(resultSet.getString(COLUMN_DATE_BIRTH));
				userInfo.setPersonalNumberPassport(personalNumberPassport);
				userInfo.setPhone(resultSet.getString(COLUMN_PHONE));

				userInfoOptional = Optional.of(userInfo);
			}
		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}

		return userInfoOptional;

	}

	/**
	 * Find client details by user id
	 * 
	 * @param userId {@link Integer} personal user id to search
	 * @return {@link Optional} of {@link UserInfo} received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public Optional<UserInfo> findDataById(Integer userId) throws DAOException {
		Optional<UserInfo> userInfoOptional = Optional.empty();

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_CLIENT_DATA_BY_USER_ID_SQL)) {

			statement.setInt(1, userId);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				UserInfo userInfo = new UserInfo();
				userInfo.setId(userId);
				userInfo.setSurname(resultSet.getString(COLUMN_SURNAME));
				userInfo.setName(resultSet.getString(COLUMN_NAME));
				userInfo.setPatronymic(resultSet.getString(COLUMN_PATRONYMIC));
				userInfo.setDateBirth(resultSet.getString(COLUMN_DATE_BIRTH));
				userInfo.setPersonalNumberPassport(resultSet.getString(COLUMN_PERSONAL_NUMBER_PASSPORT));
				userInfo.setPhone(resultSet.getString(COLUMN_PHONE));

				userInfoOptional = Optional.of(userInfo);
			}
		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}

		return userInfoOptional;
	}

	/**
	 * Update client details
	 * 
	 * @param userInfo {@link UserInfo} data to update
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public boolean update(UserInfo userInfo) throws DAOException {
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_ADDITIONAL_CLIENT_DATA_SQL)) {

			statement.setString(1, userInfo.getSurname());
			statement.setString(2, userInfo.getName());
			statement.setString(3, userInfo.getPatronymic());
			statement.setString(4, userInfo.getDateBirth());
			statement.setString(5, userInfo.getPersonalNumberPassport());
			statement.setString(6, userInfo.getPhone());
			statement.setInt(7, userInfo.getId());

			return statement.executeUpdate() != 0;

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}
}
