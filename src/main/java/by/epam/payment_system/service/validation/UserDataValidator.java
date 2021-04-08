package by.epam.payment_system.service.validation;

import java.util.ArrayList;
import java.util.List;

import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.util.Message;

public class UserDataValidator {

	private static final String LOGIN_PATTERN = "^[a-zA-Z][a-zA-Z0-9_-]{5,15}$";
	private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{5,15}$";
	private static final String CHARACTER_PATTERN = "^[a-zA-Zа-яА-Я]+$";
	private static final String DATE_PATTERN = "^([0-2]\\d|3[01])\\.(0\\d|1[012])\\.(\\d{4})$";
	private static final String PERSONAL_NUMBER_PASSPORT_PATTERN = "^[0-9]{7}[A-Z][0-9]{3}[A-Z]{2}[0-9]$";
	private static final String PHONE_PATTERN = "^\\+375[0-9]{9}$";

	private List<String> descriptionList;

	public List<String> getDescriptionList() {
		return descriptionList;
	}

	private void setDescriptionList(String description) {
		if (descriptionList == null) {
			descriptionList = new ArrayList<String>();
		}
		descriptionList.add(description);
	}

	public final boolean basicDataValidation(UserInfo userInfo) {

		if (userInfo == null) {
			setDescriptionList(Message.ERROR_NO_USER_INFO);
			return false;
		}

		if (!loginValidation(userInfo.getLogin())) {
			setDescriptionList(Message.ERROR_LOGIN);
		}

		if (!passwordValidation(userInfo.getPassword())) {
			setDescriptionList(Message.ERROR_PASSWORD);
		}
		return descriptionList == null;
	}

	public final boolean additionalDataValidation(UserInfo userInfo) {

		if (userInfo == null) {
			setDescriptionList(Message.ERROR_NO_USER_INFO);
			return false;
		}
		
		if (userInfo.getId() == null) {
			setDescriptionList(Message.ERROR_COMMON);
			return false;
		}

		if (userInfo.getSurname() == null || !userInfo.getSurname().matches(CHARACTER_PATTERN)) {
			setDescriptionList(Message.ERROR_SURNAME);
		}
		if (userInfo.getName() == null || !userInfo.getName().matches(CHARACTER_PATTERN)) {
			setDescriptionList(Message.ERROR_NAME);
		}
		if (userInfo.getPatronymic() == null || !userInfo.getPatronymic().matches(CHARACTER_PATTERN)) {
			setDescriptionList(Message.ERROR_PATRONYMIC);
		}
		if (userInfo.getDateBirth() == null || !userInfo.getDateBirth().matches(DATE_PATTERN)) {
			setDescriptionList(Message.ERROR_DATE_OF_BIRTH);
		}
		if (!numberPassportValidation(userInfo.getPersonalNumberPassport())) {
			setDescriptionList(Message.ERROR_PERSONAL_NUMBER_PASSPORT);
		}
		if (userInfo.getPhone() == null || !userInfo.getPhone().matches(PHONE_PATTERN)) {
			setDescriptionList(Message.ERROR_PHONE);
		}

		return descriptionList == null;
	}

	public final boolean numberPassportValidation(String personalNumberPassport) {
		if (personalNumberPassport == null) {
			return false;
		}
		return personalNumberPassport.matches(PERSONAL_NUMBER_PASSPORT_PATTERN);
	}

	public final boolean loginValidation(String login) {
		if (login == null) {
			return false;
		}
		return login.matches(LOGIN_PATTERN);
	}

	public final boolean passwordValidation(String password) {
		if (password == null) {
			return false;
		}
		return password.matches(PASSWORD_PATTERN);
	}

}
