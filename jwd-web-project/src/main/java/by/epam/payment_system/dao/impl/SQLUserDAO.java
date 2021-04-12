package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.UserDAO;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.entity.UserType;

/**
 * Works with database table users
 * 
 * @author Aleksandra Podgayskaya
 * @see UserDAO
 */
public class SQLUserDAO implements UserDAO {

	private static final String SELECT_USER_SQL = "SELECT * FROM USERS WHERE LOGIN=? AND PASSWORD=? ";
	private static final String SELECT_ID_SQL = "SELECT ID FROM USERS WHERE LOGIN=? ";
	private static final String INSERT_USER_SQL = "INSERT INTO USERS (LOGIN, PASSWORD, TYPE) VALUES(?, ?, ?) ";
	private static final String UPDATE_LOGIN_SQL = "UPDATE USERS SET LOGIN=?, PASSWORD=? WHERE ID=? ";
	private static final String COLUMN_USER_ID = "id";
	private static final String COLUMN_USER_TYPE = "type";

	/**
	 * Instance of {@link ConnectionPool}
	 */
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	/**
	 * Create new user
	 * 
	 * @param registrationInfo {@link UserInfo} all data to create
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public void create(UserInfo registrationInfo) throws DAOException {

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_USER_SQL)) {

			statement.setString(1, registrationInfo.getLogin());
			statement.setString(2, registrationInfo.getPassword());
			statement.setString(3, String.valueOf(registrationInfo.getUserType()));
			statement.executeUpdate();

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}

	/**
	 * Find user id
	 * 
	 * @param login {@link String} login to search
	 * @return {@link Optional} of {@link Integer} user id received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public Optional<Integer> findId(String login) throws DAOException {

		Optional<Integer> idOptional = Optional.empty();

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ID_SQL)) {

			statement.setString(1, login);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				Integer id = resultSet.getInt(COLUMN_USER_ID);
				idOptional = Optional.of(id);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}

		return idOptional;
	}

	/**
	 * Find user by login and password
	 * 
	 * @param loginationInfo {@link UserInfo} credentials
	 * @return {@link Optional} of {@link User} received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public Optional<User> find(UserInfo loginationInfo) throws DAOException {

		Optional<User> userOptional = Optional.empty();

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_USER_SQL);) {

			statement.setString(1, loginationInfo.getLogin());
			statement.setString(2, loginationInfo.getPassword());
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				int id = resultSet.getInt(COLUMN_USER_ID);
				String type = resultSet.getString(COLUMN_USER_TYPE);
				UserType userType = UserType.valueOf(type.toUpperCase());
				User user = new User(id, loginationInfo.getLogin(), loginationInfo.getPassword(), userType);
				userOptional = Optional.of(user);
			}
		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
		return userOptional;
	}

	/**
	 * Update user data
	 * 
	 * @param userInfo {@link UserInfo} data to update
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public boolean updateUser(UserInfo userInfo) throws DAOException {
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_LOGIN_SQL);) {

			statement.setString(1, userInfo.getLogin());
			statement.setString(2, userInfo.getPassword());
			statement.setInt(3, userInfo.getId());

			return statement.executeUpdate() != 0;

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}
}
