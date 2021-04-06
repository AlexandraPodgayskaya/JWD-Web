package by.epam.payment_system.controller.command.impl;

import java.io.IOException;
import java.util.Arrays;

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
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.UserService;
import by.epam.payment_system.service.exception.BusyLoginServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;
import by.epam.payment_system.service.exception.WrongPasswordServiceException;

public class ChangeLoginCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	private static final String MESSAGE_LOGIN_CHANGED = "local.message.login_changed";
	private static final String ERROR_SESSION_TIMED_OUT = "local.error.session_timed_out";
	private static final String ERROR_LOGOUT = "local.error.logout";
	private static final String ERROR_LOGIN_NOT_CHANGED = "local.error.login_not_changed";
	private static final String ERROR_WRONG_PASSWORD = "local.error.wrong_password";
	private static final String ERROR_LOGIN = "local.error.login";
	private static final String ERROR_BUSY_LOGIN = "local.error.login_is_busy";

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

		String newLogin = request.getParameter(Parameter.LOGIN);
		Integer userId = (Integer) session.getAttribute(Attribute.USER_ID);
		String userLogin = (String) session.getAttribute(Attribute.USER_LOGIN);
		String password = request.getParameter(Parameter.PASSWORD_CHECK);
		
		if (newLogin != null  &&  newLogin.equals(userLogin)) {
			logger.info("login not changed");
			session.setAttribute(Attribute.ERROR_MESSAGE, Arrays.asList(ERROR_LOGIN_NOT_CHANGED));
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
			return;
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		UserService userService = factory.getUserService();

		UserInfo userInfo = new UserInfo(userId, userLogin, password);
		try {
			userService.changeLogin(userInfo, newLogin);
			session.setAttribute(Attribute.INFO_MESSAGE, MESSAGE_LOGIN_CHANGED);
			session.setAttribute(Attribute.USER_LOGIN, newLogin);
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
		} catch (WrongPasswordServiceException e) {
			logger.error("wrong password", e);
			session.setAttribute(Attribute.ERROR_MESSAGE, Arrays.asList(ERROR_WRONG_PASSWORD));
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
		} catch (UserInfoFormatServiceException e) {
			logger.error("wrong login format", e);
			session.setAttribute(Attribute.ERROR_MESSAGE, Arrays.asList(ERROR_LOGIN));
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
		} catch (BusyLoginServiceException e) {
			logger.error("login is busy", e);
			session.setAttribute(Attribute.ERROR_MESSAGE, Arrays.asList(ERROR_BUSY_LOGIN));
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
