package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import by.epam.payment_system.dao.CardDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.CardStatus;
import by.epam.payment_system.entity.CardType;

/**
 * Works with database table cards
 * 
 * @author Aleksandra Podgayskaya
 * @see CardDAO
 */
public class SQLCardDAO implements CardDAO {

	private static final String INSERT_CARD_SQL = "INSERT INTO CARDS (NUMBER_CARD, ACCOUNT, TYPE_CARD_ID, STATUS, OWNER) VALUES(?, ?, ?, ?, ?)";
	private static final String SELECT_CARDS_SQL = "SELECT * FROM CARDS JOIN CARD_TYPES ON CARDS.TYPE_CARD_ID=CARD_TYPES.ID WHERE OWNER=? ";
	private static final String SELECT_CARD_DATA_SQL = "SELECT * FROM CARDS JOIN CARD_TYPES ON CARDS.TYPE_CARD_ID=CARD_TYPES.ID WHERE NUMBER_CARD=? ";
	private static final String SELECT_LAST_CARD_NUMBER_SQL = "SELECT MAX(NUMBER_CARD) AS last_card FROM CARDS";
	private static final String UPDATE_IS_BLOCKED_SQL = "UPDATE CARDS SET IS_BLOCKED=? WHERE NUMBER_CARD=? AND IS_CLOSED=FALSE";
	private static final String UPDATE_IS_CLOSED_SQL = "UPDATE CARDS SET IS_CLOSED=TRUE WHERE NUMBER_CARD=? ";
	private static final String COLUMN_NUMBER_CARD = "number_card";
	private static final String COLUMN_ACCOUNT = "account";
	private static final String COLUMN_OWNER = "owner";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_IMAGE = "image_path";
	private static final String COLUMN_IS_BLOCKED = "is_blocked";
	private static final String COLUMN_IS_CLOSED = "is_closed";
	private static final String COLUMN_LAST_CARD = "last_card";

	/**
	 * Instance of {@link ConnectionPool}
	 */
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	/**
	 * Create card
	 * 
	 * @param card {@link Card} all data to create
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public void create(Card card) throws DAOException {
		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_CARD_SQL);) {

			statement.setString(1, card.getNumberCard());
			statement.setString(2, card.getNumberAccount());
			statement.setInt(3, card.getCardType().getId());
			statement.setString(4, String.valueOf(card.getStatus()));
			statement.setInt(5, card.getOwnerId());

			statement.executeUpdate();

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
	}

	/**
	 * Find card information by card number
	 * 
	 * @param numberCard {@link String} card number to search
	 * @return {@link Optional} of {@link Card} received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public Optional<Card> findCardData(String numberCard) throws DAOException {

		Optional<Card> cardOptional = Optional.empty();

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_CARD_DATA_SQL)) {

			statement.setString(1, numberCard);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String numberAccount = resultSet.getString(COLUMN_ACCOUNT);
				String typeCard = resultSet.getString(COLUMN_TYPE);
				String imagePath = resultSet.getString(COLUMN_IMAGE);
				String status = resultSet.getString(COLUMN_STATUS);
				CardStatus cardStatus = CardStatus.valueOf(status.toUpperCase());
				int owner = resultSet.getInt(COLUMN_OWNER);
				boolean isBlocked = resultSet.getBoolean(COLUMN_IS_BLOCKED);
				boolean isClosed = resultSet.getBoolean(COLUMN_IS_CLOSED);
				Card card = new Card(numberCard, numberAccount, new CardType(typeCard, imagePath), cardStatus, owner,
						isBlocked, isClosed);

				cardOptional = Optional.of(card);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}

		return cardOptional;
	}

	/**
	 * Find all cards by user id
	 * 
	 * @param userId {@link Integer} user id to search
	 * @return {@link List} of {@link Card} received from database if cards are
	 *         found, else emptyList
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public List<Card> findCards(Integer userId) throws DAOException {

		List<Card> cardList = new ArrayList<>();

		try (Connection connection = connectionPool.takeConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_CARDS_SQL)) {

			statement.setInt(1, userId);
			ResultSet resultSet = statement.executeQuery();

			String numberCard;
			String numberAccount;
			String typeCard;
			String imagePath;
			String status;
			CardStatus cardStatus;
			boolean isBlocked;
			boolean isClosed;
			Card card;

			while (resultSet.next()) {
				numberCard = resultSet.getString(COLUMN_NUMBER_CARD);
				numberAccount = resultSet.getString(COLUMN_ACCOUNT);
				typeCard = resultSet.getString(COLUMN_TYPE);
				imagePath = resultSet.getString(COLUMN_IMAGE);
				status = resultSet.getString(COLUMN_STATUS);
				cardStatus = CardStatus.valueOf(status.toUpperCase());
				isBlocked = resultSet.getBoolean(COLUMN_IS_BLOCKED);
				isClosed = resultSet.getBoolean(COLUMN_IS_CLOSED);
				card = new Card(numberCard, numberAccount, new CardType(typeCard, imagePath), cardStatus, userId,
						isBlocked, isClosed);
				cardList.add(card);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}

		return cardList;
	}

	/**
	 * Get last card number
	 * 
	 * @return {@link Optional} of {@link Long} last card number
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	@Override
	public Optional<Long> getLastCardNumber() throws DAOException {
		Optional<Long> lastCardOptional = Optional.empty();
		try (Connection connection = connectionPool.takeConnection();
				Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery(SELECT_LAST_CARD_NUMBER_SQL);

			if (resultSet.next()) {
				Long lastCardNumber = resultSet.getLong(COLUMN_LAST_CARD);
				lastCardOptional = Optional.of(lastCardNumber);
			}

		} catch (ConnectionPoolException | SQLException e) {
			throw new DAOException(e);
		}
		return lastCardOptional;
	}

	/**
	 * Update blocking
	 * 
	 * @param card {@link Card} data to update
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
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

	/**
	 * Set card closed
	 * 
	 * @param numberCard {@link String} card number to set closed
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
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
