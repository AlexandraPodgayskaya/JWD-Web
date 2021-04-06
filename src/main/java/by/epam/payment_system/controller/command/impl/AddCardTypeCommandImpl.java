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
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.CardTypeFormatException;
import by.epam.payment_system.service.exception.ServiceException;

public class AddCardTypeCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	private static final String MESSAGE_CARD_TYPE_ADDED = "local.message.card_type_added";
	private static final String ERROR_SESSION_TIMED_OUT = "local.error.session_timed_out";
	private static final String ERROR_LOGOUT = "local.error.logout";
	private static final String ERROR_IPOSSIBLE_OPERATION = "local.error.impossible_operation";
	private static final String ERROR_CARD_TYPE = "local.error.card_type";
	private static final String IMAGE_FOLDER = "img/";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.info("session timed out");
			session = request.getSession(true);
			session.setAttribute(Attribute.ERROR_MESSAGE, ERROR_SESSION_TIMED_OUT);
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return;
		}

		if (session.getAttribute(Attribute.USER_LOGIN) == null) {
			logger.info("there was log out");
			session.setAttribute(Attribute.ERROR_MESSAGE, ERROR_LOGOUT);
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return;
		}

		if (session.getAttribute(Attribute.USER_TYPE) == UserType.CLIENT) {
			logger.info("impossible operation for " + UserType.CLIENT);
			session.setAttribute(Attribute.ERROR_MESSAGE, Arrays.asList(ERROR_IPOSSIBLE_OPERATION));
			response.sendRedirect(GoToPage.MAIN_PAGE);
			return;
		}

		String cardType = request.getParameter(Parameter.CARD_TYPE);
		String imagePath = IMAGE_FOLDER + request.getParameter(Parameter.IMAGE);

		ServiceFactory factory = ServiceFactory.getInstance();
		CardService cardService = factory.getCardService();

		try {
			cardService.addCardType(cardType, imagePath);
			session.setAttribute(Attribute.INFO_MESSAGE, MESSAGE_CARD_TYPE_ADDED);
			session.setAttribute(Attribute.PAGE, GoToPage.MAIN_PAGE);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (CardTypeFormatException e) {
			logger.error("wrong card type format", e);
			session.setAttribute(Attribute.ERROR_MESSAGE, Arrays.asList(ERROR_CARD_TYPE));
			response.sendRedirect(GoToPage.ADD_CARD_TYPE_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
