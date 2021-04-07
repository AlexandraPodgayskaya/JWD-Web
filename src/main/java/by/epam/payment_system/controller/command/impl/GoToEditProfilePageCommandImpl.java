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
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.AdditionalClientDataService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public class GoToEditProfilePageCommandImpl implements Command {

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

		ServiceFactory factory = ServiceFactory.getInstance();
		AdditionalClientDataService additionalClientDataService = factory.getAdditionalClientDataService();

		Integer userId = (Integer) session.getAttribute(ParameterConstraint.USER_ID);
		try {
			UserInfo userInfo = additionalClientDataService.getData(userId);
			request.setAttribute(ParameterConstraint.USER_INFO, userInfo);
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.EDIT_PROFILE_PAGE);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_EDIT_PROFILE_PAGE);
			requestDispatcher.forward(request, response);
		} catch (NoSuchUserServiceException e) {
			request.setAttribute(ParameterConstraint.USER_INFO, new UserInfo());
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.EDIT_PROFILE_PAGE);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_EDIT_PROFILE_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}
	}

}
