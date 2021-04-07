package by.epam.payment_system.controller.builder;

import javax.servlet.http.HttpServletRequest;

import by.epam.payment_system.util.ParameterConstraint;

public class AdditionalUserClientInfoBuilder extends AbstractUserInfoBuilder {

	@Override
	public void buildUserInfo(HttpServletRequest request) {
		userInfo.setId(Integer.valueOf(request.getParameter(ParameterConstraint.USER_ID)));
		userInfo.setLogin(request.getParameter(ParameterConstraint.LOGIN));
		userInfo.setPassword(request.getParameter(ParameterConstraint.PASSWORD));
		userInfo.setSurname(request.getParameter(ParameterConstraint.SURNAME));
		userInfo.setName(request.getParameter(ParameterConstraint.NAME));
		userInfo.setPatronymic(request.getParameter(ParameterConstraint.PATRONYMIC));
		userInfo.setDateBirth(request.getParameter(ParameterConstraint.DATE_OF_BIRTH));
		userInfo.setPersonalNumberPassport(request.getParameter(ParameterConstraint.PERSONAL_NUMBER_PASSPORT));
		userInfo.setPhone(request.getParameter(ParameterConstraint.PHONE));
	}

}
