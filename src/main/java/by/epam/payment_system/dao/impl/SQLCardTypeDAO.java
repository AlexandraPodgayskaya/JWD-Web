package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import by.epam.payment_system.dao.CardTypeDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.CardType;

public class SQLCardTypeDAO implements CardTypeDAO {

	private static final String SELECT_CARD_TYPES_SQL = "SELECT * FROM CARD_TYPES";
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_IMAGE_PATH = "image_path";

	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	@Override
	public List<CardType> findAll() throws DAOException {

		List<CardType> cardTypeList = new ArrayList<CardType>();

		try (Connection connection = connectionPool.takeConnection();
				Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(SELECT_CARD_TYPES_SQL);

			Integer id;
			String type;
			String imagePath;
			CardType cardType;

			while (resultSet.next()) {
				id = resultSet.getInt(COLUMN_ID);
				type = resultSet.getString(COLUMN_TYPE);
				imagePath = resultSet.getString(COLUMN_IMAGE_PATH);
				cardType = new CardType(id, type, imagePath);
				cardTypeList.add(cardType);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}

		return cardTypeList;
	}

}
