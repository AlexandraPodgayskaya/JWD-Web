package by.epam.payment_system.service.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.UserDAO;
import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.validation.UserDataValidator;

/**
 * Checking the password
 * 
 * @author Aleksandra Podgayskaya
 */
public final class PasswordCheck {

	private PasswordCheck() {

	}

	/**
	 * Check if the password is correct
	 * 
	 * @param userInfo {@link UserInfo} credentials
	 * @return boolean
	 * @throws ServiceException if credentials is incorrect, password encryption
	 *                          errors or {@link DAOException} occurs
	 */
	public static boolean isCorrect(UserInfo userInfo) throws ServiceException {

		UserDataValidator userValidator = new UserDataValidator();

		if (!userValidator.credentialsValidation(userInfo)) {
			return false;
		}

		DAOFactory factory = DAOFactory.getInstance();
		UserDAO userDAO = factory.getUserDAO();
		Optional<User> userOptional;

		try {
			userInfo.setPassword(PasswordEncryption.encrypt(userInfo.getPassword()));
			userOptional = userDAO.findUser(userInfo);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new ServiceException("password encryption error", e);
		} catch (DAOException e) {
			throw new ServiceException("check password error", e);
		}

		return userOptional.isPresent();
	}

}
