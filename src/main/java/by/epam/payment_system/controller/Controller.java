package by.epam.payment_system.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.command.CommandProvider;

public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String PARAMETER_COMMAND = "command";
	private static final String GO_TO_ERROR_PAGE = "error.jsp";

	private final CommandProvider provider = new CommandProvider();

	public Controller() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name;
		Command command;

		name = request.getParameter(PARAMETER_COMMAND);
		System.out.println(name);

		if (name != null) {
			command = provider.takeCommand(name);
			command.execute(request, response);
		} else {
			response.sendRedirect(GO_TO_ERROR_PAGE);
		}
	}
}
