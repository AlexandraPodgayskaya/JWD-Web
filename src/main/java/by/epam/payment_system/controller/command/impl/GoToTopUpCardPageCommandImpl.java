package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.command.Command;

public class GoToTopUpCardPageCommandImpl implements Command {
	
	private static final Logger logger = LogManager.getLogger();

	private static final String GO_TO_INDEX_PAGE = "index.jsp";
	private static final String GO_TO_TOP_UP_CARD_PAGE = "/WEB-INF/jsp/top_up_card.jsp";
	private static final String NUMBER_CARD = "numberCard";
	private static final String CURRENCY = "currency";
	private static final String THIS_PAGE = "Controller?command=go_to_top_up_card_page";
	private static final String ATTRIBUTE_PAGE = "page";
	private static final String PARAMETER_CURRENCY = "&currency=";
	private static final String PARAMETER_NUMBER_CARD = "&numberCard=";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.info("session aborted");
			response.sendRedirect(GO_TO_INDEX_PAGE);
			return;
		}

		request.setAttribute(NUMBER_CARD, request.getParameter(NUMBER_CARD));
		request.setAttribute(CURRENCY, request.getParameter(CURRENCY));

		session.setAttribute(ATTRIBUTE_PAGE, THIS_PAGE + PARAMETER_NUMBER_CARD + request.getParameter(NUMBER_CARD)
				+ PARAMETER_CURRENCY + request.getParameter(CURRENCY));

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(GO_TO_TOP_UP_CARD_PAGE);
		requestDispatcher.forward(request, response);
	}
}
