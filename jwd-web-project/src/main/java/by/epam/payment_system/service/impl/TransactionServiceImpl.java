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
import by.epam.payment_system.entity.Transfer;
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

/**
 * The service is responsible for transactions
 * 
 * @author Aleksandra Podgayskaya
 * @see TransactionService
 */
public class TransactionServiceImpl implements TransactionService {

	private static final String TOP_UP_CARD = "Top up card";
	private static final String TRANSFER_FROM_CARD = "Transfer from the card";
	private static final String TRANSFER_TO_CARD = "Transfer to the card";

	/**
	 * Instance of {@link DAOFactory}
	 */
	private static final DAOFactory factory = DAOFactory.getInstance();

	/**
	 * Top up card
	 * 
	 * @param transferDetails {@link Map} data for card replenishment
	 * @throws ServiceException if transferDetails is incorrect, transaction is not
	 *                          possible or {@link DAOException} occurs
	 */
	@Override
	public void topUpCard(Map<String, String> transferDetails) throws ServiceException {

		TransactionDataValidator validator = new TransactionDataValidator();
		if (!validator.topUpCardValidation(transferDetails)) {
			throw new TransactionDataServiceException("transfer data error", validator.getDescriptionList());
		}

		Card card = takeAllCardData(transferDetails.get(ParameterConstraint.RECIPIENT_CARD_NUMBER));
		checkIsPossibleTransaction(card, transferDetails.get(ParameterConstraint.CURRENCY));

		Transaction transaction = new Transaction(card.getNumberAccount(), card.getNumberCard(),
				TransactionType.RECEIPT, transferDetails.get(ParameterConstraint.AMOUNT), card.getCurrency(),
				transferDetails.get(ParameterConstraint.SENDER_CARD_NUMBER), TOP_UP_CARD);

		AccountDAO accountDAO = factory.getAccountDAO();
		try {
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

	/**
	 * Make payment
	 * 
	 * @param paymentDetails {@link Map} payment data
	 * @throws ServiceException if password or paymentDetails are incorrect,
	 *                          transaction is not possible, amount more than
	 *                          balance or {@link DAOException} occurs
	 */
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

		Card card = takeAllCardData(paymentDetails.get(ParameterConstraint.SENDER_CARD_NUMBER));
		checkIsPossibleTransaction(card, paymentDetails.get(ParameterConstraint.CURRENCY));

		if (card.getBalance().compareTo(new BigDecimal(paymentDetails.get(ParameterConstraint.AMOUNT))) < 0) {
			throw new NotEnoughMoneyServiceException("payment amount is more than balance");
		}

		Transaction transaction = new Transaction(card.getNumberAccount(), card.getNumberCard(),
				TransactionType.EXPENDITURE, paymentDetails.get(ParameterConstraint.AMOUNT), card.getCurrency(),
				paymentDetails.get(ParameterConstraint.RECIPIENT_BANK_CODE),
				paymentDetails.get(ParameterConstraint.RECIPIENT_IBAN_ACCOUNT),
				paymentDetails.get(ParameterConstraint.RECIPIENT_YNP),
				paymentDetails.get(ParameterConstraint.RECIPIENT),
				paymentDetails.get(ParameterConstraint.PURPOSE_OF_PAYMENT));

		AccountDAO accountDAO = factory.getAccountDAO();
		try {
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

	/**
	 * Make transfer
	 * 
	 * @param transferDetails {@link Map} data for transfer
	 * @throws ServiceException if password or transferDetails are incorrect,
	 *                          transaction is not possible, amount more than
	 *                          balance or {@link DAOException} occurs
	 */
	@Override
	public void makeTransfer(Map<String, String> transferDetails) throws ServiceException {
		UserInfo userInfo = new UserInfo(transferDetails.get(ParameterConstraint.USER_LOGIN),
				transferDetails.get(ParameterConstraint.PASSWORD_CHECK));

		if (!PasswordCheck.isCorrect(userInfo)) {
			throw new WrongPasswordServiceException("wrong password");
		}

		TransactionDataValidator validator = new TransactionDataValidator();
		if (!validator.transferValidation(transferDetails)) {
			throw new TransactionDataServiceException("transfer data error", validator.getDescriptionList());
		}

		Card senderCard = takeAllCardData(transferDetails.get(ParameterConstraint.SENDER_CARD_NUMBER));
		checkIsPossibleTransaction(senderCard, transferDetails.get(ParameterConstraint.CURRENCY));

		Card recipientCard = takeAllCardData(transferDetails.get(ParameterConstraint.RECIPIENT_CARD_NUMBER));
		checkIsPossibleTransaction(recipientCard, transferDetails.get(ParameterConstraint.CURRENCY));

		if (senderCard.getNumberAccount().equals(recipientCard.getNumberAccount())) {
			throw new ImpossibleOperationServiceException("transfer within one account is not possible");
		}

		if (senderCard.getBalance().compareTo(new BigDecimal(transferDetails.get(ParameterConstraint.AMOUNT))) < 0) {
			throw new NotEnoughMoneyServiceException("transfer amount is more than balance");
		}

		Transaction expenseTransaction = new Transaction(senderCard.getNumberAccount(), senderCard.getNumberCard(),
				TransactionType.EXPENDITURE, transferDetails.get(ParameterConstraint.AMOUNT), senderCard.getCurrency(),
				recipientCard.getNumberCard(), TRANSFER_FROM_CARD);
		Transaction receiptTransaction = new Transaction(recipientCard.getNumberAccount(),
				recipientCard.getNumberCard(), TransactionType.RECEIPT, transferDetails.get(ParameterConstraint.AMOUNT),
				senderCard.getCurrency(), recipientCard.getNumberCard(), TRANSFER_TO_CARD);
		Transfer transfer = new Transfer(senderCard.getNumberAccount(), recipientCard.getNumberAccount(),
				transferDetails.get(ParameterConstraint.AMOUNT));
		AccountDAO accountDAO = factory.getAccountDAO();
		try {
			if (!accountDAO.doTransfer(transfer)) {
				throw new ImpossibleOperationServiceException("can not make transfer");
			}

			Timestamp transactionTime = new Timestamp(System.currentTimeMillis());
			expenseTransaction.setDateTime(transactionTime);
			receiptTransaction.setDateTime(transactionTime);

			TransactionLogDAO transactionLogDAO = factory.getTransactionLogDAO();
			transactionLogDAO.addTransaction(expenseTransaction);
			transactionLogDAO.addTransaction(receiptTransaction);

		} catch (DAOException e) {
			throw new ServiceException("transfer error", e);
		}

	}

	/**
	 * Take all account transactions
	 * 
	 * @param numberCard {@link String} number card
	 * @return {@link List} of {@link Transaction} received from database
	 * @throws ServiceException if numberCard is null, transaction is not possible
	 *                          or {@link DAOException} occurs
	 */
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

	/**
	 * Take all card transactions
	 * 
	 * @param numberCard {@link String} number card
	 * @return {@link List} of {@link Transaction} received from database
	 * @throws ServiceException if numberCard is null or {@link DAOException} occurs
	 */
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

	/**
	 * Check if the transaction is possible
	 * 
	 * @param card     {@link Card} data
	 * @param currency {@link String} transaction currency
	 * @throws ServiceException if transaction is not possible
	 */
	private void checkIsPossibleTransaction(Card card, String currency) throws ServiceException {
		if (card.getIsClosed() || card.getIsBlocked() || Currency.valueOf(currency) != card.getCurrency()) {
			throw new ImpossibleOperationServiceException("impossible transaction");
		}
	}

	/**
	 * Take all card data by number card
	 * 
	 * @param numberCard {@link String} number card
	 * @return {@link Card}
	 * @throws ServiceException if card number is incorrect or {@link DAOException}
	 *                          occurs
	 */
	private Card takeAllCardData(String numberCard) throws ServiceException {
		Card card;

		CardDAO cardDAO = factory.getCardDAO();
		AccountDAO accountDAO = factory.getAccountDAO();

		try {
			Optional<Card> cardOptional = cardDAO.findCardData(numberCard);
			if (cardOptional.isEmpty()) {
				throw new ImpossibleOperationServiceException("no such card");
			}
			card = cardOptional.get();
			Optional<Account> accountOptional = accountDAO.getAccount(card.getNumberAccount());
			if (cardOptional.isEmpty()) {
				throw new ImpossibleOperationServiceException("no such account");
			}
			Account account = accountOptional.get();
			card.setBalance(account.getBalance());
			card.setCurrency(account.getCurrency());

		} catch (DAOException e) {
			throw new ServiceException("card search error", e);
		}
		return card;
	}
}
