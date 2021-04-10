package by.epam.payment_system.dao;

import java.util.List;

import by.epam.payment_system.entity.CardType;

public interface CardTypeDAO {

	List<CardType> findAll() throws DAOException;

}
