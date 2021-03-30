package by.epam.payment_system.controller.command.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.service.CardService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.ServiceException;

public class GoToMainPageCommandImpl implements Command {
	
	private static final Logger logger = LogManager.getLogger();

	private static final String GO_TO_INDEX_PAGE = "index.jsp";
	private static final String GO_TO_MAIN_PAGE = "/WEB-INF/jsp/main.jsp";
	private static final String GO_TO_ERROR_PAGE = "error.jsp";
	private static final String ATTRIBUTE_USER_ID = "userId";
	private static final String ATTRIBUTE_CARD_LIST = "cardList";
	private static final String ATTRIBUTE_PAGE = "page";
	private static final String MAIN_PAGE = "Controller?command=go_to_main_page";

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
		
		List<Card> cardList;
		
		try {
			cardList = cardService.takeCards((Integer)session.getAttribute(ATTRIBUTE_USER_ID));
			while (cardList.contains(null)) {
				cardList.remove(null);
			}
			request.setAttribute(ATTRIBUTE_CARD_LIST, cardList);
			session.setAttribute(ATTRIBUTE_PAGE, MAIN_PAGE);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GO_TO_MAIN_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			response.sendRedirect(GO_TO_ERROR_PAGE);
		}

	}

}
