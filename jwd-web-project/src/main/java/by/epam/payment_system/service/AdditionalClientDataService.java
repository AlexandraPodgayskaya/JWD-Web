package by.epam.payment_system.service;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.exception.ServiceException;

/**
 * The interface for operations with client data
 * 
 * @author Aleksandra Podgayskaya
 */
public interface AdditionalClientDataService {

	/**
	 * Add client details
	 * 
	 * @param additionalClientInfo {@link UserInfo} client details
	 * @throws ServiceException if client details is incorrect or
	 *                          {@link DAOException} occurs
	 */
	void addData(UserInfo additionalClientInfo) throws ServiceException;

	/**
	 * Take client details by user id
	 * 
	 * @param userId {@link Long} user id
	 * @return {@link UserInfo}
	 * @throws ServiceException if userId is null, data not found or
	 *                          {@link DAOException} occurs
	 */
	UserInfo getData(Long userId) throws ServiceException;

	/**
	 * Take client details by personal number passport
	 * 
	 * @param personalNumberPassport {@link String} personal number passport of the
	 *                               client
	 * @return {@link UserInfo}
	 * @throws ServiceException if personalNumberPassport is incorrect, client not
	 *                          found or {@link DAOException} occurs
	 */
	UserInfo search(String personalNumberPassport) throws ServiceException;

	/**
	 * Change client details
	 * 
	 * @param userInfo {@link UserInfo} client details to change
	 * @throws ServiceException if client details is incorrect, the data has not
	 *                          changed or {@link DAOException} occurs
	 */
	void changeData(UserInfo userInfo) throws ServiceException;

}
