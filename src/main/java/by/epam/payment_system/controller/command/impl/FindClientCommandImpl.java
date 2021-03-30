package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.AdditionalClientDataService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.ServiceException;

public class FindClientCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	private static final String GO_TO_INDEX_PAGE = "index.jsp";
	private static final String GO_TO_ERROR_PAGE = "error.jsp";
	private static final String GO_TO_MAIN_PAGE = "Controller?command=go_to_main_page";
	private static final String PARAMETER_NUMBER_PASSPORT = "clientPassportNumber";
	private static final String ATTRIBUTE_FOUND_CLIENT_INFO = "foundClientInfo";
	private static final String ATTRIBUTE_INFO_MESSAGE = "infoMessage";
	private static final String MESSAGE_CLIENT_NOT_FOUND = "local.message.client_not_found";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.info("session aborted");
			response.sendRedirect(GO_TO_INDEX_PAGE);
			return;
		}
		
		session.removeAttribute(ATTRIBUTE_FOUND_CLIENT_INFO);

		ServiceFactory factory = ServiceFactory.getInstance();
		AdditionalClientDataService clientDataService = factory.getAdditionalClientDataService();

		try {
			UserInfo userInfo = clientDataService.getData(request.getParameter(PARAMETER_NUMBER_PASSPORT));
			session.setAttribute(ATTRIBUTE_FOUND_CLIENT_INFO, userInfo);
			response.sendRedirect(GO_TO_MAIN_PAGE);
		} catch (NoSuchUserServiceException e) {
			logger.error(e.getMessage());
			session.setAttribute(ATTRIBUTE_INFO_MESSAGE, MESSAGE_CLIENT_NOT_FOUND);
			response.sendRedirect(GO_TO_MAIN_PAGE);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			response.sendRedirect(GO_TO_ERROR_PAGE);
		}

	}

}
