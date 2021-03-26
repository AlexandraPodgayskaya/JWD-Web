package by.epam.payment_system.service.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import by.epam.payment_system.entity.Currency;

public class TransactionDataValidator {

	private static final String SENDER_CARD_NUMBER = "senderCardNumber";
	private static final String SENDER_EXPIRATION_DATE = "senderExpirationDate";
	private static final String SENDER_CVV_CODE = "senderCvvCode";
	private static final String AMOUNT = "amount";
	private static final String CURRENCY = "currency";
	private static final String RECIPIENT_YNP = "recipientYNP";
	private static final String RECIPIENT = "recipientName";
	private static final String RECIPIENT_CARD_NUMBER = "recipientCardNumber";
	private static final String RECIPIENT_BANK_CODE = "BIC";
	private static final String RECIPIENT_IBAN_ACCOUNT = "IBAN";
	private static final String NUMBER_CARD = "^[0-9]{16}$";
	private static final String EXPIRATION_DATE = "^(0\\d|1[012])\\/(\\d{2})$";
	private static final String CVV_CODE = "^[0-9]{3}$";
	private static final String DATE_FORMAT = "MM/yy";
	private static final String SUM = "^[0-9]{1,8}(\\.[0-9]{2})?$";
	private static final String YNP = "^[0-9]{9}$";
	private static final String BIC = "^[A-Z0-9]{8}$";
	private static final String IBAN = "^BY[0-9]{2}[A-Z]{4}[0-9]{20}$";
	private static final String ERROR_NUMBER_CARD = "local.error.number_card";
	private static final String ERROR_EXPIRATION_DATE = "local.error.expiration_date";
	private static final String ERROR_CVV_CODE = "local.error.cvv_code";
	private static final String ERROR_SUM = "local.error.sum";
	private static final String ERROR_CURRENCY = "local.error.currency";
	private static final String ERROR_YNP = "local.error.ynp";
	private static final String ERROR_RECIPIENT = "local.error.recipient";
	private static final String ERROR_BIC = "local.error.bic";

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

	public final boolean topUpCardValidation(Map<String, String> transferDetails) {

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

	public final boolean paymentValidation(Map<String, String> paymentDetails) {

		if (paymentDetails.get(SENDER_CARD_NUMBER) == null
				|| !paymentDetails.get(SENDER_CARD_NUMBER).matches(NUMBER_CARD)) {
			setDescriptionList(ERROR_NUMBER_CARD);
		}

		if (paymentDetails.get(RECIPIENT_YNP) == null || !paymentDetails.get(RECIPIENT_YNP).matches(YNP)) {
			setDescriptionList(ERROR_YNP);
		}

		if (paymentDetails.get(RECIPIENT) == null) {
			setDescriptionList(ERROR_RECIPIENT);
		}

		if (paymentDetails.get(RECIPIENT_BANK_CODE) == null || !paymentDetails.get(RECIPIENT_BANK_CODE).matches(BIC)) {
			System.out.println(paymentDetails.get(RECIPIENT_BANK_CODE));
			System.out.println(paymentDetails.get(RECIPIENT_BANK_CODE).matches(BIC));
			setDescriptionList(ERROR_BIC);
		}

		if (paymentDetails.get(RECIPIENT_IBAN_ACCOUNT) == null
				|| !paymentDetails.get(RECIPIENT_IBAN_ACCOUNT).matches(IBAN)) {
			setDescriptionList(ERROR_BIC);
		}

		if (paymentDetails.get(AMOUNT) == null || !paymentDetails.get(AMOUNT).matches(SUM)) {
			setDescriptionList(ERROR_SUM);
		}

		if (paymentDetails.get(CURRENCY) != null) {
			try {
				Currency.valueOf(paymentDetails.get(CURRENCY));
			} catch (IllegalArgumentException e) {
				setDescriptionList(ERROR_CURRENCY);
			}
		} else {
			setDescriptionList(ERROR_CURRENCY);
		}

		return descriptionList == null;
	}
}
