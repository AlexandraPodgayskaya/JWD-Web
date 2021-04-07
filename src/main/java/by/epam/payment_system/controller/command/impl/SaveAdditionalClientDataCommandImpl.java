package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.builder.AbstractUserInfoBuilder;
import by.epam.payment_system.controller.builder.AdditionalUserClientInfoBuilder;
import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.util.GoToPage;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.AdditionalClientDataService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public class SaveAdditionalClientDataCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AbstractUserInfoBuilder builder = new AdditionalUserClientInfoBuilder();
		builder.buildUserInfo(request);
		UserInfo additionalClientInfo = builder.getUserInfo();

		ServiceFactory factory = ServiceFactory.getInstance();
		AdditionalClientDataService additionalClientDataService = factory.getAdditionalClientDataService();

		HttpSession session = request.getSession(true);
		try {
			additionalClientDataService.addData(additionalClientInfo);
			session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_PROFILE_SAVED);
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.INDEX_PAGE);
			response.sendRedirect(GoToPage.INDEX_PAGE);
		} catch (UserInfoFormatServiceException e) {
			logger.error("wrong user info format", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, e.getErrorDescription());
			response.sendRedirect(GoToPage.CLIENT_DATA_PAGE + additionalClientInfo.getId());
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}
	}
}
