package by.epam.payment_system.service.impl;

import java.util.Optional;

import by.epam.payment_system.dao.AdditionalClientDataDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.AdditionalClientDataService;
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
	public UserInfo getData(String personalNumberPassport) throws ServiceException {

		UserDataValidator validator = new UserDataValidator();

		if (!validator.numberPassportValidation(personalNumberPassport)) {
			throw new NoSuchUserServiceException("personal number passport format error");
		}

		AdditionalClientDataDAO additionalClientDataDAO = factory.getAdditionalClientDataDAO();

		Optional <UserInfo> userInfoOptional;
		try {
			userInfoOptional = additionalClientDataDAO.findData(personalNumberPassport);
			
			if (userInfoOptional.isEmpty()) {
				throw new NoSuchUserServiceException("no client data");
			}
		} catch (DAOException e) {
			throw new ServiceException("client data find error", e);
		}
		return userInfoOptional.get();
	}

}
