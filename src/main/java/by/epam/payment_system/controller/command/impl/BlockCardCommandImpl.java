package by.epam.payment_system.controller.command.impl;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.ServiceException;

public class BlockCardCommandImpl implements Command {
	
	private static final Logger logger = LogManager.getLogger();
	
	private static final String GO_TO_INDEX_PAGE = "index.jsp";
	private static final String GO_TO_ERROR_PAGE = "error.jsp";
	private static final String GO_TO_MAIN_PAGE = "Controller?command=go_to_main_page";
	private static final String PARAMETR_NUMBER_CARD = "numberCard";
	private static final String ATTRIBUTE_INFO_MESSAGE = "infoMessage";
	private static final String ATTRIBUTE_ERROR_MESSAGE = "errorMessageList";
	private static final String MESSAGE_BLOCKING_OK = "local.message.blocking_ok";
	private static final String ERROR_IMPOSSIBLE_OPERATION = "local.error.impossible_operation";


	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.info("session aborted");
			response.sendRedirect(GO_TO_INDEX_PAGE);
			return;
		}
		
		ServiceFactory factory = ServiceFactory.getInstance();
		CardService cardService = factory.getCardService();
		
		String numberCard = request.getParameter(PARAMETR_NUMBER_CARD);
		
		try {
			cardService.blockCard(numberCard);
			session.setAttribute(ATTRIBUTE_INFO_MESSAGE, MESSAGE_BLOCKING_OK);
			response.sendRedirect(GO_TO_MAIN_PAGE);
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
