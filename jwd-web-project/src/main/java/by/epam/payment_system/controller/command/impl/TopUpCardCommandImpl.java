package by.epam.payment_system.controller.command.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.TransactionService;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.TransactionDataServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

/**
 * The command is responsible for toping up card
 * 
 * @author Aleksandra Podgayskaya
 * @see Command
 */
public class TopUpCardCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Execute the command to top up card
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

		Enumeration<String> parameters = request.getParameterNames();
		Map<String, String> transferDetails = new HashMap<>();

		for (String parameter : Collections.list(parameters)) {
			transferDetails.put(parameter, request.getParameter(parameter));
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		TransactionService transactionService = factory.getTransactionService();

		HttpSession session = request.getSession(true);
		try {
			transactionService.topUpCard(transferDetails);
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.MAIN_PAGE);
			session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_TOP_UP_CARD_OK);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (TransactionDataServiceException e) {
			logger.error("incorrect data for transaction", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, e.getErrorDescription());
			response.sendRedirect((String) session.getAttribute(ParameterConstraint.PAGE));
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
