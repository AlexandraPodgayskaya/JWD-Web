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
import by.epam.payment_system.controller.util.SessionControl;
import by.epam.payment_system.controller.util.URIConstructor;
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.TransactionService;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.NotEnoughMoneyServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.TransactionDataServiceException;
import by.epam.payment_system.service.exception.WrongPasswordServiceException;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

public class MakePaymentCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (!SessionControl.isExist(request, response)) {
			return;
		}

		HttpSession session = request.getSession(true);
		if (session.getAttribute(ParameterConstraint.USER_TYPE) == UserType.ADMIN) {
			logger.info("impossible operation for " + UserType.ADMIN);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_IMPOSSIBLE_OPERATION));
			response.sendRedirect(GoToPage.MAIN_PAGE);
			return;
		}

		Enumeration<String> parameters = request.getParameterNames();
		Map<String, String> paymentDetails = new HashMap<>();

		for (String parameter : Collections.list(parameters)) {
			paymentDetails.put(parameter, request.getParameter(parameter));
		}

		paymentDetails.put(ParameterConstraint.USER_LOGIN, (String) session.getAttribute(ParameterConstraint.USER_LOGIN));

		ServiceFactory factory = ServiceFactory.getInstance();
		TransactionService transactionService = factory.getTransactionService();
		try {
			transactionService.makePayment(paymentDetails);
			session.setAttribute(ParameterConstraint.PAGE, GoToPage.MAIN_PAGE);
			session.setAttribute(ParameterConstraint.INFO_MESSAGE, Message.INFO_PAYMENT_OK);
			response.sendRedirect(GoToPage.MAIN_PAGE);
		} catch (WrongPasswordServiceException e) {
			logger.error("wrong password", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_WRONG_PASSWORD));
			response.sendRedirect(GoToPage.PAYMENT_PAGE + URIConstructor.SET_NUMBER_CARD
					+ paymentDetails.get(ParameterConstraint.SENDER_CARD_NUMBER) + URIConstructor.SET_CURRENCY
					+ paymentDetails.get(ParameterConstraint.CURRENCY) + URIConstructor.SET_BALANCE
					+ paymentDetails.get(ParameterConstraint.BALANCE));
		} catch (TransactionDataServiceException e) {
			logger.error("incorrect data for payment", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, e.getErrorDescription());
			response.sendRedirect(GoToPage.PAYMENT_PAGE + URIConstructor.SET_NUMBER_CARD
					+ paymentDetails.get(ParameterConstraint.SENDER_CARD_NUMBER) + URIConstructor.SET_CURRENCY
					+ paymentDetails.get(ParameterConstraint.CURRENCY) + URIConstructor.SET_BALANCE
					+ paymentDetails.get(ParameterConstraint.BALANCE));
		} catch (NotEnoughMoneyServiceException e) {
			logger.error("not enough money for payment", e);
			session.setAttribute(ParameterConstraint.ERROR_MESSAGE, Arrays.asList(Message.ERROR_NOT_ENOUGH_MONEY));
			response.sendRedirect(GoToPage.PAYMENT_PAGE + URIConstructor.SET_NUMBER_CARD
					+ paymentDetails.get(ParameterConstraint.SENDER_CARD_NUMBER) + URIConstructor.SET_CURRENCY
					+ paymentDetails.get(ParameterConstraint.CURRENCY) + URIConstructor.SET_BALANCE
					+ paymentDetails.get(ParameterConstraint.BALANCE));
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
