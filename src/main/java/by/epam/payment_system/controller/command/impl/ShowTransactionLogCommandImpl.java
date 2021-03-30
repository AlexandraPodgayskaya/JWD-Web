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
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.TransactionService;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.ServiceException;

public class ShowTransactionLogCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	private static final String GO_TO_INDEX_PAGE = "index.jsp";
	private static final String GO_TO_ERROR_PAGE = "error.jsp";
	private static final String GO_TO_MAIN_PAGE = "Controller?command=go_to_main_page";
	private static final String GO_TO_TRANSACTION_LOG_PAGE = "/WEB-INF/jsp/transaction_log.jsp";
	private static final String THIS_PAGE = "Controller?command=";
	private static final String PARAMETER_COMMAND = "command";
	private static final String PARAMETER_NUMBER_CARD = "numberCard";
	private static final String ATTRIBUTE_ERROR_MESSAGE = "errorMessageList";
	private static final String ATTRIBUTE_TRANSACTION_LIST = "transactionList";
	private static final String ATTRIBUTE_PAGE = "page";
	private static final String ATTRIBUTE_COMMAND = "command";
	private static final String ATTRIBUTE_INFO_MESSAGE = "infoMessage";
	private static final String MESSAGE_LOG_IS_EMPTY = "local.message.log_is_empty";
	private static final String ERROR_IMPOSSIBLE_OPERATION = "local.error.impossible_operation";
	private static final String SET_PARAMETER_NUMBER_CARD = "&numberCard=";
	private static final String COMMAND_SHOW_ACCOUNT_LOG = "show_account_log";
	private static final String COMMAND_SHOW_CARD_LOG = "show_card_log";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.info("session aborted");
			response.sendRedirect(GO_TO_INDEX_PAGE);
			return;
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		TransactionService transactionService = factory.getTransactionService();

		try {
			List<Transaction> transactionList;
			if (request.getParameter(PARAMETER_COMMAND) != null
					&& request.getParameter(PARAMETER_COMMAND).equals(COMMAND_SHOW_CARD_LOG)) {
				transactionList = transactionService.takeCardTransactions(request.getParameter(PARAMETER_NUMBER_CARD));

			} else if (request.getParameter(PARAMETER_COMMAND) != null
					&& request.getParameter(PARAMETER_COMMAND).equals(COMMAND_SHOW_ACCOUNT_LOG)) {
				transactionList = transactionService
						.takeAccountTransactions(request.getParameter(PARAMETER_NUMBER_CARD));
			} else {
				logger.info("incorrect command name");
				session.setAttribute(ATTRIBUTE_ERROR_MESSAGE, Arrays.asList(ERROR_IMPOSSIBLE_OPERATION));
				response.sendRedirect(GO_TO_MAIN_PAGE);
				return;
			}

			if (transactionList.isEmpty()) {
				logger.info("transaction log is empty");
				session.setAttribute(ATTRIBUTE_INFO_MESSAGE, MESSAGE_LOG_IS_EMPTY);
				response.sendRedirect(GO_TO_MAIN_PAGE);
				return;
			}
			
			while (transactionList.contains(null)) {
				transactionList.remove(null);
			}

			request.setAttribute(ATTRIBUTE_TRANSACTION_LIST, transactionList);
			request.setAttribute(ATTRIBUTE_COMMAND, request.getParameter(PARAMETER_COMMAND));
			session.setAttribute(ATTRIBUTE_PAGE, THIS_PAGE + request.getParameter(PARAMETER_COMMAND)
					+ SET_PARAMETER_NUMBER_CARD + request.getParameter(PARAMETER_NUMBER_CARD));
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GO_TO_TRANSACTION_LOG_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ImpossibleOperationServiceException e) {
			logger.error(e.getMessage());
			session.setAttribute(ATTRIBUTE_ERROR_MESSAGE, Arrays.asList(ERROR_IMPOSSIBLE_OPERATION));
			response.sendRedirect(GO_TO_MAIN_PAGE);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			response.sendRedirect(GO_TO_ERROR_PAGE);
		}

	}

}
