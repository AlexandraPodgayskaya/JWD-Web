package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epam.payment_system.controller.command.Attribute;
import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.command.GoToPage;
import by.epam.payment_system.controller.command.Parameter;

public class ChangeLocaleCommandImpl implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		session.setAttribute(Attribute.LOCALE, request.getParameter(Parameter.COMMAND));

		String page;

		if (request.getParameter(Parameter.PAGE) != null) {
			page = request.getParameter(Parameter.PAGE);
		} else if (session.getAttribute(Attribute.PAGE) != null) {
			page = (String) session.getAttribute(Attribute.PAGE);
		} else {
			page = GoToPage.INDEX_PAGE;
		}

		response.sendRedirect(page);
	}

}
