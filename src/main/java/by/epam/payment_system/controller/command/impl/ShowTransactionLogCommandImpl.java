package by.epam.payment_system.controller.command.impl;

import java.io.IOException;
import java.util.Arrays;
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
import by.epam.payment_system.controller.util.URIConstructor;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.TransactionService;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public class ShowTransactionLogCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	private static final String COMMAND_SHOW_ACCOUNT_LOG = "show_account_log";
	private static final String COMMAND_SHOW_CARD_LOG = "show_card_log";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.info("session timed out");
			session = request.getSession(true);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Message.ERROR_SESSION_TIMED_OUT);
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return;
		}

		if (session.getAttribute(ParameterConstraint.USER_LOGIN) == null) {
			logger.info("there was log out");
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Message.ERROR_LOGOUT);
			response.sendRedirect(GoToPage.INDEX_PAGE);
			return;
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		TransactionService transactionService = factory.getTransactionService();

		try {
			List<Transaction> transactionList;
			if (request.getParameter(ParameterConstraint.COMMAND) != null
					&& request.getParameter(ParameterConstraint.COMMAND).equals(COMMAND_SHOW_CARD_LOG)) {
				transactionList = transactionService.takeCardTransactions(request.getParameter(ParameterConstraint.NUMBER_CARD));

			} else if (request.getParameter(ParameterConstraint.COMMAND) != null
					&& request.getParameter(ParameterConstraint.COMMAND).equals(COMMAND_SHOW_ACCOUNT_LOG)) {
				transactionList = transactionService
						.takeAccountTransactions(request.getParameter(ParameterConstraint.NUMBER_CARD));
			} else {
				logger.info("incorrect command name");
				session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_IMPOSSIBLE_OPERATION));
				response.sendRedirect(GoToPage.MAIN_PAGE);
				return;
			}

			if (transactionList.isEmpty()) {
				logger.info("transaction log is empty");
				session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_LOG_IS_EMPTY);
				response.sendRedirect(GoToPage.MAIN_PAGE);
				return;
			}

			request.setAttribute(ParameterConstraint.TRANSACTION_LIST, transactionList);
			request.setAttribute(ParameterConstraint.COMMAND, request.getParameter(ParameterConstraint.COMMAND));
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.TRANSACTION_LOG_PAGE + request.getParameter(ParameterConstraint.COMMAND)
					+ URIConstructor.SET_NUMBER_CARD + request.getParameter(ParameterConstraint.NUMBER_CARD));
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_TRANSACTION_LOG_PAGE);
			requestDispatcher.forward(request, response);
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
