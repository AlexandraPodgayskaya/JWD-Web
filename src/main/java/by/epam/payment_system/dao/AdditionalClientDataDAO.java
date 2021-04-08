package by.epam.payment_system.dao;

import java.util.Optional;

import by.epam.payment_system.entity.UserInfo;

public interface AdditionalClientDataDAO {

	void create(UserInfo additionalClientInfo) throws DAOException;
	
	boolean update(UserInfo userInfo) throws DAOException;

	Optional <UserInfo> findDataByPassport(String personalNumberPassport) throws DAOException;
	
	Optional <UserInfo> findDataById(Integer userId) throws DAOException;

}
