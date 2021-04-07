package by.epam.payment_system.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.UserDAO;
import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.service.UserService;
import by.epam.payment_system.service.exception.BusyLoginServiceException;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;
import by.epam.payment_system.service.exception.WrongPasswordServiceException;
import by.epam.payment_system.service.util.PasswordCheck;
import by.epam.payment_system.service.util.PasswordEncryption;
import by.epam.payment_system.service.validation.UserDataValidator;

public class UserServiceImpl implements UserService {

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
			Optional<User> userOptional = userDAO.find(userInfo);

			if (userOptional.isEmpty()) {
				throw new NoSuchUserServiceException("no such user");
			}

			user = userOptional.get();

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

		userInfo.setUserType(UserType.CLIENT);

		UserDAO userDAO = factory.getUserDAO();

		Integer id;
		try {
			userInfo.setPassword(PasswordEncryption.encrypt(userInfo.getPassword()));
			userDAO.create(userInfo);
			Optional<Integer> idOptional = userDAO.findId(userInfo.getLogin());
			if (idOptional.isEmpty()) {
				throw new ServiceException("user not created");
			}
			id = idOptional.get();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new ServiceException("password encryption error", e);
		} catch (DAOException e) {
			throw new ServiceException("user creation error", e);
		}

		return id;
	}

	@Override
	public void changeLogin(UserInfo userInfo, String newLogin) throws ServiceException {

		if (!PasswordCheck.isCorrect(userInfo)) {
			throw new WrongPasswordServiceException("wrong password");
		}

		UserDataValidator userValidator = new UserDataValidator();

		if (!userValidator.loginValidation(newLogin)) {
			throw new UserInfoFormatServiceException("new login format error");
		}

		if (!freeLogin(newLogin)) {
			throw new BusyLoginServiceException("login is busy");
		}

		userInfo.setLogin(newLogin);
		UserDAO userDAO = factory.getUserDAO();

		try {
			if (!userDAO.updateUser(userInfo)) {
				throw new ServiceException("login change error");
			}

		} catch (DAOException e) {
			throw new ServiceException("login change error", e);
		}

	}

	@Override
	public void changePassword(UserInfo userInfo, String newPassword) throws ServiceException {
		if (!PasswordCheck.isCorrect(userInfo)) {
			throw new WrongPasswordServiceException("wrong password for confirmation");
		}

		UserDataValidator userValidator = new UserDataValidator();

		if (!userValidator.passwordValidation(newPassword)) {
			throw new UserInfoFormatServiceException("new password format error");
		}

		UserDAO userDAO = factory.getUserDAO();

		try {
			userInfo.setPassword(PasswordEncryption.encrypt(newPassword));
			if (!userDAO.updateUser(userInfo)) {
				throw new ServiceException("password change error");
			}

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new ServiceException("password encryption error", e);
		} catch (DAOException e) {
			throw new ServiceException("password change error", e);
		}

	}

	private boolean freeLogin(String login) throws ServiceException {
		UserDAO userDAO = factory.getUserDAO();

		Optional<Integer> idOptional;
		try {
			idOptional = userDAO.findId(login);
		} catch (DAOException e) {
			throw new ServiceException("impossible to check whether the login is free", e);
		}
		return idOptional.isEmpty();
	}

}
