package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

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
import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.UserService;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

/**
 * The command is responsible for the login
 * 
 * @author Aleksandra Podgayskaya
 * @see Command
 */
public class LoginCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Execute the command to login
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
		UserInfo loginationInfo = builder.getUserInfo();

		ServiceFactory factory = ServiceFactory.getInstance();
		UserService userService = factory.getUserService();

		HttpSession session = request.getSession(true);

		if (session.getAttribute(ParameterConstraint.USER_LOGIN) != null) {
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Message.ERROR_REPEATED_LOGIN);
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return;
		}

		try {
			User user = userService.authorization(loginationInfo);

			session.setAttribute(ParameterConstraint.USER_ID, user.getId());
			session.setAttribute(ParameterConstraint.USER_LOGIN, user.getLogin());
			session.setAttribute(ParameterConstraint.USER_TYPE, user.getType());
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.MAIN_PAGE);
			response.sendRedirect(GoToPage.MAIN_PAGE);

		} catch (NoSuchUserServiceException e) {
			logger.error("wrong login and password", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Message.ERROR_NO_SUCH_USER);
			response.sendRedirect(GoToPage.INDEX_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
