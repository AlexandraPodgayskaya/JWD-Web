package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

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
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.AdditionalClientDataService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.ServiceException;

public class FindClientCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	private static final String MESSAGE_CLIENT_NOT_FOUND = "local.message.client_not_found";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.info("session aborted");
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return;
		}

		session.removeAttribute(Attribute.FOUND_CLIENT_INFO);

		ServiceFactory factory = ServiceFactory.getInstance();
		AdditionalClientDataService clientDataService = factory.getAdditionalClientDataService();

		try {
			UserInfo userInfo = clientDataService.getData(request.getParameter(Parameter.NUMBER_PASSPORT));
			session.setAttribute(Attribute.FOUND_CLIENT_INFO, userInfo);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (NoSuchUserServiceException e) {
			logger.error("user is not found", e);
			session.setAttribute(Attribute.INFO_MESSAGE, MESSAGE_CLIENT_NOT_FOUND);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
