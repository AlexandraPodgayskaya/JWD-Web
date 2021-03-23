package by.epam.payment_system.dao;

import by.epam.payment_system.entity.Transaction;

public interface TransactionLogDAO {
	
	void addTransaction (Transaction transaction) throws DAOException;

}
