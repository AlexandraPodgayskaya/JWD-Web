package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.command.Attribute;
import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.command.GoToPage;
import by.epam.payment_system.controller.command.Parameter;

public class GoToTopUpCardPageCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();
	
	private static final String ERROR_SESSION_TIMED_OUT = "local.error.session_timed_out";
	private static final String ERROR_LOGOUT = "local.error.logout";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.info("session timed out");
			session = request.getSession(true);
			session.setAttribute(Attribute.ERROR_MESSAGE, ERROR_SESSION_TIMED_OUT);
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return;
		}
		
		if (session.getAttribute(Attribute.USER_LOGIN) == null) {
			logger.info("there was log out");
			session.setAttribute(Attribute.ERROR_MESSAGE, ERROR_LOGOUT);
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return;
		}

		request.setAttribute(Attribute.NUMBER_CARD, request.getParameter(Parameter.NUMBER_CARD));
		request.setAttribute(Attribute.CURRENCY, request.getParameter(Parameter.CURRENCY));

		session.setAttribute(Attribute.PAGE,
				GoToPage.TOP_UP_CARD_PAGE + Parameter.SET_NUMBER_CARD + request.getParameter(Parameter.NUMBER_CARD)
						+ Parameter.SET_CURRENCY + request.getParameter(Parameter.CURRENCY));

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_TOP_UP_CARD_PAGE);
		requestDispatcher.forward(request, response);
	}
}
