package by.epam.payment_system.controller.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

/**
 * Control the possibility of an operation for user type
 * 
 * @author Aleksandra Podgayskaya
 */
public final class OperationControl {

	private static final Logger logger = LogManager.getLogger();

	private OperationControl() {
	}

	/**
	 * Check if the operation is allowed to user
	 * 
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param userType {@link UserType} type for which the operation is allowed
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean isAllowedToUser(HttpServletRequest request, HttpServletResponse response, UserType userType)
			throws IOException {
		HttpSession session = request.getSession(true);
		if (session.getAttribute(ParameterConstraint.USER_TYPE) != userType) {
			logger.info("impossible operation for " + UserType.ADMIN);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Message.ERROR_IMPOSSIBLE_OPERATION);
			response.sendRedirect(GoToPage.MAIN_PAGE);
			return false;
		}

		return true;
	}

}
