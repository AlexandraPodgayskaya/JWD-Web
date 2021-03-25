package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epam.payment_system.controller.command.Command;

public class GoToPaymentPageCommandImpl implements Command {

	private static final String GO_TO_INDEX_PAGE = "index.jsp";
	private static final String GO_TO_PAYMENT_PAGE = "/WEB-INF/jsp/payment.jsp";
	private static final String NUMBER_CARD = "numberCard";
	private static final String CURRENCY = "currency";
	private static final String BALANCE = "balance";
	private static final String THIS_PAGE = "Controller?command=go_to_payment_page";
	private static final String ATTRIBUTE_PAGE = "page";
	private static final String PARAMETER_CURRENCY = "&currency=";
	private static final String PARAMETER_NUMBER_CARD = "&numberCard=";
	private static final String PARAMETER_BALANCE = "&balance=";
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			response.sendRedirect(GO_TO_INDEX_PAGE);
			return;
		}

		request.setAttribute(NUMBER_CARD, request.getParameter(NUMBER_CARD));
		request.setAttribute(CURRENCY, request.getParameter(CURRENCY));
		request.setAttribute(BALANCE, request.getParameter(BALANCE));

		session.setAttribute(ATTRIBUTE_PAGE, THIS_PAGE + PARAMETER_NUMBER_CARD + request.getParameter(NUMBER_CARD)
				+ PARAMETER_CURRENCY + request.getParameter(CURRENCY) + PARAMETER_BALANCE + request.getParameter(BALANCE));

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(GO_TO_PAYMENT_PAGE);
		requestDispatcher.forward(request, response);

	}

}
