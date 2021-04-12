package by.epam.payment_system.dao;

import java.sql.SQLException;
import java.util.Optional;

import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.UserInfo;

/**
 * The interface for working with database table client_details
 * 
 * @author Aleksandra Podgayskaya
 */
public interface AdditionalClientDataDAO {

	/**
	 * Create client details
	 * 
	 * @param additionalClientInfo {@link UserInfo} all data to create
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	void create(UserInfo additionalClientInfo) throws DAOException;

	/**
	 * Find client details by personal number passport
	 * 
	 * @param personalNumberPassport {@link String} personal number passport to
	 *                               search
	 * @return {@link Optional} of {@link UserInfo} received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	Optional<UserInfo> findDataByPassport(String personalNumberPassport) throws DAOException;

	/**
	 * Find client details by user id
	 * 
	 * @param userId {@link Integer} personal user id to search
	 * @return {@link Optional} of {@link UserInfo} received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	Optional<UserInfo> findDataById(Integer userId) throws DAOException;

	/**
	 * Update client details
	 * 
	 * @param userInfo {@link UserInfo} data to update
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	boolean update(UserInfo userInfo) throws DAOException;

}
