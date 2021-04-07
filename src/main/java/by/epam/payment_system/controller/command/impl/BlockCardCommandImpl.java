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
import by.epam.payment_system.controller.util.SessionControl;
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public class BlockCardCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (!SessionControl.isExist(request, response)) {
			return;
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		CardService cardService = factory.getCardService();

		String numberCard = request.getParameter(ParameterConstraint.NUMBER_CARD);

		HttpSession session = request.getSession(true);
		try {
			cardService.blockCard(numberCard);
			session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_BLOCKING_OK);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (ImpossibleOperationServiceException e) {
			logger.error("impossible operation", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_IMPOSSIBLE_OPERATION));
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
