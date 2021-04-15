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

/**
 * The service is responsible for user operations
 * 
 * @author Aleksandra Podgayskaya
 * @see UserService
 */
public class UserServiceImpl implements UserService {

	/**
	 * Instance of {@link DAOFactory}
	 */
	private static final DAOFactory factory = DAOFactory.getInstance();

	/**
	 * Check login and password
	 * 
	 * @param userInfo {@link UserInfo} credentials
	 * @return {@link User}
	 * @throws ServiceException if credentials is incorrect, password encryption
	 *                          errors or {@link DAOException} occurs
	 */
	@Override
	public User authorization(UserInfo userInfo) throws ServiceException {

		UserDataValidator validator = new UserDataValidator();

		if (!validator.credentialsValidation(userInfo)) {
			throw new NoSuchUserServiceException("no such user");
		}

		UserDAO userDAO = factory.getUserDAO();

		User user;
		try {
			userInfo.setPassword(PasswordEncryption.encrypt(userInfo.getPassword()));
			Optional<User> userOptional = userDAO.findUser(userInfo);

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

	/**
	 * Add login and password for new user
	 * 
	 * @param userInfo {@link UserInfo} credentials
	 * @return {@link Integer} new user's id
	 * @throws ServiceException if credentials is incorrect, login is busy, password
	 *                          encryption error or {@link DAOException} occurs
	 */
	@Override
	public Integer registration(UserInfo userInfo) throws ServiceException {

		UserDataValidator validator = new UserDataValidator();

		if (!validator.credentialsValidation(userInfo)) {
			throw new UserInfoFormatServiceException("user data format error", validator.getDescriptionList());
		}

		if (!checkIfLoginFree(userInfo.getLogin())) {
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

	/**
	 * Change login
	 * 
	 * @param userInfo {@link UserInfo} credentials
	 * @param newLogin {@link String} new login
	 * @throws ServiceException if wrong password to confirm the operation, new
	 *                          login is incorrect or busy, login is not changed or
	 *                          {@link DAOException} occurs
	 */
	@Override
	public void changeLogin(UserInfo userInfo, String newLogin) throws ServiceException {

		if (!PasswordCheck.isCorrect(userInfo)) {
			throw new WrongPasswordServiceException("wrong password");
		}

		if (!UserDataValidator.loginValidation(newLogin)) {
			throw new UserInfoFormatServiceException("new login format error");
		}

		if (!checkIfLoginFree(newLogin)) {
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
	@Override
	public void changePassword(UserInfo userInfo, String newPassword) throws ServiceException {
		if (!PasswordCheck.isCorrect(userInfo)) {
			throw new WrongPasswordServiceException("wrong password for confirmation");
		}

		if (!UserDataValidator.passwordValidation(newPassword)) {
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

	/**
	 * Check if login is free
	 * 
	 * @param login {@link String} login
	 * @return boolean
	 * @throws ServiceException if {@link DAOException} occurs
	 */
	private boolean checkIfLoginFree(String login) throws ServiceException {
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
