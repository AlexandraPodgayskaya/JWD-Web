package by.epam.payment_system.service.impl;

import java.sql.Timestamp;
import java.util.Map;

import by.epam.payment_system.dao.AccountDAO;
import by.epam.payment_system.dao.CardDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.TransactionLogDAO;
import by.epam.payment_system.entity.Account;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.Currency;
import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.entity.TransactionType;
import by.epam.payment_system.service.TransactionService;
import by.epam.payment_system.service.exception.ServiceException;
import by.epam.payment_system.service.exception.TopUpCardServiceException;
import by.epam.payment_system.service.exception.TransferDataServiceException;
import by.epam.payment_system.service.validation.TransactionDataValidator;

public class TransactionServiceImpl implements TransactionService {

	private static final String CURRENCY = "currency";
	private static final String AMOUNT = "amount";
	private static final String RECIPIENT_CARD_NUMBER = "recipientCardNumber";
	private static final String SENDER_CARD_NUMBER = "senderCardNumber";
	private static final String TOP_UP_CARD = "Top up card";

	@Override
	public void topUpCard(Map<String, String> transferDetails) throws ServiceException {

		TransactionDataValidator validator = new TransactionDataValidator();
		if (!validator.topUpCardValidation(transferDetails)) {
			throw new TransferDataServiceException("transfer data error", validator.getDescriptionList());
		}

		DAOFactory factory = DAOFactory.getInstance();
		CardDAO cardDAO = factory.getCardDAO();

		String numberCard = transferDetails.get(RECIPIENT_CARD_NUMBER);

		try {
			Card card = cardDAO.findCardData(numberCard);
			if (card == null || card.getIsClosed() || card.getIsBlocked()) {
				throw new TopUpCardServiceException("can not top up card");
			}

			AccountDAO accountDAO = factory.getAccountDAO();
			Account account = accountDAO.getAccount(card.getNumberAccount());

			if (Currency.valueOf(transferDetails.get(CURRENCY)) != account.getCurrency()) {
				throw new TopUpCardServiceException("can not top up card");
			}

			Transaction transaction = new Transaction(account.getNumberAccount(), numberCard, TransactionType.RECEIPT,
					transferDetails.get(AMOUNT), account.getCurrencyId(), transferDetails.get(SENDER_CARD_NUMBER),
					TOP_UP_CARD);

			if (!accountDAO.updateBalance(transaction)) {
				throw new TopUpCardServiceException("can not top up card");
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
		
		
	}

}
