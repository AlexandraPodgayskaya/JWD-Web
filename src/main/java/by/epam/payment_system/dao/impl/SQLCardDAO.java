package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import by.epam.payment_system.dao.CardDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.CardStatus;

public class SQLCardDAO implements CardDAO {

	private static final String SELECT_CARDS_SQL = "SELECT * FROM CARDS JOIN CARD_STATUSES ON CARDS.STATUS_ID=CARD_STATUSES.ID WHERE OWNER=? ";
	private static final String SELECT_CARD_DATA_SQL = "SELECT * FROM CARDS JOIN CARD_STATUSES ON CARDS.STATUS_ID=CARD_STATUSES.ID WHERE NUMBER_CARD=? ";
	private static final String UPDATE_IS_BLOCKED_SQL = "UPDATE CARDS SET IS_BLOCKED=? WHERE NUMBER_CARD=? AND IS_CLOSED=FALSE";
	private static final String UPDATE_IS_CLOSED_SQL = "UPDATE CARDS SET IS_CLOSED=TRUE WHERE NUMBER_CARD=? ";
	private static final String COLUMN_NUMBER_CARD = "number_card";
	private static final String COLUMN_ACCOUNT = "account";
	private static final String COLUMN_OWNER = "owner";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_IS_BLOCKED = "is_blocked";
	private static final String COLUMN_IS_CLOSED = "is_closed";

	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	@Override
	public List<Card> findCards(Integer userId) throws DAOException {

		List<Card> cardList = new ArrayList<Card>();

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_CARDS_SQL)) {

			statement.setInt(1, userId);
			ResultSet resultSet = statement.executeQuery();

			String numberCard;
			String numberAccount;
			String status;
			CardStatus cardStatus;
			boolean isBlocked;
			boolean isClosed;
			Card card;

			while (resultSet.next()) {
				numberCard = resultSet.getString(COLUMN_NUMBER_CARD);
				numberAccount = resultSet.getString(COLUMN_ACCOUNT);
				status = resultSet.getString(COLUMN_STATUS);
				cardStatus = CardStatus.valueOf(status.toUpperCase());
				isBlocked = resultSet.getBoolean(COLUMN_IS_BLOCKED);
				isClosed = resultSet.getBoolean(COLUMN_IS_CLOSED);
				card = new Card(numberCard, numberAccount, cardStatus, userId, isBlocked, isClosed);
				cardList.add(card);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}

		return cardList;
	}

	@Override
	public boolean updateBlocking(Card card) throws DAOException {

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_IS_BLOCKED_SQL);) {

			statement.setBoolean(1, card.getIsBlocked());
			statement.setString(2, card.getNumberCard());

			return statement.executeUpdate() != 0;

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}

	// @Nullable
	@Override
	public Card findCardData(String numberCard) throws DAOException {

		Card card = null;
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_CARD_DATA_SQL)) {

			statement.setString(1, numberCard);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String numberAccount = resultSet.getString(COLUMN_ACCOUNT);
				String status = resultSet.getString(COLUMN_STATUS);
				CardStatus cardStatus = CardStatus.valueOf(status.toUpperCase());
				int owner = resultSet.getInt(COLUMN_OWNER);
				boolean isBlocked = resultSet.getBoolean(COLUMN_IS_BLOCKED);
				boolean isClosed = resultSet.getBoolean(COLUMN_IS_CLOSED);
				card = new Card(numberCard, numberAccount, cardStatus, owner, isBlocked, isClosed);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}

		return card;
	}

	@Override
	public boolean setClosed(String numberCard) throws DAOException {

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_IS_CLOSED_SQL)) {

			statement.setString(1, numberCard);

			return statement.executeUpdate() != 0;

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}
}
