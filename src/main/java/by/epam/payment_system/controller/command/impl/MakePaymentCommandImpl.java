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

import by.epam.payment_system.controller.command.Attribute;
import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.command.GoToPage;
import by.epam.payment_system.controller.command.Parameter;
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.TransactionService;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.NotEnoughMoneyServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.TransactionDataServiceException;
import by.epam.payment_system.service.exception.WrongPasswordServiceException;

public class MakePaymentCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	private static final String MESSAGE_PAYMENT_OK = "local.message.payment_ok";
	private static final String ERROR_SESSION_TIMED_OUT = "local.error.session_timed_out";
	private static final String ERROR_LOGOUT = "local.error.logout";
	private static final String ERROR_IMPOSSIBLE_OPERATION = "local.error.impossible_operation";
	private static final String ERROR_WRONG_PASSWORD = "local.error.wrong_password";
	private static final String ERROR_NOT_ENOUGH_MONEY = "local.error.not_enough_money";

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

		if (session.getAttribute(Attribute.USER_TYPE) == UserType.ADMIN) {
			logger.info("impossible operation for " + UserType.ADMIN);
			session.setAttribute(Attribute.ERROR_MESSAGE, Arrays.asList(ERROR_IMPOSSIBLE_OPERATION));
			response.sendRedirect(GoToPage.MAIN_PAGE);
			return;
		}

		Enumeration<String> parameters = request.getParameterNames();
		Map<String, String> paymentDetails = new HashMap<>();

		for (String parameter : Collections.list(parameters)) {
			paymentDetails.put(parameter, request.getParameter(parameter));
		}

		paymentDetails.put(Attribute.USER_LOGIN, (String) session.getAttribute(Attribute.USER_LOGIN));

		ServiceFactory factory = ServiceFactory.getInstance();
		TransactionService transactionService = factory.getTransactionService();
		try {
			transactionService.makePayment(paymentDetails);
			session.setAttribute(Attribute.PAGE, GoToPage.MAIN_PAGE);
			session.setAttribute(Attribute.INFO_MESSAGE, MESSAGE_PAYMENT_OK);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (WrongPasswordServiceException e) {
			logger.error("wrong password", e);
			session.setAttribute(Attribute.ERROR_MESSAGE, Arrays.asList(ERROR_WRONG_PASSWORD));
			response.sendRedirect(
					GoToPage.PAYMENT_PAGE + Parameter.SET_NUMBER_CARD + paymentDetails.get(Parameter.SENDER_CARD_NUMBER)
							+ Parameter.SET_CURRENCY + paymentDetails.get(Parameter.CURRENCY) + Parameter.SET_BALANCE
							+ paymentDetails.get(Parameter.BALANCE));
		} catch (TransactionDataServiceException e) {
			logger.error("incorrect data for payment", e);
			session.setAttribute(Attribute.ERROR_MESSAGE, e.getErrorDescription());
			response.sendRedirect(
					GoToPage.PAYMENT_PAGE + Parameter.SET_NUMBER_CARD + paymentDetails.get(Parameter.SENDER_CARD_NUMBER)
							+ Parameter.SET_CURRENCY + paymentDetails.get(Parameter.CURRENCY) + Parameter.SET_BALANCE
							+ paymentDetails.get(Parameter.BALANCE));
		} catch (NotEnoughMoneyServiceException e) {
			logger.error("not enough money for payment", e);
			session.setAttribute(Attribute.ERROR_MESSAGE, Arrays.asList(ERROR_NOT_ENOUGH_MONEY));
			response.sendRedirect(
					GoToPage.PAYMENT_PAGE + Parameter.SET_NUMBER_CARD + paymentDetails.get(Parameter.SENDER_CARD_NUMBER)
							+ Parameter.SET_CURRENCY + paymentDetails.get(Parameter.CURRENCY) + Parameter.SET_BALANCE
							+ paymentDetails.get(Parameter.BALANCE));
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
