package by.epam.payment_system.service.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import by.epam.payment_system.entity.Currency;
import by.epam.payment_system.util.Message;
import by.epam.payment_system.util.ParameterConstraint;

/**
 * Validate transaction data
 * 
 * @author Aleksandra Podgayskaya
 */
public class TransactionDataValidator {

	private static final String NUMBER_CARD_PATTERN = "^[0-9]{16}$";
	private static final String EXPIRATION_DATE_PATTERN = "^(0\\d|1[012])\\/(\\d{2})$";
	private static final String CVV_CODE_PATTERN = "^[0-9]{3}$";
	private static final String SUM_PATTERN = "^[0-9]{1,8}(\\.[0-9]{2})?$";
	private static final String YNP_PATTERN = "^[0-9]{9}$";
	private static final String BIC_PATTERN = "^[A-Z0-9]{8}$";
	private static final String IBAN_PATTERN = "^BY[0-9]{2}[A-Z]{4}[0-9]{20}$";
	private static final String DATE_FORMAT = "MM/yy";
	private static final String START_ATTRIBUTE = "<";
	private static final String END_ATTRIBUTE = ">";
	private static final String START_ATTRIBUTE_NAME = "&lt";
	private static final String END_ATTRIBUTE_NAME = "&gt";

	/**
	 * Keeps {@link List} of {@link String} error names
	 */
	private List<String> descriptionList;

	/**
	 * Get error names
	 * 
	 * @return {@link List} of {@link String} error names
	 */
	public List<String> getDescriptionList() {
		return descriptionList;
	}

	/**
	 * Set error
	 * 
	 * @param description {@link String} error name
	 */
	private void setDescriptionList(String description) {
		if (descriptionList == null) {
			descriptionList = new ArrayList<>();
		}
		descriptionList.add(description);
	}

	/**
	 * Validate data for card replenishment
	 * 
	 * @param transferDetails {@link Map} data for card replenishment
	 * @return boolean
	 */
	public final boolean topUpCardValidation(Map<String, String> transferDetails) {

		if (transferDetails == null) {
			setDescriptionList(Message.ERROR_NO_TRANSFER_DETAILS);
			return false;
		}

		transferValidation(transferDetails);

		if (transferDetails.get(ParameterConstraint.SENDER_EXPIRATION_DATE) != null
				&& transferDetails.get(ParameterConstraint.SENDER_EXPIRATION_DATE).matches(EXPIRATION_DATE_PATTERN)) {

			String expirationDate = transferDetails.get(ParameterConstraint.SENDER_EXPIRATION_DATE);
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern(DATE_FORMAT);
			try {
				Date date = format.parse(expirationDate);
				Date currentDate = new Date();
				if (date.before(currentDate)) {
					setDescriptionList(Message.ERROR_EXPIRATION_DATE);
				}
			} catch (ParseException e) {
				setDescriptionList(Message.ERROR_EXPIRATION_DATE);
			}
		} else {
			setDescriptionList(Message.ERROR_EXPIRATION_DATE);
		}

		if (transferDetails.get(ParameterConstraint.SENDER_CVV_CODE) == null
				|| !transferDetails.get(ParameterConstraint.SENDER_CVV_CODE).matches(CVV_CODE_PATTERN)) {
			setDescriptionList(Message.ERROR_CVV_CODE);
		}

		return descriptionList == null;
	}

	/**
	 * Validate payment data
	 * 
	 * @param paymentDetails {@link Map} payment data
	 * @return boolean
	 */
	public final boolean paymentValidation(Map<String, String> paymentDetails) {

		if (paymentDetails == null) {
			setDescriptionList(Message.ERROR_NO_PAYMENT_DETAILS);
			return false;
		}

		if (paymentDetails.get(ParameterConstraint.SENDER_CARD_NUMBER) == null
				|| !paymentDetails.get(ParameterConstraint.SENDER_CARD_NUMBER).matches(NUMBER_CARD_PATTERN)) {
			setDescriptionList(Message.ERROR_NUMBER_CARD);
		}

		if (paymentDetails.get(ParameterConstraint.RECIPIENT_YNP) == null
				|| !paymentDetails.get(ParameterConstraint.RECIPIENT_YNP).matches(YNP_PATTERN)) {
			setDescriptionList(Message.ERROR_YNP);
		}

		String recipient = paymentDetails.get(ParameterConstraint.RECIPIENT);
		if (recipient == null) {
			setDescriptionList(Message.ERROR_RECIPIENT);
		} else {
			paymentDetails.put(ParameterConstraint.RECIPIENT, recipient
					.replaceAll(START_ATTRIBUTE, START_ATTRIBUTE_NAME).replaceAll(END_ATTRIBUTE, END_ATTRIBUTE_NAME));
		}

		if (paymentDetails.get(ParameterConstraint.RECIPIENT_BANK_CODE) == null
				|| !paymentDetails.get(ParameterConstraint.RECIPIENT_BANK_CODE).matches(BIC_PATTERN)) {
			setDescriptionList(Message.ERROR_BIC);
		}

		if (paymentDetails.get(ParameterConstraint.RECIPIENT_IBAN_ACCOUNT) == null
				|| !paymentDetails.get(ParameterConstraint.RECIPIENT_IBAN_ACCOUNT).matches(IBAN_PATTERN)) {
			setDescriptionList(Message.ERROR_IBAN);
		}

		String purposePayment = paymentDetails.get(ParameterConstraint.PURPOSE_OF_PAYMENT);
		if (purposePayment != null) {
			paymentDetails.put(ParameterConstraint.PURPOSE_OF_PAYMENT, purposePayment
					.replaceAll(START_ATTRIBUTE, START_ATTRIBUTE_NAME).replaceAll(END_ATTRIBUTE, END_ATTRIBUTE_NAME));
		}

		if (paymentDetails.get(ParameterConstraint.AMOUNT) == null
				|| !paymentDetails.get(ParameterConstraint.AMOUNT).matches(SUM_PATTERN)) {
			setDescriptionList(Message.ERROR_SUM);
		}

		if (paymentDetails.get(ParameterConstraint.CURRENCY) != null) {
			try {
				Currency.valueOf(paymentDetails.get(ParameterConstraint.CURRENCY));
			} catch (IllegalArgumentException e) {
				setDescriptionList(Message.ERROR_CURRENCY);
			}
		} else {
			setDescriptionList(Message.ERROR_CURRENCY);
		}

		return descriptionList == null;
	}

	/**
	 * Validate data for transfer
	 * 
	 * @param transferDetails {@link Map} data for transfer
	 * @return boolean
	 */
	public final boolean transferValidation(Map<String, String> transferDetails) {

		if (transferDetails == null) {
			setDescriptionList(Message.ERROR_NO_TRANSFER_DETAILS);
			return false;
		}

		if (transferDetails.get(ParameterConstraint.SENDER_CARD_NUMBER) == null
				|| !transferDetails.get(ParameterConstraint.SENDER_CARD_NUMBER).matches(NUMBER_CARD_PATTERN)) {
			setDescriptionList(Message.ERROR_NUMBER_CARD);
		}

		if (transferDetails.get(ParameterConstraint.RECIPIENT_CARD_NUMBER) == null
				|| !transferDetails.get(ParameterConstraint.RECIPIENT_CARD_NUMBER).matches(NUMBER_CARD_PATTERN)) {
			setDescriptionList(Message.ERROR_NUMBER_CARD);
		}
		if (transferDetails.get(ParameterConstraint.AMOUNT) == null
				|| !transferDetails.get(ParameterConstraint.AMOUNT).matches(SUM_PATTERN)) {
			setDescriptionList(Message.ERROR_SUM);
		}

		if (transferDetails.get(ParameterConstraint.CURRENCY) != null) {
			try {
				Currency.valueOf(transferDetails.get(ParameterConstraint.CURRENCY));
			} catch (IllegalArgumentException e) {
				setDescriptionList(Message.ERROR_CURRENCY);
			}
		} else {
			setDescriptionList(Message.ERROR_CURRENCY);
		}

		return descriptionList == null;
	}
}
