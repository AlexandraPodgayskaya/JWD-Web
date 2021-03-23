package by.epam.payment_system.controller.builder;

import javax.servlet.http.HttpServletRequest;

import by.epam.payment_system.entity.UserInfo;

public abstract class AbstractUserInfoBuilder {
	protected UserInfo userInfo;

	public AbstractUserInfoBuilder() {
		userInfo = new UserInfo();
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public abstract void buildUserInfo(HttpServletRequest request);

}
