package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.command.CommandException;
import by.epam.payment_system.controller.util.GoToPage;
import by.epam.payment_system.controller.util.OperationControl;
import by.epam.payment_system.controller.util.SessionControl;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.CardStatus;
import by.epam.payment_system.entity.CardType;
import by.epam.payment_system.entity.Currency;
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

/**
 * The command is responsible for opening the main card
 * 
 * @author Aleksandra Podgayskaya
 * @see Command
 */
public class OpenMainCardCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Execute the command to open the main card
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

		String cardTypeId = request.getParameter(ParameterConstraint.CARD_TYPE_ID);
		String currency = request.getParameter(ParameterConstraint.CURRENCY);
		Long userId = (Long) session.getAttribute(ParameterConstraint.USER_ID);

		ServiceFactory factory = ServiceFactory.getInstance();
		CardService cardService = factory.getCardService();

		try {
			if (cardTypeId == null || currency == null) {
				throw new CommandException("not data for opening a card");
			}
			CardType cardType = new CardType(Long.valueOf(cardTypeId));
			Card card = new Card(cardType, CardStatus.MAIN, userId, Currency.valueOf(currency));
			cardService.openMainCard(card);
			session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_CARD_IS_OPEN);
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.MAIN_PAGE);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (IllegalArgumentException | CommandException | ImpossibleOperationServiceException e) {
			logger.error("incorrect data for opening a card", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Message.ERROR_INCORRECT_DATA);
			response.sendRedirect(GoToPage.OPEN_CARD_PAGE);
		} catch (NoSuchUserServiceException e) {
			logger.error("client profile is not completed", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Message.ERROR_PROFILE_NOT_COMPLETED);
			response.sendRedirect(GoToPage.OPEN_CARD_PAGE);
		} catch (ServiceException e) {
			logger.error("general system error", e);
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}

	}

}
