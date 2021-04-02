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
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.ServiceException;

public class CloseCardCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	private static final String MESSAGE_CLOSING_OK = "local.message.closing_card_ok";
	private static final String ERROR_IMPOSSIBLE_OPERATION = "local.error.impossible_operation";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null) {
			logger.info("session aborted");
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return;
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		CardService cardService = factory.getCardService();

		String numberCard = request.getParameter(Parameter.NUMBER_CARD);

		try {
			cardService.closeCard(numberCard);
			session.setAttribute(Attribute.INFO_MESSAGE, MESSAGE_CLOSING_OK);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (ImpossibleOperationServiceException e) {
			logger.error("impossible operation", e);
			session.setAttribute(Attribute.ERROR_MESSAGE, Arrays.asList(ERROR_IMPOSSIBLE_OPERATION));
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
