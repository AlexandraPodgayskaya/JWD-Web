package by.epam.payment_system.dao;

import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;

public interface UserDAO {

	void create(UserInfo registrationInfo) throws DAOException;

	User find(UserInfo loginationInfo) throws DAOException;
	
	Integer findId(String login) throws DAOException;

}
