package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.util.GoToPage;

/**
 * Default command for incorrect command name
 * 
 * @author Aleksandra Podgayskaya
 * @see Command
 */
public class DefaultCommandImpl implements Command {

	/**
	 * Go to the error page
	 * 
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.sendRedirect(GoToPage.ERROR_PAGE);

	}

}
