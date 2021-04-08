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
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.UserService;
import by.epam.payment_system.service.exception.BusyLoginServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;
import by.epam.payment_system.service.exception.WrongPasswordServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public class ChangeLoginCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (!SessionControl.isExist(request, response) || !OperationControl.calledClient(request, response)) {
			return;
		}

		HttpSession session = request.getSession(true);
		
		String newLogin = request.getParameter(ParameterConstraint.LOGIN);
		Integer userId = (Integer) session.getAttribute(ParameterConstraint.USER_ID);
		String userLogin = (String) session.getAttribute(ParameterConstraint.USER_LOGIN);
		String password = request.getParameter(ParameterConstraint.PASSWORD_CHECK);

		if (newLogin != null && newLogin.equals(userLogin)) {
			logger.info("login not changed");
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_LOGIN_NOT_CHANGED));
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
			return;
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		UserService userService = factory.getUserService();

		UserInfo userInfo = new UserInfo(userId, userLogin, password);
		try {
			userService.changeLogin(userInfo, newLogin);
			session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_LOGIN_CHANGED);
			session.setAttribute(ParameterConstraint.USER_LOGIN, newLogin);
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
		} catch (WrongPasswordServiceException e) {
			logger.error("wrong password", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_WRONG_PASSWORD));
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
		} catch (UserInfoFormatServiceException e) {
			logger.error("wrong login format", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_LOGIN));
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
		} catch (BusyLoginServiceException e) {
			logger.error("login is busy", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_BUSY_LOGIN));
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
