package by.epam.payment_system.controller.command.impl;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.builder.AbstractUserInfoBuilder;
import by.epam.payment_system.controller.builder.UserInfoBuilder;
import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.UserService;
import by.epam.payment_system.service.exception.BusyLoginServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;

public class RegisterCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	private static final String GO_TO_CLIENT_DATA_PAGE = "UserData?userId=";
	private static final String GO_TO_REGISTRATION_PAGE = "Registration";
	private static final String GO_TO_ERROR_PAGE = "error.jsp";
	private static final String ATTRIBUTE_ERROR_MESSAGE = "errorMessageList";
	private static final String ATTRIBUTE_INFO_MESSAGE = "infoMessage";
	private static final String ATTRIBUTE_PAGE = "page";
	private static final String MESSAGE_REGISTRATION_OK = "local.message.registration_ok";
	private static final String ERROR_BUSY_LOGIN = "local.error.login_is_busy";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AbstractUserInfoBuilder builder = new UserInfoBuilder();
		builder.buildUserInfo(request);
		UserInfo userInfo = builder.getUserInfo();

		ServiceFactory factory = ServiceFactory.getInstance();
		UserService userService = factory.getUserService();

		HttpSession session = request.getSession(true);
		try {
			Integer id = userService.registration(userInfo);
			session.setAttribute(ATTRIBUTE_INFO_MESSAGE, MESSAGE_REGISTRATION_OK);
			session.setAttribute(ATTRIBUTE_PAGE, GO_TO_CLIENT_DATA_PAGE + String.valueOf(id));
			response.sendRedirect(GO_TO_CLIENT_DATA_PAGE + String.valueOf(id));
		} catch (UserInfoFormatServiceException e) {
			logger.error("wrong user info format", e);
			session.setAttribute(ATTRIBUTE_ERROR_MESSAGE, e.getErrorDescription());
			response.sendRedirect(GO_TO_REGISTRATION_PAGE);
		} catch (BusyLoginServiceException e) {
			logger.error("login is busy", e);
			session.setAttribute(ATTRIBUTE_ERROR_MESSAGE, Arrays.asList(ERROR_BUSY_LOGIN));
			response.sendRedirect(GO_TO_REGISTRATION_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GO_TO_ERROR_PAGE);
		}
	}

}
