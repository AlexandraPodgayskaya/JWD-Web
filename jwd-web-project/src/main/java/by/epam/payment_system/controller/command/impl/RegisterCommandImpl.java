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
import by.epam.payment_system.controller.util.GoToPage;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.UserService;
import by.epam.payment_system.service.exception.BusyLoginServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

/**
 * The command is responsible for registration
 * 
 * @author Aleksandra Podgayskaya
 * @see Command
 */
public class RegisterCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Execute the command to register
	 * 
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @throws ServletException
	 * @throws IOException
	 */
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
			session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_REGISTRATION_OK);
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.CLIENT_DATA_PAGE + String.valueOf(id));
			response.sendRedirect(GoToPage.CLIENT_DATA_PAGE + String.valueOf(id));
		} catch (UserInfoFormatServiceException e) {
			logger.error("wrong user info format", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, e.getErrorDescription());
			response.sendRedirect(GoToPage.REGISTRATION_PAGE);
		} catch (BusyLoginServiceException e) {
			logger.error("login is busy", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_BUSY_LOGIN));
			response.sendRedirect(GoToPage.REGISTRATION_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}
	}

}
