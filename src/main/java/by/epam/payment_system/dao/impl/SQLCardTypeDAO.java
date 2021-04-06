package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import by.epam.payment_system.dao.CardTypeDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;

public class SQLCardTypeDAO implements CardTypeDAO {

	private static final String INSERT_CARD_TYPE_SQL = "INSERT INTO CARD_TYPES (TYPE, IMAGE_PATH) VALUES(?, ?) ";

	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	@Override
	public void create(String cardType, String imagePath) throws DAOException {
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_CARD_TYPE_SQL)) {

			statement.setString(1, cardType);
			statement.setString(2, imagePath);

			statement.executeUpdate();

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}

}
