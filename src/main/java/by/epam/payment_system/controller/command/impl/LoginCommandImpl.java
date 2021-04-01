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
import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.UserService;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.ServiceException;

public class LoginCommandImpl implements Command {
	
	private static final Logger logger = LogManager.getLogger();

	private static final String GO_TO_INDEX_PAGE = "index.jsp";
	private static final String GO_TO_MAIN_PAGE = "Controller?command=go_to_main_page";
	private static final String GO_TO_ERROR_PAGE = "error.jsp";
	private static final String ATTRIBUTE_USER_ID = "userId";
	private static final String ATTRIBUTE_USER_TYPE = "userType";
	private static final String ATTRIBUTE_USER_LOGIN = "userLogin";
	private static final String ATTRIBUTE_PAGE = "page";
	private static final String ATTRIBUTE_ERROR_MESSAGE = "errorMessageList";
	private static final String ERROR_NO_SUCH_USER = "local.error.no_such_user";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		AbstractUserInfoBuilder builder = new UserInfoBuilder();
		builder.buildUserInfo(request);
		UserInfo loginationInfo = builder.getUserInfo();

		ServiceFactory factory = ServiceFactory.getInstance();
		UserService userService = factory.getUserService();

		HttpSession session = request.getSession(true);

		try {
			User user = userService.authorization(loginationInfo);

			session.setAttribute(ATTRIBUTE_USER_TYPE, user.getType());
			session.setAttribute(ATTRIBUTE_USER_ID, user.getId());
			session.setAttribute(ATTRIBUTE_USER_LOGIN, user.getLogin());

			session.setAttribute(ATTRIBUTE_PAGE, GO_TO_MAIN_PAGE);
			response.sendRedirect(GO_TO_MAIN_PAGE);

		} catch (NoSuchUserServiceException e) {
			logger.error("wrong login and password", e);
			session.setAttribute(ATTRIBUTE_ERROR_MESSAGE, Arrays.asList(ERROR_NO_SUCH_USER));
			response.sendRedirect(GO_TO_INDEX_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GO_TO_ERROR_PAGE);
		}

	}

}
