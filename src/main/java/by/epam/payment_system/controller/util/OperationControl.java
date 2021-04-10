package by.epam.payment_system.controller.util;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public final class OperationControl {

	private static final Logger logger = LogManager.getLogger();

	private OperationControl() {
	}

	public static boolean isAllowedToUser(HttpServletRequest request, HttpServletResponse response, UserType userType)
			throws IOException {
		HttpSession session = request.getSession(true);
		if (session.getAttribute(ParameterConstraint.USER_TYPE) != userType) {
			logger.info("impossible operation for " + UserType.ADMIN);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_IMPOSSIBLE_OPERATION));
			response.sendRedirect(GoToPage.MAIN_PAGE);
			return false;
		}

		return true;
	}

}
