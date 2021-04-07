package by.epam.payment_system.controller.builder;

import javax.servlet.http.HttpServletRequest;

import by.epam.payment_system.util.ParameterConstraint;

public class UserInfoBuilder extends AbstractUserInfoBuilder {

	@Override
	public void buildUserInfo(HttpServletRequest request) {
		userInfo.setLogin(request.getParameter(ParameterConstraint.LOGIN));
		userInfo.setPassword(request.getParameter(ParameterConstraint.PASSWORD));
	}

}
