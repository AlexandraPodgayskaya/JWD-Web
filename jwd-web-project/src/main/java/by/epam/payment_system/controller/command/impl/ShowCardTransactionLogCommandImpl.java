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
import by.epam.payment_system.controller.util.SessionControl;
import by.epam.payment_system.controller.util.URIConstructor;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.TransactionService;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

/**
 * The command is responsible for showing card transaction log
 * 
 * @author Aleksandra Podgayskaya
 * @see Command
 */
public class ShowCardTransactionLogCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Execute the command to show card transaction log
	 * 
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!SessionControl.isExist(request, response)) {
			return;
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		TransactionService transactionService = factory.getTransactionService();

		HttpSession session = request.getSession(true);
		try {
			List<Transaction> transactionList = transactionService
					.takeCardTransactions(request.getParameter(ParameterConstraint.NUMBER_CARD));

			if (transactionList.isEmpty()) {
				logger.info("transaction log is empty");
				session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_LOG_IS_EMPTY);
				response.sendRedirect(GoToPage.MAIN_PAGE);
				return;
			}

			request.setAttribute(ParameterConstraint.TRANSACTION_LIST, transactionList);
			request.setAttribute(ParameterConstraint.COMMAND, request.getParameter(ParameterConstraint.COMMAND));
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.CARD_TRANSACTION_LOG_PAGE
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
