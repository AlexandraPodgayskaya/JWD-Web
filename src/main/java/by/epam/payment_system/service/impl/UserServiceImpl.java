package by.epam.payment_system.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.UserDAO;
import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.UserService;
import by.epam.payment_system.service.exception.BusyLoginServiceException;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;
import by.epam.payment_system.service.util.encryption.PasswordEncryption;
import by.epam.payment_system.service.validation.UserDataValidator;

public class UserServiceImpl implements UserService {

	private static final int TYPE_USER_CLIENT = 1;
	
	private static final DAOFactory factory = DAOFactory.getInstance();

	@Override
	public User authorization(UserInfo userInfo) throws ServiceException {

		UserDataValidator validator = new UserDataValidator();

		if (!validator.basicDataValidation(userInfo)) {
			throw new NoSuchUserServiceException("no such user");
		}

		UserDAO userDAO = factory.getUserDAO();

		User user;
		try {
			userInfo.setPassword(PasswordEncryption.encrypt(userInfo.getPassword()));
			user = userDAO.find(userInfo);
			if (user == null) {
				throw new NoSuchUserServiceException("no such user");
			}

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new ServiceException("password encryption error", e);
		} catch (DAOException e) {
			throw new ServiceException("user search error", e);
		}
		return user;
	}

	@Override
	public Integer registration(UserInfo userInfo) throws ServiceException {

		UserDataValidator validator = new UserDataValidator();

		if (!validator.basicDataValidation(userInfo)) {
			throw new UserInfoFormatServiceException("user data format error", validator.getDescriptionList());
		}

		if (!freeLogin(userInfo.getLogin())) {
			throw new BusyLoginServiceException("login is busy");
		}

		userInfo.setTypeUserId(TYPE_USER_CLIENT);

		UserDAO userDAO = factory.getUserDAO();

		Integer id;
		try {
			userInfo.setPassword(PasswordEncryption.encrypt(userInfo.getPassword()));
			userDAO.create(userInfo);
			id = userDAO.findId(userInfo.getLogin());
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new ServiceException("password encryption error", e);
		} catch (DAOException e) {
			throw new ServiceException("user creation error", e);
		}

		return id;
	}

	private boolean freeLogin(String login) throws ServiceException {
		UserDAO userDAO = factory.getUserDAO();

		Integer id;
		try {
			id = userDAO.findId(login);
		} catch (DAOException e) {
			throw new ServiceException("id search error", e);
		}
		return id == null;
	}

}
