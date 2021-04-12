package by.epam.payment_system.dao;

import java.sql.SQLException;
import java.util.List;

import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.CardType;

/**
 * The interface for working with database table card_types
 * 
 * @author Aleksandra Podgayskaya
 */
public interface CardTypeDAO {

	/**
	 * Find all card types
	 * 
	 * @return {@link List} of {@link CardType} received from database if card types
	 *         are found, else emptyList
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	List<CardType> findAll() throws DAOException;

}
