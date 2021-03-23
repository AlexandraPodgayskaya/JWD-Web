package by.epam.payment_system.service.impl;

import by.epam.payment_system.dao.AdditionalClientDataDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.AdditionalClientDataService;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;
import by.epam.payment_system.service.validation.UserDataValidator;

public class AdditionalClientDataServiceImpl implements AdditionalClientDataService {

	@Override
	public void addData(UserInfo additionalClientInfo) throws ServiceException {

		UserDataValidator validator = new UserDataValidator();

		if (!validator.additionalDataValidation(additionalClientInfo)) {
			throw new UserInfoFormatServiceException("user data format error", validator.getDescriptionList());
		}

		DAOFactory factory = DAOFactory.getInstance();
		AdditionalClientDataDAO additionalClientDataDAO = factory.getAdditionalClientDataDAO();
		try {
			additionalClientDataDAO.create(additionalClientInfo);
		} catch (DAOException e) {
			throw new ServiceException("additional client data creation error", e);
		}

	}

}
