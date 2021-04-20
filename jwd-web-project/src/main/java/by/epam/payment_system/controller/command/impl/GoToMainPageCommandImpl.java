package by.epam.payment_system.controller.command.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.util.GoToPage;
import by.epam.payment_system.controller.util.SessionControl;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.util.ParameterConstraint;

/**
 * The command is responsible for going to the main page
 * 
 * @author Aleksandra Podgayskaya
 * @see Command
 */
public class GoToMainPageCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Execute the command to go to the main page
	 * 
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (!SessionControl.isExist(request, response)) {
			return;
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		CardService cardService = factory.getCardService();

		List<Card> cardList;
		Long id = null;

		HttpSession session = request.getSession(true);
		if (session.getAttribute(ParameterConstraint.USER_TYPE) == UserType.ADMIN
				&& session.getAttribute(ParameterConstraint.FOUND_CLIENT_INFO) != null) {
			UserInfo userInfo = (UserInfo) session.getAttribute(ParameterConstraint.FOUND_CLIENT_INFO);
			id = userInfo.getId();
		} else if (session.getAttribute(ParameterConstraint.USER_TYPE) == UserType.CLIENT) {
			id = (Long) session.getAttribute(ParameterConstraint.USER_ID);
		}

		try {
			cardList = id != null ? cardService.takeCards(id) : Collections.emptyList();
			request.setAttribute(ParameterConstraint.CARD_LIST, cardList);
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.MAIN_PAGE);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_MAIN_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
