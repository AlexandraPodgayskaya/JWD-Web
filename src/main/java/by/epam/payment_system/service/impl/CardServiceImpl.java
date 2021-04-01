package by.epam.payment_system.service.impl;

import java.util.Collections;
import java.util.List;

import by.epam.payment_system.dao.AccountDAO;
import by.epam.payment_system.dao.CardDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.entity.Account;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.service.CardService;

import by.epam.payment_system.service.exception.ImpossibleOperationServiceException;
import by.epam.payment_system.service.exception.ServiceException;

public class CardServiceImpl implements CardService {
	
	private static final DAOFactory factory = DAOFactory.getInstance();

	@Override
	public List<Card> takeCards(Integer userId) throws ServiceException {

		if (userId == null) {
			return Collections.emptyList();
		}
		
		CardDAO cardDAO = factory.getCardDAO();
		AccountDAO accountDAO = factory.getAccountDAO();

		List<Card> cardList;
		try {
			cardList = cardDAO.findCards(userId);
			for (Card card : cardList) {
				Account account = accountDAO.getAccount(card.getNumberAccount());
				if (account != null) {
					card.setBalance(account.getBalance());
					card.setCurrency(account.getCurrency());
				}
			}
		} catch (DAOException e) {
			throw new ServiceException("card search error", e);
		}
		return cardList;
	}

	@Override
	public void blockCard(String numberCard) throws ServiceException {

		if (numberCard == null) {
			throw new ImpossibleOperationServiceException("no card number to block");
		}

		CardDAO cardDAO = factory.getCardDAO();

		Card card = new Card(numberCard, Boolean.TRUE);

		try {
			if (!cardDAO.updateBlocking(card)) {
				throw new ImpossibleOperationServiceException("card blocking error");
			}

		} catch (DAOException e) {
			throw new ServiceException("card blocking error", e);
		}
	}

	@Override
	public void unblockCard(String numberCard) throws ServiceException {
		if (numberCard == null) {
			throw new ImpossibleOperationServiceException("no card number to unblock");
		}

		CardDAO cardDAO = factory.getCardDAO();

		Card card = new Card(numberCard, Boolean.FALSE);

		try {
			if (!cardDAO.updateBlocking(card)) {
				throw new ImpossibleOperationServiceException("card unblocking error");
			}

		} catch (DAOException e) {
			throw new ServiceException("card unblocking error", e);
		}
		
	}
	
	@Override
	public void closeCard(String numberCard) throws ServiceException {

		if (numberCard == null) {
			throw new ImpossibleOperationServiceException("no card number to close");
		}

		CardDAO cardDAO = factory.getCardDAO();

		try {
			if (!cardDAO.setClosed(numberCard)) {
				throw new ImpossibleOperationServiceException("card closing error");
			}

		} catch (DAOException e) {
			throw new ServiceException("card closing error", e);
		}

	}


}
