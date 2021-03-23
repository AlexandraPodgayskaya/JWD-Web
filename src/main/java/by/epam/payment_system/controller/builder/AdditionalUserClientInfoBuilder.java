package by.epam.payment_system.controller.builder;

import javax.servlet.http.HttpServletRequest;

public class AdditionalUserClientInfoBuilder extends AbstractUserInfoBuilder {

	private static final String PARAMETR_ID = "userId";
	private static final String PARAMETR_LOGIN = "login";
	private static final String PARAMETR_PASSWORD = "password";
	private static final String PARAMETR_SURNAME = "surname";
	private static final String PARAMETR_NAME = "name";
	private static final String PARAMETR_PATRONYMIC = "patronymic";
	private static final String PARAMETR_DATE_OF_BIRTH = "date_of_birth";
	private static final String PARAMETR_PERSONAL_NUMBER_PASSPORT = "personal_number_passport";
	private static final String PARAMETR_PHONE = "phone";
	
	@Override
	public void buildUserInfo(HttpServletRequest request) {
		userInfo.setId(Integer.valueOf(request.getParameter(PARAMETR_ID)));
		userInfo.setLogin(request.getParameter(PARAMETR_LOGIN));
		userInfo.setPassword(request.getParameter(PARAMETR_PASSWORD));
		userInfo.setSurname(request.getParameter(PARAMETR_SURNAME));
		userInfo.setName(request.getParameter(PARAMETR_NAME));
		userInfo.setPatronymic(request.getParameter(PARAMETR_PATRONYMIC));
		userInfo.setDateBirth(request.getParameter(PARAMETR_DATE_OF_BIRTH));
		userInfo.setPersonalNumberPassport(request.getParameter(PARAMETR_PERSONAL_NUMBER_PASSPORT));
		userInfo.setPhone(request.getParameter(PARAMETR_PHONE));
	}

}
