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
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.AdditionalClientDataService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.ServiceException;

public class GoToEditProfilePageCommandImpl implements Command {
	
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

		ServiceFactory factory = ServiceFactory.getInstance();
		AdditionalClientDataService additionalClientDataService = factory.getAdditionalClientDataService();
		
		Integer userId = (Integer)session.getAttribute(Attribute.USER_ID);
		try {
			UserInfo userInfo = additionalClientDataService.getData(userId);
			request.setAttribute(Attribute.USER_INFO, userInfo);
			session.setAttribute(Attribute.PAGE, GoToPage.EDIT_PROFILE_PAGE);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_EDIT_PROFILE_PAGE);
			requestDispatcher.forward(request, response);
		} catch (NoSuchUserServiceException e) {
			request.setAttribute(Attribute.USER_INFO, new UserInfo());
			session.setAttribute(Attribute.PAGE, GoToPage.EDIT_PROFILE_PAGE);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_EDIT_PROFILE_PAGE);
			requestDispatcher.forward(request, response);		
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}
	}

}
