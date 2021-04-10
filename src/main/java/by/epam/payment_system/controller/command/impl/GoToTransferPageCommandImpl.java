package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.controller.util.GoToPage;
import by.epam.payment_system.controller.util.OperationControl;
import by.epam.payment_system.controller.util.SessionControl;
import by.epam.payment_system.controller.util.URIConstructor;
import by.epam.payment_system.entity.UserType;
import by.epam.payment_system.util.ParameterConstraint;

public class GoToTransferPageCommandImpl implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (!SessionControl.isExist(request, response)
				|| !OperationControl.isAllowedToUser(request, response, UserType.CLIENT)) {
			return;
		}

		request.setAttribute(ParameterConstraint.NUMBER_CARD, request.getParameter(ParameterConstraint.NUMBER_CARD));
		request.setAttribute(ParameterConstraint.CURRENCY, request.getParameter(ParameterConstraint.CURRENCY));
		request.setAttribute(ParameterConstraint.BALANCE, request.getParameter(ParameterConstraint.BALANCE));

		HttpSession session = request.getSession(true);

		session.setAttribute(ParameterConstraint.PAGE,
				GoToPage.PAYMENT_PAGE + URIConstructor.SET_NUMBER_CARD
						+ request.getParameter(ParameterConstraint.NUMBER_CARD) + URIConstructor.SET_CURRENCY
						+ request.getParameter(ParameterConstraint.CURRENCY) + URIConstructor.SET_BALANCE
						+ request.getParameter(ParameterConstraint.BALANCE));

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(GoToPage.FORWARD_TRANSFER_PAGE);
		requestDispatcher.forward(request, response);
	}

}