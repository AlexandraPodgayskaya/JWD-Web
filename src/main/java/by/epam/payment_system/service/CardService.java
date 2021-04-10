package by.epam.payment_system.service;

import java.util.List;

import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.CardInfo;
import by.epam.payment_system.service.exception.ServiceException;

public interface CardService {

	List<Card> takeCards(Integer userId) throws ServiceException;

	void blockCard(String numberCard) throws ServiceException;

	void unblockCard(String numberCard) throws ServiceException;

	void closeCard(String numberCard) throws ServiceException;

	void openMainCard(Card card) throws ServiceException;

	void openAdditionalCard(Card card, String personalNumberPassport, Integer userId) throws ServiceException;

	CardInfo takeAllCardOptions(Integer id) throws ServiceException;

}
