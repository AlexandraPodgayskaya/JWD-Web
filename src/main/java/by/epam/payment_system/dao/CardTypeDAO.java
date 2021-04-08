package by.epam.payment_system.dao;

import java.util.List;

import by.epam.payment_system.entity.CardType;

public interface CardTypeDAO {

	void create(CardType cardType) throws DAOException;

	List<CardType> findAll() throws DAOException;

}
