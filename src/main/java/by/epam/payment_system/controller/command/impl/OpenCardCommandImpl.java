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
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.CardTypeFormatException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public class OpenCardCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (!SessionControl.isExist(request, response) || !OperationControl.calledClient(request, response)) {
			return;
		}

		String cardStatus = request.getParameter(ParameterConstraint.CARD_STATUS);
		String cardTypeId = request.getParameter(ParameterConstraint.CARD_TYPE_ID);
		String currency = request.getParameter(ParameterConstraint.CARD_TYPE_ID);
		String numberAccount = request.getParameter(ParameterConstraint.NUMBER_ACCOUNT);
		String numberPassport = request.getParameter(ParameterConstraint.PERSONAL_NUMBER_PASSPORT);

		ServiceFactory factory = ServiceFactory.getInstance();
		CardService cardService = factory.getCardService();

		HttpSession session = request.getSession(true);
		try {

			cardService.addCardType(cardStatus, cardTypeId);
			session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_CARD_TYPE_ADDED);
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.MAIN_PAGE);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (CardTypeFormatException e) {
			logger.error("wrong card type format", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_CARD_TYPE));
			response.sendRedirect(GoToPage.ADD_CARD_TYPE_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
