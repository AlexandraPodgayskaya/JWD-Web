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
	private static final String UPDATE_BLOCKING_SQL = "UPDATE CARDS SET IS_BLOCKED=? WHERE NUMBER_CARD=? AND IS_CLOSED=FALSE";
	private static final String SELECT_IS_BLOCKED_SQL = "SELECT IS_BLOCKED FROM CARDS WHERE OWNER=? ";
	private static final String PARAMETR_NUMBER_CARD = "number_card";
	private static final String PARAMETR_ACCOUNT = "account";
	private static final String PARAMETR_OWNER = "owner";
	private static final String PARAMETR_STATUS = "status";
	private static final String PARAMETR_IS_BLOCKED = "is_blocked";
	private static final String PARAMETR_IS_CLOSED = "is_closed";

	@Override
	public List<Card> findCards(Integer userId) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Card> cardList = new ArrayList<Card>();

		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(SELECT_CARDS_SQL);

			statement.setInt(1, userId);
			resultSet = statement.executeQuery();

			String numberCard;
			String numberAccount;
			String status;
			CardStatus cardStatus;
			boolean isBlocked;
			boolean isClosed;
			Card card;

			while (resultSet.next()) {
				numberCard = resultSet.getString(PARAMETR_NUMBER_CARD);
				numberAccount = resultSet.getString(PARAMETR_ACCOUNT);
				status = resultSet.getString(PARAMETR_STATUS);
				cardStatus = CardStatus.valueOf(status.toUpperCase());
				isBlocked = resultSet.getBoolean(PARAMETR_IS_BLOCKED);
				isClosed = resultSet.getBoolean(PARAMETR_IS_CLOSED);
				card = new Card(numberCard, numberAccount, cardStatus, userId, isBlocked, isClosed);
				cardList.add(card);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		} finally {
			try {
				connectionPool.closeConnection(connection, statement);
			} catch (ConnectionPoolException e) {
				throw new DAOException(e);
			}
		}
		return cardList;
	}

	@Override
	public boolean updateBlocking(Card card) throws DAOException {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(UPDATE_BLOCKING_SQL);

			statement.setBoolean(1, card.getIsBlocked());
			statement.setString(2, card.getNumberCard());
			return statement.executeUpdate() != 0;

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

	public static void main(String[] args) throws DAOException {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		boolean isBlocked = false;
		try {
			connectionPool.init();
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(SELECT_IS_BLOCKED_SQL);
			statement.setString(1, "1515");
			resultSet = statement.executeQuery();

			System.out.println("1");
			if (resultSet.next()) {
				System.out.println("2");
				isBlocked = resultSet.getBoolean(PARAMETR_IS_BLOCKED);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		} finally {
			try {
				connectionPool.closeConnection(connection, statement);
				connectionPool.destroy();
			} catch (ConnectionPoolException e) {
				throw new DAOException(e);
			}
		}
		System.out.println(isBlocked);
	}

	// @Nullable
	@Override
	public Card findCardData(String numberCard) throws DAOException {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		Card card = null;
		try {
			connection = connectionPool.takeConnection();
			statement = connection.prepareStatement(SELECT_CARD_DATA_SQL);

			statement.setString(1, numberCard);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String numberAccount = resultSet.getString(PARAMETR_ACCOUNT);
				String status = resultSet.getString(PARAMETR_STATUS);
				CardStatus cardStatus = CardStatus.valueOf(status.toUpperCase());
				int owner = resultSet.getInt(PARAMETR_OWNER);
				boolean isBlocked = resultSet.getBoolean(PARAMETR_IS_BLOCKED);
				boolean isClosed = resultSet.getBoolean(PARAMETR_IS_CLOSED);
				card = new Card(numberCard, numberAccount, cardStatus, owner, isBlocked, isClosed);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		} finally {
			try {
				connectionPool.closeConnection(connection, statement);
			} catch (ConnectionPoolException e) {
				throw new DAOException(e);
			}
		}
		return card;
	}

}
