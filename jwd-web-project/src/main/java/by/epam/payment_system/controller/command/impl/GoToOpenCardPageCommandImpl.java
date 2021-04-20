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
import by.epam.payment_system.entity.CardInfo;
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.util.ParameterConstraint;

/**
 * The command is responsible for going to the open card page
 * 
 * @author Aleksandra Podgayskaya
 * @see Command
 */
public class GoToOpenCardPageCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Execute the command to go to the open card page
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

		ServiceFactory factory = ServiceFactory.getInstance();
		CardService cardService = factory.getCardService();
		Long id = (Long) session.getAttribute(ParameterConstraint.USER_ID);

		try {
			CardInfo cardInfo = cardService.takeAllCardOptions(id);
			request.setAttribute(ParameterConstraint.CARD_LIST, cardInfo.getCardList());
			request.setAttribute(ParameterConstraint.CARD_TYPE_LIST, cardInfo.getCardTypeList());
			request.setAttribute(ParameterConstraint.CURRENCY_LIST, cardInfo.getCurrencyList());
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.OPEN_CARD_PAGE);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_OPEN_CARD_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
