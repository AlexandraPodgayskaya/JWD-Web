package by.epam.payment_system.dao;

import by.epam.payment_system.entity.UserInfo;

public interface AdditionalClientDataDAO {

	void create(UserInfo additionalClientInfo) throws DAOException;

	UserInfo findData(String personalNumberPassport) throws DAOException;

}
