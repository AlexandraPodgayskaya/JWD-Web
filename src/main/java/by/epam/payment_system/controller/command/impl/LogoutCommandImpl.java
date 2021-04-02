package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.command.GoToPage;

public class LogoutCommandImpl implements Command {
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);

		if (session != null) {
			session.invalidate();
		}
	
		response.sendRedirect(GoToPage.INDEX_PAGE);
	}

}
