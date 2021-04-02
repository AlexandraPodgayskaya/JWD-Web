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

public class SQLUserDAO implements UserDAO {

	private static final String SELECT_USER_SQL = "SELECT USERS.ID, TYPE FROM USERS JOIN USER_TYPES ON USERS.TYPE_ID=USER_TYPES.ID WHERE LOGIN=? AND PASSWORD=? ";
	private static final String SELECT_ID_SQL = "SELECT ID FROM USERS WHERE LOGIN=? ";
	private static final String INSERT_USER_SQL = "INSERT INTO USERS (LOGIN, PASSWORD, TYPE_ID) VALUES(?, ?, ?) ";
	private static final String COLUMN_USER_ID = "id";
	private static final String COLUMN_USER_TYPE = "type";

	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	@Override
	public void create(UserInfo registrationInfo) throws DAOException {

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_USER_SQL)) {

			statement.setString(1, registrationInfo.getLogin());
			statement.setString(2, registrationInfo.getPassword());
			statement.setInt(3, registrationInfo.getTypeUserId());
			statement.executeUpdate();

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}

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
}
