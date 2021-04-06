package by.epam.payment_system.service;

import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.exception.ServiceException;

public interface UserService {

	User authorization(UserInfo loginationInfo) throws ServiceException;

	Integer registration(UserInfo registrationInfo) throws ServiceException;

	void changeLogin(UserInfo userInfo, String newLogin) throws ServiceException;

}
