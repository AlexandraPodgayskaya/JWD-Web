package by.epam.payment_system.dao;

import by.epam.payment_system.entity.Account;
import by.epam.payment_system.entity.Transaction;

public interface AccountDAO {

	Account getAccount(String numberAccount) throws DAOException;

	boolean updateBalance(Transaction transaction) throws DAOException;

}
