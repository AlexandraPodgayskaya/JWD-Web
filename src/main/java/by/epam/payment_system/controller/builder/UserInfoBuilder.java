package by.epam.payment_system.controller.builder;

import javax.servlet.http.HttpServletRequest;

public class UserInfoBuilder extends AbstractUserInfoBuilder {
	
	private static final String PARAMETR_LOGIN = "login";
	private static final String PARAMETR_PASSWORD = "password";
	
	@Override
	public void buildUserInfo(HttpServletRequest request) {
		userInfo.setLogin(request.getParameter(PARAMETR_LOGIN));
		userInfo.setPassword(request.getParameter(PARAMETR_PASSWORD));
	}

}
