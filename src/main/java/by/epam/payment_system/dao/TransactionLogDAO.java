package by.epam.payment_system.dao;

import java.util.List;

import by.epam.payment_system.entity.Transaction;

public interface TransactionLogDAO {

	void addTransaction(Transaction transaction) throws DAOException;

	List<Transaction> findCardTransactions(String numberCard) throws DAOException;

	List<Transaction> findAccountTransactions(String numberAccount) throws DAOException;

}
