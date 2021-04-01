package by.epam.payment_system.dao;

import by.epam.payment_system.entity.TransactionType;

public interface TypeTransactionDAO {
	
	TransactionType findType (int id) throws DAOException;
	Integer findId (TransactionType type)throws DAOException;

}
