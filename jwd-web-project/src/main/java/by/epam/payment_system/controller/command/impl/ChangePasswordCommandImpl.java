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
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.UserService;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;
import by.epam.payment_system.service.exception.WrongPasswordServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

/**
 * The command is responsible for changing the password
 * 
 * @author Aleksandra Podgayskaya
 * @see Command
 */
public class ChangePasswordCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Execute the command to change the password
	 * 
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (!SessionControl.isExist(request, response)
				|| !OperationControl.isAllowedToUser(request, response, UserType.CLIENT)) {
			return;
		}

		HttpSession session = request.getSession(true);

		String newPassword = request.getParameter(ParameterConstraint.PASSWORD);
		String passwordRepeat = request.getParameter(ParameterConstraint.PASSWORD_REPEAT);
		String passwordCheck = request.getParameter(ParameterConstraint.PASSWORD_CHECK);
		Integer userId = (Integer) session.getAttribute(ParameterConstraint.USER_ID);
		String userLogin = (String) session.getAttribute(ParameterConstraint.USER_LOGIN);

		if (newPassword != null) {
			if (!newPassword.equals(passwordRepeat)) {
				logger.info("password repeat error");
				session.setAttribute(ParameterConstraint.ERROR_MESSAGE,
						Arrays.asList(Message.ERROR_INCORRECT_PASSWORD_REPEAT));
				response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
				return;
			}
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		UserService userService = factory.getUserService();

		UserInfo userInfo = new UserInfo(userId, userLogin, passwordCheck);
		try {
			userService.changePassword(userInfo, newPassword);
			session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_PASSWORD_CHANGED);
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
		} catch (WrongPasswordServiceException e) {
			logger.error("wrong password for confirmation", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_WRONG_PASSWORD));
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
		} catch (UserInfoFormatServiceException e) {
			logger.error("wrong new password format", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_PASSWORD));
			response.sendRedirect(GoToPage.EDIT_PROFILE_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
