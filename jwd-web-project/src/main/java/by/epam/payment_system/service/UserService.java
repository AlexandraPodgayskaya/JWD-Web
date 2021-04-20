package by.epam.payment_system.service;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.exception.ServiceException;

/**
 * The interface for user operations
 * 
 * @author Aleksandra Podgayskaya
 */
public interface UserService {

	/**
	 * Check login and password
	 * 
	 * @param userInfo {@link UserInfo} credentials
	 * @return {@link User}
	 * @throws ServiceException if credentials is incorrect, password encryption
	 *                          error or {@link DAOException} occurs
	 */
	User authorization(UserInfo userInfo) throws ServiceException;

	/**
	 * Add login and password for new user
	 * 
	 * @param userInfo {@link UserInfo} credentials
	 * @return long new user's id
	 * @throws ServiceException if credentials is incorrect, login is busy, password
	 *                          encryption error or {@link DAOException} occurs
	 */
	long registration(UserInfo userInfo) throws ServiceException;

	/**
	 * Change login
	 * 
	 * @param userInfo {@link UserInfo} credentials
	 * @param newLogin {@link String} new login
	 * @throws ServiceException if wrong password to confirm the operation, new
	 *                          login is incorrect or busy, login is not changed or
	 *                          {@link DAOException} occurs
	 */
	void changeLogin(UserInfo userInfo, String newLogin) throws ServiceException;

	/**
	 * Change password
	 * 
	 * @param userInfo    {@link UserInfo} credentials
	 * @param newPassword {@link String} new password
	 * @throws ServiceException if wrong password to confirm the operation, new
	 *                          password is incorrect, password encryption error,
	 *                          password is not changed or {@link DAOException}
	 *                          occurs
	 */
	void changePassword(UserInfo userInfo, String newPassword) throws ServiceException;

}
