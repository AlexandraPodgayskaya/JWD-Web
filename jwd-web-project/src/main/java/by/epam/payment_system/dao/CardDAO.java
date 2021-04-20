package by.epam.payment_system.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Card;

/**
 * The interface for working with database table cards
 * 
 * @author Aleksandra Podgayskaya
 */
public interface CardDAO {

	/**
	 * Create card
	 * 
	 * @param card {@link Card} all data to create
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	void create(Card card) throws DAOException;

	/**
	 * Find card information by card number
	 * 
	 * @param numberCard {@link String} card number to search
	 * @return {@link Optional} of {@link Card} received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	Optional<Card> findCardData(String numberCard) throws DAOException;

	/**
	 * Find all cards by user id
	 * 
	 * @param userId {@link Long} user id to search
	 * @return {@link List} of {@link Card} received from database if cards are
	 *         found, else emptyList
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	List<Card> findCards(Long userId) throws DAOException;

	/**
	 * Update blocking
	 * 
	 * @param card {@link Card} data to update
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	boolean updateBlocking(Card card) throws DAOException;

	/**
	 * Set card closed
	 * 
	 * @param numberCard {@link String} card number to set closed
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	boolean setClosed(String numberCard) throws DAOException;

}
