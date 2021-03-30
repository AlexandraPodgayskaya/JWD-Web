package by.epam.payment_system.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	private static final Logger logger = LogManager.getLogger();

	@Override
	public List<Card> takeCards(Integer userId) throws ServiceException {

		DAOFactory factory = DAOFactory.getInstance();
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
			logger.error(e.getMessage());
			throw new ServiceException("card search error", e);
		}
		return cardList;
	}

	@Override
	public void blockCard(String numberCard) throws ServiceException {

		if (numberCard == null) {
			throw new ImpossibleOperationServiceException("no card number to block");
		}

		DAOFactory factory = DAOFactory.getInstance();
		CardDAO cardDAO = factory.getCardDAO();

		Card card = new Card(numberCard, Boolean.TRUE);

		try {
			if (!cardDAO.updateBlocking(card)) {
				throw new ImpossibleOperationServiceException("card blocking error");
			}

		} catch (DAOException e) {
			logger.error(e.getMessage());
			throw new ServiceException("card blocking error", e);
		}
	}

	@Override
	public void unblockCard(String numberCard) throws ServiceException {
		if (numberCard == null) {
			throw new ImpossibleOperationServiceException("no card number to unblock");
		}

		DAOFactory factory = DAOFactory.getInstance();
		CardDAO cardDAO = factory.getCardDAO();

		Card card = new Card(numberCard, Boolean.FALSE);

		try {
			if (!cardDAO.updateBlocking(card)) {
				throw new ImpossibleOperationServiceException("card unblocking error");
			}

		} catch (DAOException e) {
			logger.error(e.getMessage());
			throw new ServiceException("card unblocking error", e);
		}
		
	}
	
	@Override
	public void closeCard(String numberCard) throws ServiceException {

		if (numberCard == null) {
			throw new ImpossibleOperationServiceException("no card number to close");
		}

		DAOFactory factory = DAOFactory.getInstance();
		CardDAO cardDAO = factory.getCardDAO();

		try {
			if (!cardDAO.setClosed(numberCard)) {
				throw new ImpossibleOperationServiceException("card closing error");
			}

		} catch (DAOException e) {
			logger.error(e.getMessage());
			throw new ServiceException("card closing error", e);
		}

	}


}
