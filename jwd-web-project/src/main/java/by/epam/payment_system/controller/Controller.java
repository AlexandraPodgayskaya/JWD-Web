package by.epam.payment_system.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.command.CommandProvider;
import by.epam.payment_system.controller.util.GoToPage;
import by.epam.payment_system.util.ParameterConstraint;

/**
 * Controller receive request from client (get or post)
 * 
 * @author Aleksandra Podgayskaya
 * @see HttpServlet
 */
public class Controller extends HttpServlet {

	private static final Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = 1L;

	private final CommandProvider provider = new CommandProvider();

	public Controller() {
		super();
	}

	/**
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	/**
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	/**
	 * Receive request that redirected from doGet or doPost, then with the help of
	 * ControllerProvider define command and execute it
	 * 
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @throws ServletException
	 * @throws IOException
	 */
	private void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name = request.getParameter(ParameterConstraint.COMMAND);

		if (name != null) {
			Command command = provider.takeCommand(name);
			command.execute(request, response);
		} else {
			logger.error("null command");
			response.sendRedirect(GoToPage.ERROR_PAGE);
		}
	}
}
