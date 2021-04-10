package by.epam.payment_system.controller.command.impl;

import java.io.IOException;
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
import by.epam.payment_system.controller.util.OperationControl;
import by.epam.payment_system.controller.util.SessionControl;
import by.epam.payment_system.controller.util.URIConstructor;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.util.ParameterConstraint;

public class GoToTransferPageCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (!SessionControl.isExist(request, response)
				|| !OperationControl.isAllowedToUser(request, response, UserType.CLIENT)) {
			return;
		}

		HttpSession session = request.getSession(true);
		Integer id = (Integer) session.getAttribute(ParameterConstraint.USER_ID);
		String numberCard = request.getParameter(ParameterConstraint.NUMBER_CARD);
		String numberAccount = request.getParameter(ParameterConstraint.NUMBER_ACCOUNT);
		String currency = request.getParameter(ParameterConstraint.CURRENCY);
		String balance = request.getParameter(ParameterConstraint.BALANCE);

		request.setAttribute(ParameterConstraint.NUMBER_CARD, numberCard);
		request.setAttribute(ParameterConstraint.CURRENCY, currency);
		request.setAttribute(ParameterConstraint.BALANCE, balance);

		session.setAttribute(ParameterConstraint.PAGE,
				GoToPage.TRANSFER_PAGE + URIConstructor.SET_NUMBER_CARD + numberCard + URIConstructor.SET_CURRENCY
						+ currency + URIConstructor.SET_BALANCE + balance + URIConstructor.SET_NUMBER_ACCOUNT
						+ numberAccount);

		ServiceFactory factory = ServiceFactory.getInstance();
		CardService cardService = factory.getCardService();

		try {
			List<Card> cardList = cardService.takeCardsForTransfer(id, numberAccount, currency);
			request.setAttribute(ParameterConstraint.CARD_LIST, cardList);

			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_TRANSFER_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}
	}

}
