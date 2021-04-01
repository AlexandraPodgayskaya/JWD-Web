package by.epam.payment_system.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import by.epam.payment_system.dao.AccountDAO;
import by.epam.payment_system.dao.CardDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.TransactionLogDAO;
import by.epam.payment_system.dao.UserDAO;
import by.epam.payment_system.entity.Account;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.CardStatus;
import by.epam.payment_system.entity.Currency;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.entity.TransactionType;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.TransactionService;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.NoSuchUserServiceException;
import by.epam.payment_system.service.exception.NotEnoughMoneyServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.TransactionDataServiceException;
import by.epam.payment_system.service.util.encryption.PasswordEncryption;
import by.epam.payment_system.service.validation.TransactionDataValidator;
import by.epam.payment_system.service.validation.UserDataValidator;

public class TransactionServiceImpl implements TransactionService {

	private static final String CURRENCY = "currency";
	private static final String AMOUNT = "amount";
	private static final String PASSWORD = "passwordCheck";
	private static final String LOGIN = "userLogin";
	private static final String RECIPIENT_CARD_NUMBER = "recipientCardNumber";
	private static final String SENDER_CARD_NUMBER = "senderCardNumber";
	private static final String RECIPIENT_BANK_CODE = "BIC";
	private static final String RECIPIENT_IBAN_ACCOUNT = "IBAN";
	private static final String RECIPIENT_YNP = "recipientYNP";
	private static final String RECIPIENT = "recipientName";
	private static final String TOP_UP_CARD = "Top up card";
	private static final String PURPOSE_OF_PAYMENT = "purposeOfPayment";

	private static final DAOFactory factory = DAOFactory.getInstance();

	@Override
	public void topUpCard(Map<String, String> transferDetails) throws ServiceException {

		TransactionDataValidator validator = new TransactionDataValidator();
		if (!validator.topUpCardValidation(transferDetails)) {
			throw new TransactionDataServiceException("transfer data error", validator.getDescriptionList());
		}

		CardDAO cardDAO = factory.getCardDAO();

		String numberCard = transferDetails.get(RECIPIENT_CARD_NUMBER);

		try {
			Card card = cardDAO.findCardData(numberCard);
			if (card == null || card.getIsClosed() || card.getIsBlocked()) {
				throw new ImpossibleOperationServiceException("can not top up card");
			}

			AccountDAO accountDAO = factory.getAccountDAO();
			Account account = accountDAO.getAccount(card.getNumberAccount());

			if (Currency.valueOf(transferDetails.get(CURRENCY)) != account.getCurrency()) {
				throw new ImpossibleOperationServiceException("can not top up card");
			}

			Transaction transaction = new Transaction(account.getNumberAccount(), numberCard, TransactionType.RECEIPT,
					transferDetails.get(AMOUNT), account.getCurrencyId(), transferDetails.get(SENDER_CARD_NUMBER),
					TOP_UP_CARD);

			if (!accountDAO.updateBalance(transaction)) {
				throw new ImpossibleOperationServiceException("can not top up card");
			}

			transaction.setDateTime(new Timestamp(System.currentTimeMillis()));

			TransactionLogDAO transactionLogDAO = factory.getTransactionLogDAO();
			transactionLogDAO.addTransaction(transaction);

		} catch (DAOException e) {
			throw new ServiceException("toping up card error", e);
		}
	}

	@Override
	public void makePayment(Map<String, String> paymentDetails) throws ServiceException {

		UserInfo userInfo = new UserInfo(paymentDetails.get(LOGIN), paymentDetails.get(PASSWORD));

		UserDataValidator userValidator = new UserDataValidator();
		if (!userValidator.basicDataValidation(userInfo)) {
			throw new NoSuchUserServiceException("wrong password");
		}

		UserDAO userDAO = factory.getUserDAO();

		try {
			userInfo.setPassword(PasswordEncryption.encrypt(userInfo.getPassword()));
			if (userDAO.find(userInfo) == null) {
				throw new NoSuchUserServiceException("wrong password");
			}
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new ServiceException("password encryption error", e);
		} catch (DAOException e) {
			throw new ServiceException("check password error", e);
		}

		TransactionDataValidator validator = new TransactionDataValidator();
		if (!validator.paymentValidation(paymentDetails)) {
			throw new TransactionDataServiceException("payment data error", validator.getDescriptionList());
		}

		CardDAO cardDAO = factory.getCardDAO();

		try {
			Card card = cardDAO.findCardData(paymentDetails.get(SENDER_CARD_NUMBER));
			if (card == null || card.getIsClosed() || card.getIsBlocked()) {
				throw new ImpossibleOperationServiceException("can not make payment");
			}

			AccountDAO accountDAO = factory.getAccountDAO();
			Account account = accountDAO.getAccount(card.getNumberAccount());

			if (Currency.valueOf(paymentDetails.get(CURRENCY)) != account.getCurrency()) {
				throw new ImpossibleOperationServiceException("can not make payment");
			}

			if (account.getBalance().compareTo(new BigDecimal(paymentDetails.get(AMOUNT))) < 0) {
				throw new NotEnoughMoneyServiceException("payment amount is more than balance");
			}

			Transaction transaction = new Transaction(account.getNumberAccount(),
					paymentDetails.get(SENDER_CARD_NUMBER), TransactionType.EXPENDITURE, paymentDetails.get(AMOUNT),
					account.getCurrencyId(), paymentDetails.get(RECIPIENT_BANK_CODE),
					paymentDetails.get(RECIPIENT_IBAN_ACCOUNT), paymentDetails.get(RECIPIENT_YNP),
					paymentDetails.get(RECIPIENT), paymentDetails.get(PURPOSE_OF_PAYMENT));

			if (!accountDAO.updateBalance(transaction)) {
				throw new ImpossibleOperationServiceException("can not make payment");
			}

			transaction.setDateTime(new Timestamp(System.currentTimeMillis()));

			TransactionLogDAO transactionLogDAO = factory.getTransactionLogDAO();
			transactionLogDAO.addTransaction(transaction);

		} catch (DAOException e) {
			throw new ServiceException("payment error", e);
		}

	}

	@Override
	public List<Transaction> takeAccountTransactions(String numberCard) throws ServiceException {

		if (numberCard == null) {
			throw new ImpossibleOperationServiceException("no card number to take account transactions");
		}

		CardDAO cardDAO = factory.getCardDAO();
		TransactionLogDAO transactionLogDAO = factory.getTransactionLogDAO();

		List<Transaction> transactionList;
		try {
			Card card = cardDAO.findCardData(numberCard);
			if (card.getStatus() == CardStatus.ADDITIONAL) {
				throw new ImpossibleOperationServiceException(
						"it is impossible to show account transactions for owners of additional cards");
			}
			transactionList = transactionLogDAO.findAccountTransactions(card.getNumberAccount());

		} catch (DAOException e) {
			throw new ServiceException("search account transactions error", e);
		}
		return transactionList;
	}

	@Override
	public List<Transaction> takeCardTransactions(String numberCard) throws ServiceException {

		if (numberCard == null) {
			throw new ImpossibleOperationServiceException("no card number to take account transactions");
		}

		TransactionLogDAO transactionLogDAO = factory.getTransactionLogDAO();

		List<Transaction> transactionList;
		try {
			transactionList = transactionLogDAO.findCardTransactions(numberCard);
		} catch (DAOException e) {
			throw new ServiceException("search account transactions error", e);
		}
		return transactionList;
	}
}
