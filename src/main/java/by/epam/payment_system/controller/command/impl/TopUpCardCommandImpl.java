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
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.TransactionService;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.TransactionDataServiceException;

public class TopUpCardCommandImpl implements Command {

	private static final Logger logger = LogManager.getLogger();
	
	private static final String GO_TO_INDEX_PAGE = "index.jsp";
	private static final String GO_TO_MAIN_PAGE = "Controller?command=go_to_main_page";
	private static final String GO_TO_TOP_UP_CARD_PAGE = "Controller?command=go_to_top_up_card_page";
	private static final String GO_TO_ERROR_PAGE = "error.jsp";
	private static final String ATTRIBUTE_USER_TYPE = "userType";
	private static final String ATTRIBUTE_ERROR_MESSAGE = "errorMessageList";
	private static final String ATTRIBUTE_INFO_MESSAGE = "infoMessage";
	private static final String ATTRIBUTE_PAGE = "page";
	private static final String CURRENCY = "currency";
	private static final String RECIPIENT_CARD_NUMBER = "recipientCardNumber";
	private static final String MESSAGE_TOP_UP_CARD_OK = "local.message.top_up_card_ok";
	private static final String ERROR_IPOSSIBLE_OPERATION = "local.error.impossible_operation";
	private static final String SET_PARAMETER_CURRENCY = "&currency=";
	private static final String SET_PARAMETER_NUMBER_CARD = "&numberCard=";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.info("session aborted");
			response.sendRedirect(GO_TO_INDEX_PAGE);
			return;
		}

		if (session.getAttribute(ATTRIBUTE_USER_TYPE) == UserType.ADMIN) {
			logger.info("impossible operation for " + UserType.ADMIN);
			session.setAttribute(ATTRIBUTE_ERROR_MESSAGE, Arrays.asList(ERROR_IPOSSIBLE_OPERATION));
			response.sendRedirect(GO_TO_MAIN_PAGE); 
			return;
		}

		Enumeration<String> parameters = request.getParameterNames();
		Map<String, String> transferDetails = new HashMap<>();

		for (String parameter : Collections.list(parameters)) {
			transferDetails.put(parameter, request.getParameter(parameter));
		}

		ServiceFactory factory = ServiceFactory.getInstance();
		TransactionService transactionService = factory.getTransactionService();
		try {
			transactionService.topUpCard(transferDetails);
			session.setAttribute(ATTRIBUTE_PAGE, GO_TO_MAIN_PAGE);
			session.setAttribute(ATTRIBUTE_INFO_MESSAGE, MESSAGE_TOP_UP_CARD_OK);
			response.sendRedirect(GO_TO_MAIN_PAGE);
		} catch (TransactionDataServiceException e) {
			logger.error(e.getMessage());
			session.setAttribute(ATTRIBUTE_ERROR_MESSAGE, e.getErrorDescription());
			response.sendRedirect(GO_TO_TOP_UP_CARD_PAGE + SET_PARAMETER_NUMBER_CARD
					+ transferDetails.get(RECIPIENT_CARD_NUMBER) + SET_PARAMETER_CURRENCY + transferDetails.get(CURRENCY));
		} catch (ImpossibleOperationServiceException e) {
			logger.error(e.getMessage());
			session.setAttribute(ATTRIBUTE_ERROR_MESSAGE, Arrays.asList(ERROR_IPOSSIBLE_OPERATION));
			response.sendRedirect(GO_TO_MAIN_PAGE);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			response.sendRedirect(GO_TO_ERROR_PAGE);
		}

	}

}
