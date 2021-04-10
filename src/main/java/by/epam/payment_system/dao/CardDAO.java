package by.epam.payment_system.dao;

import java.util.List;
import java.util.Optional;

import by.epam.payment_system.entity.Card;

public interface CardDAO {

	List<Card> findCards(Integer userId) throws DAOException;

	boolean updateBlocking(Card card) throws DAOException;

	Optional<Card> findCardData(String numberCard) throws DAOException;

	boolean setClosed(String numberCard) throws DAOException;

	Optional<Long> getLastCardNumber() throws DAOException;

	void create(Card card) throws DAOException;
}
