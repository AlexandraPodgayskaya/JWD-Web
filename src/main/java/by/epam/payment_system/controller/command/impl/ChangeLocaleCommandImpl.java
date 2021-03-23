package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epam.payment_system.controller.command.Command;

public class ChangeLocaleCommandImpl implements Command {

	private static final String PARAMETER_COMMAND = "command";
	private static final String PAGE = "page";
	private static final String INDEX_PAGE = "index.jsp";
	private static final String ATTRIBUTE_LOCALE = "locale";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		session.setAttribute(ATTRIBUTE_LOCALE, request.getParameter(PARAMETER_COMMAND));

		String page;

		if (request.getParameter(PAGE) != null) {
			page = request.getParameter(PAGE);
		} else if (session.getAttribute(PAGE) != null) {
			page = (String) session.getAttribute(PAGE);
		} else {
			page = INDEX_PAGE;
		}

		response.sendRedirect(page);
	}

}
