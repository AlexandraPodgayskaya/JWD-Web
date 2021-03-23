package by.epam.payment_system.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.epam.payment_system.controller.builder.AbstractUserInfoBuilder;
import by.epam.payment_system.controller.builder.AdditionalUserClientInfoBuilder;
import by.epam.payment_system.controller.command.Command;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.AdditionalClientDataService;
import by.epam.payment_system.service.ServiceFactory;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.UserInfoFormatServiceException;

public class SaveAdditionalClientDataCommandImpl implements Command {
	
	private static final String GO_TO_INDEX_PAGE = "index.jsp";
	private static final String GO_TO_CLIENT_DATA_PAGE = "UserData?userId=";
	private static final String GO_TO_ERROR_PAGE = "error.jsp";
	private static final String ATTRIBUTE_ERROR_MESSAGE = "errorMessageList";
	private static final String ATTRIBUTE_PAGE = "page";
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AbstractUserInfoBuilder builder = new AdditionalUserClientInfoBuilder();
		builder.buildUserInfo(request);
		UserInfo additionalClientInfo = builder.getUserInfo();

		ServiceFactory factory = ServiceFactory.getInstance();
		AdditionalClientDataService additionalClientDataService = factory.getAdditionalClientDataService();
		
		HttpSession session = request.getSession(true);
		try {
			additionalClientDataService.addData(additionalClientInfo);
			session.setAttribute(ATTRIBUTE_PAGE, GO_TO_INDEX_PAGE);
			response.sendRedirect(GO_TO_INDEX_PAGE);
		} catch (UserInfoFormatServiceException e) {
			session.setAttribute(ATTRIBUTE_ERROR_MESSAGE, e.getErrorDescription());
			response.sendRedirect(GO_TO_CLIENT_DATA_PAGE + additionalClientInfo.getId());
		} catch (ServiceException e) {
			response.sendRedirect(GO_TO_ERROR_PAGE);
		}
	}

}
