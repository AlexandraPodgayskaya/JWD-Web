package by.epam.payment_system.controller.builder;

import javax.servlet.http.HttpServletRequest;

import by.epam.payment_system.controller.command.Parameter;

public class UserInfoBuilder extends AbstractUserInfoBuilder {

	@Override
	public void buildUserInfo(HttpServletRequest request) {
		userInfo.setLogin(request.getParameter(Parameter.LOGIN));
		userInfo.setPassword(request.getParameter(Parameter.PASSWORD));
	}

}
