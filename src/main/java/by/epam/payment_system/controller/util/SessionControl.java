package by.epam.payment_system.controller.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public final class SessionControl {

	private static final Logger logger = LogManager.getLogger();
	
	public static boolean isExist (HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.info("session timed out");
			session = request.getSession(true);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Message.ERROR_SESSION_TIMED_OUT);
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return false;
		}

		if (session.getAttribute(ParameterConstraint.USER_LOGIN) == null) {
			logger.info("there was log out");
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Message.ERROR_LOGOUT);
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return false;
		}
		
		return true;
	}
	
	private SessionControl() {

	}
}
