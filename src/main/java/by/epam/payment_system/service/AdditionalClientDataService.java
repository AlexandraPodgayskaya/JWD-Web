package by.epam.payment_system.service;

import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.exception.ServiceException;

public interface AdditionalClientDataService {
	
	void addData(UserInfo additionalUserClientInfo) throws ServiceException;
	
	UserInfo getData (String personalNumberPassport) throws ServiceException;

}
