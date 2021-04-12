package by.epam.payment_system.dao;

import java.sql.SQLException;
import java.util.Optional;

import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;

/**
 * The interface for working with database table users
 * 
 * @author Aleksandra Podgayskaya
 */
public interface UserDAO {

	/**
	 * Create new user
	 * 
	 * @param registrationInfo {@link UserInfo} all data to create
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	void create(UserInfo registrationInfo) throws DAOException;

	/**
	 * Find user id
	 * 
	 * @param login {@link String} login to search
	 * @return {@link Optional} of {@link Integer} user id received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	Optional<Integer> findId(String login) throws DAOException;

	/**
	 * Find user by login and password
	 * 
	 * @param loginationInfo {@link UserInfo} credentials
	 * @return {@link Optional} of {@link User} received from database
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	Optional<User> find(UserInfo loginationInfo) throws DAOException;

	/**
	 * Update user data
	 * 
	 * @param userInfo {@link UserInfo} data to update
	 * @return boolean true if everything go correct, else false
	 * @throws DAOException if {@link ConnectionPoolException} or
	 *                      {@link SQLException} occur
	 */
	boolean updateUser(UserInfo userInfo) throws DAOException;

}
