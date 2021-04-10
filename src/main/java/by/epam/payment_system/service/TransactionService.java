package by.epam.payment_system.service;

import java.util.List;
import java.util.Map;

import by.epam.payment_system.entity.Transaction;
import by.epam.payment_system.service.exception.ServiceException;

public interface TransactionService {

	void topUpCard(Map<String, String> transferDetails) throws ServiceException;

	void makePayment(Map<String, String> paymentDetails) throws ServiceException;
	
	void makeTransfer(Map<String, String> transferDetails) throws ServiceException;
	
	List <Transaction> takeAccountTransactions (String numberCard) throws ServiceException;
	
	List <Transaction> takeCardTransactions (String numberCard) throws ServiceException;
}
