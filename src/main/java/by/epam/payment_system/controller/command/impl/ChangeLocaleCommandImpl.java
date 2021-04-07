package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.util.GoToPage;
import by.epam.payment_system.util.ParameterConstraint;

public class ChangeLocaleCommandImpl implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		session.setAttribute(ParameterConstraint.LOCALE, request.getParameter(ParameterConstraint.COMMAND));

		String page;

		if (request.getParameter(ParameterConstraint.PAGE) != null) {
			page = request.getParameter(ParameterConstraint.PAGE);
		} else if (session.getAttribute(ParameterConstraint.PAGE) != null) {
			page = (String) session.getAttribute(ParameterConstraint.PAGE);
		} else {
			page = GoToPage.INDEX_PAGE;
		}

		response.sendRedirect(page);
	}

}
