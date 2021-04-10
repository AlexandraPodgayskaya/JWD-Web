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

public class AdditionalClientDataServiceImpl implements AdditionalClientDataService {

	private static final DAOFactory factory = DAOFactory.getInstance();

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

	@Override
	public UserInfo getData(Integer userId) throws ServiceException {

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
