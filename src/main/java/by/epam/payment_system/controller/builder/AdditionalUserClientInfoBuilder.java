package by.epam.payment_system.controller.builder;

import javax.servlet.http.HttpServletRequest;

import by.epam.payment_system.controller.command.Parameter;

public class AdditionalUserClientInfoBuilder extends AbstractUserInfoBuilder {

	@Override
	public void buildUserInfo(HttpServletRequest request) {
		userInfo.setId(Integer.valueOf(request.getParameter(Parameter.ID)));
		userInfo.setLogin(request.getParameter(Parameter.LOGIN));
		userInfo.setPassword(request.getParameter(Parameter.PASSWORD));
		userInfo.setSurname(request.getParameter(Parameter.SURNAME));
		userInfo.setName(request.getParameter(Parameter.NAME));
		userInfo.setPatronymic(request.getParameter(Parameter.PATRONYMIC));
		userInfo.setDateBirth(request.getParameter(Parameter.DATE_OF_BIRTH));
		userInfo.setPersonalNumberPassport(request.getParameter(Parameter.PERSONAL_NUMBER_PASSPORT));
		userInfo.setPhone(request.getParameter(Parameter.PHONE));
	}

}
