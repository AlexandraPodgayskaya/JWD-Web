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
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.CardTypeFormatException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public class AddCardTypeCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	private static final String IMAGE_FOLDER = "img/";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (!SessionControl.isExist(request, response) || !OperationControl.calledAdmin(request, response)) {
			return;
		}

		String cardType = request.getParameter(ParameterConstraint.CARD_TYPE);

		String imagePath = null;
		if (request.getParameter(ParameterConstraint.IMAGE) != null) {
			imagePath = IMAGE_FOLDER + request.getParameter(ParameterConstraint.IMAGE);
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		CardService cardService = factory.getCardService();

		HttpSession session = request.getSession(true);
		try {
			cardService.addCardType(cardType, imagePath);
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
