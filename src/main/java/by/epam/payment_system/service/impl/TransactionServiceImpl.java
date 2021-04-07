package by.epam.payment_system.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import by.epam.payment_system.dao.AccountDAO;
import by.epam.payment_system.dao.CardDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.TransactionLogDAO;
import by.epam.payment_system.entity.Account;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.CardStatus;
import by.epam.payment_system.entity.Currency;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.entity.TransactionType;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.service.TransactionService;
import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.NotEnoughMoneyServiceException;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.TransactionDataServiceException;
import by.epam.payment_system.service.exception.WrongPasswordServiceException;
import by.epam.payment_system.service.util.PasswordCheck;
import by.epam.payment_system.service.validation.TransactionDataValidator;
import by.epam.payment_system.util.ParameterConstraint;

public class TransactionServiceImpl implements TransactionService {

	private static final String TOP_UP_CARD = "Top up card";

	private static final DAOFactory factory = DAOFactory.getInstance();

	@Override
	public void topUpCard(Map<String, String> transferDetails) throws ServiceException {

		TransactionDataValidator validator = new TransactionDataValidator();
		if (!validator.topUpCardValidation(transferDetails)) {
			throw new TransactionDataServiceException("transfer data error", validator.getDescriptionList());
		}

		CardDAO cardDAO = factory.getCardDAO();
		String numberCard = transferDetails.get(ParameterConstraint.RECIPIENT_CARD_NUMBER);

		try {
			Optional<Card> cardOptional = cardDAO.findCardData(numberCard);
			if (cardOptional.isEmpty()) {
				throw new ImpossibleOperationServiceException("can not top up card");
			}
			Card card = cardOptional.get();
			if (card.getIsClosed() || card.getIsBlocked()) {
				throw new ImpossibleOperationServiceException("can not top up card");
			}

			AccountDAO accountDAO = factory.getAccountDAO();
			Optional<Account> accountOptional = accountDAO.getAccount(card.getNumberAccount());
			if (accountOptional.isEmpty()) {
				throw new ImpossibleOperationServiceException("can not top up card");
			}
			Account account = accountOptional.get();

			if (Currency.valueOf(transferDetails.get(ParameterConstraint.CURRENCY)) != account.getCurrency()) {
				throw new ImpossibleOperationServiceException("can not top up card");
			}

			Transaction transaction = new Transaction(account.getNumberAccount(), numberCard, TransactionType.RECEIPT,
					transferDetails.get(ParameterConstraint.AMOUNT), account.getCurrency(),
					transferDetails.get(ParameterConstraint.SENDER_CARD_NUMBER), TOP_UP_CARD);

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

		UserInfo userInfo = new UserInfo(paymentDetails.get(ParameterConstraint.USER_LOGIN),
				paymentDetails.get(ParameterConstraint.PASSWORD_CHECK));

		if (!PasswordCheck.isCorrect(userInfo)) {
			throw new WrongPasswordServiceException("wrong password");
		}

		TransactionDataValidator validator = new TransactionDataValidator();
		if (!validator.paymentValidation(paymentDetails)) {
			throw new TransactionDataServiceException("payment data error", validator.getDescriptionList());
		}

		CardDAO cardDAO = factory.getCardDAO();
		try {
			Optional<Card> cardOptional = cardDAO
					.findCardData(paymentDetails.get(ParameterConstraint.SENDER_CARD_NUMBER));
			if (cardOptional.isEmpty()) {
				throw new ImpossibleOperationServiceException("can not make payment");
			}
			Card card = cardOptional.get();
			if (card.getIsClosed() || card.getIsBlocked()) {
				throw new ImpossibleOperationServiceException("can not make payment");
			}

			AccountDAO accountDAO = factory.getAccountDAO();
			Optional<Account> accountOptional = accountDAO.getAccount(card.getNumberAccount());
			if (accountOptional.isEmpty()) {
				throw new ImpossibleOperationServiceException("can not make payment");
			}
			Account account = accountOptional.get();
			if (Currency.valueOf(paymentDetails.get(ParameterConstraint.CURRENCY)) != account.getCurrency()) {
				throw new ImpossibleOperationServiceException("can not make payment");
			}

			if (account.getBalance().compareTo(new BigDecimal(paymentDetails.get(ParameterConstraint.AMOUNT))) < 0) {
				throw new NotEnoughMoneyServiceException("payment amount is more than balance");
			}

			Transaction transaction = new Transaction(account.getNumberAccount(),
					paymentDetails.get(ParameterConstraint.SENDER_CARD_NUMBER), TransactionType.EXPENDITURE,
					paymentDetails.get(ParameterConstraint.AMOUNT), account.getCurrency(),
					paymentDetails.get(ParameterConstraint.RECIPIENT_BANK_CODE),
					paymentDetails.get(ParameterConstraint.RECIPIENT_IBAN_ACCOUNT),
					paymentDetails.get(ParameterConstraint.RECIPIENT_YNP),
					paymentDetails.get(ParameterConstraint.RECIPIENT),
					paymentDetails.get(ParameterConstraint.PURPOSE_OF_PAYMENT));

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
			Optional<Card> cardOptional = cardDAO.findCardData(numberCard);
			if (cardOptional.isEmpty()) {
				throw new ImpossibleOperationServiceException("no such card in payment system");
			}

			Card card = cardOptional.get();
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
