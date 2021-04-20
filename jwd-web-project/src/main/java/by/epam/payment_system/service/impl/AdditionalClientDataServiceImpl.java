package by.epam.payment_system.service.impl;

import java.util.Optional;

import by.epam.payment_system.dao.AdditionalClientDataDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.AdditionalClientDataService;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;
import by.epam.payment_system.service.validation.UserDataValidator;

/**
 * The service is responsible for operations with client data
 * 
 * @author Aleksandra Podgayskaya
 * @see AdditionalClientDataService
 */
public class AdditionalClientDataServiceImpl implements AdditionalClientDataService {

	/**
	 * Instance of {@link DAOFactory}
	 */
	private static final DAOFactory factory = DAOFactory.getInstance();

	/**
	 * Add client details
	 * 
	 * @param additionalClientInfo {@link UserInfo} client details
	 * @throws ServiceException if client details is incorrect or
	 *                          {@link DAOException} occurs
	 */
	@Override
	public void addData(UserInfo additionalClientInfo) throws ServiceException {

		UserDataValidator validator = new UserDataValidator();

		if (!validator.additionalDataValidation(additionalClientInfo)) {
			throw new UserInfoFormatServiceException("user data format error", validator.getDescriptionList());
		}

		AdditionalClientDataDAO additionalClientDataDAO = factory.getAdditionalClientDataDAO();
		try {
			additionalClientDataDAO.create(additionalClientInfo);
		} catch (DAOException e) {
			throw new ServiceException("additional client data creation error", e);
		}
	}
	
	/**
	 * Take client details by user id
	 * 
	 * @param userId {@link Long} user id
	 * @return {@link UserInfo}
	 * @throws ServiceException if userId is null, data not found or
	 *                          {@link DAOException} occurs
	 */
	@Override
	public UserInfo getData(Long userId) throws ServiceException {

		if (userId == null) {
			throw new ImpossibleOperationServiceException("no user id to get data");
		}

		AdditionalClientDataDAO additionalClientDataDAO = factory.getAdditionalClientDataDAO();

		Optional<UserInfo> userInfoOptional;
		try {
			userInfoOptional = additionalClientDataDAO.findDataById(userId);

			if (userInfoOptional.isEmpty()) {
				throw new NoSuchUserServiceException("no client data");
			}
		} catch (DAOException e) {
			throw new ServiceException("client data find error", e);
		}
		return userInfoOptional.get();
	}

	/**
	 * Take client details by personal number passport
	 * 
	 * @param personalNumberPassport {@link String} personal number passport of the
	 *                               client
	 * @return {@link UserInfo}
	 * @throws ServiceException if personalNumberPassport is incorrect, client not
	 *                          found or {@link DAOException} occurs
	 */
	@Override
	public UserInfo search(String personalNumberPassport) throws ServiceException {

		if (!UserDataValidator.numberPassportValidation(personalNumberPassport)) {
			throw new UserInfoFormatServiceException("personal number passport format error");
		}

		AdditionalClientDataDAO additionalClientDataDAO = factory.getAdditionalClientDataDAO();

		Optional<UserInfo> userInfoOptional;
		try {
			userInfoOptional = additionalClientDataDAO.findDataByPassport(personalNumberPassport);

			if (userInfoOptional.isEmpty()) {
				throw new NoSuchUserServiceException("no client data");
			}
		} catch (DAOException e) {
			throw new ServiceException("client data find error", e);
		}
		return userInfoOptional.get();
	}

	/**
	 * Change client details
	 * 
	 * @param userInfo {@link UserInfo} client details to change
	 * @throws ServiceException if client details is incorrect, the data has not
	 *                          changed or {@link DAOException} occurs
	 */
	@Override
	public void changeData(UserInfo userInfo) throws ServiceException {
		UserDataValidator validator = new UserDataValidator();

		if (!validator.additionalDataValidation(userInfo)) {
			throw new UserInfoFormatServiceException("user data format error", validator.getDescriptionList());
		}

		AdditionalClientDataDAO additionalClientDataDAO = factory.getAdditionalClientDataDAO();
		Optional<UserInfo> userInfoOptional;
		try {
			userInfoOptional = additionalClientDataDAO.findDataById(userInfo.getId());

			if (userInfoOptional.isEmpty()) {
				additionalClientDataDAO.create(userInfo);
			} else {
				if (!additionalClientDataDAO.update(userInfo)) {
					throw new ServiceException("client data change error");
				}
			}
		} catch (DAOException e) {
			throw new ServiceException("client data change error", e);
		}

	}
}
