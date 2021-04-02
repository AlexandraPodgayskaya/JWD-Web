package by.epam.payment_system.dao;

import java.util.Optional;

import by.epam.payment_system.entity.Account;
import by.epam.payment_system.entity.Transaction;

public interface AccountDAO {

	Optional <Account> getAccount(String numberAccount) throws DAOException;

	boolean updateBalance(Transaction transaction) throws DAOException;

}
