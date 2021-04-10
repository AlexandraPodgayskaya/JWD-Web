package by.epam.payment_system.controller.command.impl;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.util.GoToPage;
import by.epam.payment_system.controller.util.OperationControl;
import by.epam.payment_system.controller.util.SessionControl;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.service.AdditionalClientDataService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public class FindClientCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (!SessionControl.isExist(request, response)
				|| !OperationControl.isAllowedToUser(request, response, UserType.ADMIN)) {
			return;
		}

		HttpSession session = request.getSession(true);

		session.removeAttribute(ParameterConstraint.FOUND_CLIENT_INFO);

		ServiceFactory factory = ServiceFactory.getInstance();
		AdditionalClientDataService clientDataService = factory.getAdditionalClientDataService();

		try {
			UserInfo userInfo = clientDataService.search(request.getParameter(ParameterConstraint.NUMBER_PASSPORT));
			session.setAttribute(ParameterConstraint.FOUND_CLIENT_INFO, userInfo);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (UserInfoFormatServiceException e) {
			logger.error("incorrect data for search", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE,
					Arrays.asList(Message.ERROR_PERSONAL_NUMBER_PASSPORT));
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (NoSuchUserServiceException e) {
			logger.error("user is not found", e);
			session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_CLIENT_NOT_FOUND);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
