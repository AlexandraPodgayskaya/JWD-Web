package by.epam.payment_system.controller.builder;

import javax.servlet.http.HttpServletRequest;

import by.epam.payment_system.util.ParameterConstraint;

/**
 * The builder is responsible for building user info
 * 
 * @author Aleksandra Podgayskaya
 * @see AbstractUserInfoBuilder
 */
public class UserInfoBuilder extends AbstractUserInfoBuilder {

	/**
	 * Build credentials
	 * 
	 * @param request {@link HttpServletRequest}
	 */
	@Override
	public void buildUserInfo(HttpServletRequest request) {
		userInfo.setLogin(request.getParameter(ParameterConstraint.LOGIN));
		userInfo.setPassword(request.getParameter(ParameterConstraint.PASSWORD));
	}

}
