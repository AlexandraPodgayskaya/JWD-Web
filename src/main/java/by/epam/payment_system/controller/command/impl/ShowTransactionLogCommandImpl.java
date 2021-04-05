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

import by.epam.payment_system.controller.command.Attribute;
import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.command.GoToPage;
import by.epam.payment_system.controller.command.Parameter;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.TransactionService;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.ServiceException;

public class ShowTransactionLogCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	private static final String MESSAGE_LOG_IS_EMPTY = "local.message.log_is_empty";
	private static final String ERROR_SESSION_TIMED_OUT = "local.error.session_timed_out";
	private static final String ERROR_LOGOUT = "local.error.logout";
	private static final String ERROR_IMPOSSIBLE_OPERATION = "local.error.impossible_operation";
	private static final String COMMAND_SHOW_ACCOUNT_LOG = "show_account_log";
	private static final String COMMAND_SHOW_CARD_LOG = "show_card_log";

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

		ServiceFactory factory = ServiceFactory.getInstance();
		TransactionService transactionService = factory.getTransactionService();

		try {
			List<Transaction> transactionList;
			if (request.getParameter(Parameter.COMMAND) != null
					&& request.getParameter(Parameter.COMMAND).equals(COMMAND_SHOW_CARD_LOG)) {
				transactionList = transactionService.takeCardTransactions(request.getParameter(Parameter.NUMBER_CARD));

			} else if (request.getParameter(Parameter.COMMAND) != null
					&& request.getParameter(Parameter.COMMAND).equals(COMMAND_SHOW_ACCOUNT_LOG)) {
				transactionList = transactionService
						.takeAccountTransactions(request.getParameter(Parameter.NUMBER_CARD));
			} else {
				logger.info("incorrect command name");
				session.setAttribute(Attribute.ERROR_MESSAGE, Arrays.asList(ERROR_IMPOSSIBLE_OPERATION));
				response.sendRedirect(GoToPage.MAIN_PAGE);
				return;
			}

			if (transactionList.isEmpty()) {
				logger.info("transaction log is empty");
				session.setAttribute(Attribute.INFO_MESSAGE, MESSAGE_LOG_IS_EMPTY);
				response.sendRedirect(GoToPage.MAIN_PAGE);
				return;
			}

			request.setAttribute(Attribute.TRANSACTION_LIST, transactionList);
			request.setAttribute(Attribute.COMMAND, request.getParameter(Parameter.COMMAND));
			session.setAttribute(Attribute.PAGE, GoToPage.TRANSACTION_LOG_PAGE + request.getParameter(Parameter.COMMAND)
					+ Parameter.SET_NUMBER_CARD + request.getParameter(Parameter.NUMBER_CARD));
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_TRANSACTION_LOG_PAGE);
			requestDispatcher.forward(request, response);
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
