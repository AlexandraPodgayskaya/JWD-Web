package by.epam.payment_system.dao;

import java.util.Optional;

import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;

public interface UserDAO {

	void create(UserInfo registrationInfo) throws DAOException;

	Optional <User> find(UserInfo loginationInfo) throws DAOException;
	
	Optional <Integer> findId(String login) throws DAOException;

}
