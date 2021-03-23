package by.epam.payment_system.dao;

import java.util.List;

import by.epam.payment_system.entity.Card;

public interface CardDAO {

	List <Card> findCards(Integer userId) throws DAOException;
	boolean updateBlocking (Card card) throws DAOException;
	Card findCardData (String numberCard) throws DAOException;
}
