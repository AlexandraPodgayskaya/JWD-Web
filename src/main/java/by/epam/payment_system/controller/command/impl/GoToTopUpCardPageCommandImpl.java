package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.util.GoToPage;
import by.epam.payment_system.controller.util.URIConstructor;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public class GoToTopUpCardPageCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.info("session timed out");
			session = request.getSession(true);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Message.ERROR_SESSION_TIMED_OUT);
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return;
		}

		if (session.getAttribute(ParameterConstraint.USER_LOGIN) == null) {
			logger.info("there was log out");
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Message.ERROR_LOGOUT);
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return;
		}

		request.setAttribute(ParameterConstraint.NUMBER_CARD, request.getParameter(ParameterConstraint.NUMBER_CARD));
		request.setAttribute(ParameterConstraint.CURRENCY, request.getParameter(ParameterConstraint.CURRENCY));

		session.setAttribute(ParameterConstraint.PAGE,
				GoToPage.TOP_UP_CARD_PAGE + URIConstructor.SET_NUMBER_CARD + request.getParameter(ParameterConstraint.NUMBER_CARD)
						+ URIConstructor.SET_CURRENCY + request.getParameter(ParameterConstraint.CURRENCY));

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_TOP_UP_CARD_PAGE);
		requestDispatcher.forward(request, response);
	}
}
