package by.epam.payment_system.dao;

import by.epam.payment_system.entity.UserInfo;

public interface AdditionalClientDataDAO extends GenericDAO<UserInfo, Integer, String> {
	
	UserInfo findData (String personalNumberPassport) throws DAOException;

}
