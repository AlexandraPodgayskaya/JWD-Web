package by.epam.payment_system.controller.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The interface for different commands
 * 
 * @author Aleksandra Podgayskaya
 */
public interface Command {

	/**
	 * Execute command
	 * 
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @throws ServletException
	 * @throws IOException
	 */
	void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
