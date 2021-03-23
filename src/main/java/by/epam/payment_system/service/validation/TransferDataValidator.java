package by.epam.payment_system.service.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import by.epam.payment_system.entity.Currency;

public class TransferDataValidator {

	private static final String SENDER_CARD_NUMBER = "senderCardNumber";
	private static final String SENDER_EXPIRATION_DATE = "senderExpirationDate";
	private static final String SENDER_CVV_CODE = "senderCvvCode";
	private static final String AMOUNT = "amount";
	private static final String CURRENCY = "currency";
	private static final String RECIPIENT_CARD_NUMBER = "recipientCardNumber";
	private static final String NUMBER_CARD = "^[0-9]{16}$";
	private static final String EXPIRATION_DATE = "^(0\\d|1[012])\\/(\\d{2})$";
	private static final String CVV_CODE = "^[0-9]{3}$";
	private static final String DATE_FORMAT = "MM/yy";
	private static final String SUM = "^[0-9]{1,8}(\\.[0-9]{2})?$";
	private static final String ERROR_NUMBER_CARD = "local.error.number_card";
	private static final String ERROR_EXPIRATION_DATE = "local.error.expiration_date";
	private static final String ERROR_CVV_CODE = "local.error.cvv_code";
	private static final String ERROR_SUM = "local.error.sum";
	private static final String ERROR_CURRENCY = "local.error.currency";

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

	public final boolean validation(Map<String, String> transferDetails) {

		if (transferDetails.get(SENDER_CARD_NUMBER) == null
				|| !transferDetails.get(SENDER_CARD_NUMBER).matches(NUMBER_CARD)) {
			setDescriptionList(ERROR_NUMBER_CARD);
		}

		if (transferDetails.get(SENDER_EXPIRATION_DATE) != null
				&& transferDetails.get(SENDER_EXPIRATION_DATE).matches(EXPIRATION_DATE)) {

			String expirationDate = transferDetails.get(SENDER_EXPIRATION_DATE);
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern(DATE_FORMAT);
			try {
				Date date = format.parse(expirationDate);
				Date currentDate = new Date();
				if (date.before(currentDate)) {
					setDescriptionList(ERROR_EXPIRATION_DATE);
				}
			} catch (ParseException e) {
				setDescriptionList(ERROR_EXPIRATION_DATE);
			}
		} else {
			setDescriptionList(ERROR_EXPIRATION_DATE);
		}

		if (transferDetails.get(SENDER_CVV_CODE) == null || !transferDetails.get(SENDER_CVV_CODE).matches(CVV_CODE)) {
			setDescriptionList(ERROR_CVV_CODE);
		}

		if (transferDetails.get(AMOUNT) == null || !transferDetails.get(AMOUNT).matches(SUM)) {
			setDescriptionList(ERROR_SUM);
		}

		if (transferDetails.get(CURRENCY) != null) {
			try {
				Currency.valueOf(transferDetails.get(CURRENCY));
			} catch (IllegalArgumentException e) {
				setDescriptionList(ERROR_CURRENCY);
			}
		} else {
			setDescriptionList(ERROR_CURRENCY);
		}

		if (transferDetails.get(RECIPIENT_CARD_NUMBER) == null
				|| !transferDetails.get(RECIPIENT_CARD_NUMBER).matches(NUMBER_CARD)) {
			setDescriptionList(ERROR_NUMBER_CARD);
		}

		return descriptionList == null;
	}
}
