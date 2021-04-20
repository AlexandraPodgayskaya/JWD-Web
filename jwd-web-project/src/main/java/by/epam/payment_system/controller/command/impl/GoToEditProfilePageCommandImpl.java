package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
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
import by.epam.payment_system.util.ParameterConstraint;

/**
 * The command is responsible for going to the profile page
 * 
 * @author Aleksandra Podgayskaya
 * @see Command
 */
public class GoToEditProfilePageCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Execute the command to go to the profile page
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

		ServiceFactory factory = ServiceFactory.getInstance();
		AdditionalClientDataService additionalClientDataService = factory.getAdditionalClientDataService();

		HttpSession session = request.getSession(true);
		Long userId = (Long) session.getAttribute(ParameterConstraint.USER_ID);
		try {
			UserInfo userInfo = additionalClientDataService.getData(userId);
			request.setAttribute(ParameterConstraint.USER_INFO, userInfo);
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.EDIT_PROFILE_PAGE);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_EDIT_PROFILE_PAGE);
			requestDispatcher.forward(request, response);
		} catch (NoSuchUserServiceException e) {
			request.setAttribute(ParameterConstraint.USER_INFO, new UserInfo());
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.EDIT_PROFILE_PAGE);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_EDIT_PROFILE_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}
	}

}
