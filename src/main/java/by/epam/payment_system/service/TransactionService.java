package by.epam.payment_system.service;

import java.util.Map;

import by.epam.payment_system.service.exception.ServiceException;

public interface TransactionService {

	void topUpCard(Map<String, String> transferDetails) throws ServiceException;

	void makePayment(Map<String, String> paymentDetails) throws ServiceException;
}
