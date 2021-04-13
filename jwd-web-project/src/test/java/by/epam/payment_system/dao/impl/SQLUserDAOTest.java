package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.UserDAO;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.User;
import by.epam.payment_system.entity.UserInfo;
import by.epam.payment_system.entity.UserType;

public class SQLUserDAOTest {

	private static final String NAME_CONFIGURATION_FILE = "db_test";

	private static final String USER_LOGIN = "Sane4ka";
	private static final String WRONG_LOGIN = "Nastya";
	private static final String NEW_LOGIN = "New User";
	private static final String UPDATED_LOGIN = "Polina";
	private static final String USER_PASSWORD = "241992";
	private static final String NEW_PASSWORD = "123456";
	private static final String SELECT_NEW_USER_SQL = "SELECT ID FROM USERS WHERE LOGIN='New User'";
	private static final Integer USER_ID = 1;
	private static final Integer UPDATED_USER_ID = 2;
	private static final Integer WRONG_ID = 0;

	private static final UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

	@BeforeClass
	public static void connectionPoolInit() throws ConnectionPoolException {
		ConnectionPool.getInstance().init(NAME_CONFIGURATION_FILE);
	}

	@AfterClass
	public static void connectionPoolDestroy() throws ConnectionPoolException {
		ConnectionPool.getInstance().destroy();
	}

	@Test
	public void createTest() throws DAOException, SQLException, ConnectionPoolException {
		UserInfo registrationInfo = new UserInfo(NEW_LOGIN, NEW_PASSWORD, UserType.CLIENT);
		userDAO.create(registrationInfo);

		try (Connection connection = ConnectionPool.getInstance().takeConnection();
				Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(SELECT_NEW_USER_SQL);
			Assert.assertTrue(resultSet.next());
		}
	}

	@Test(expected = DAOException.class)
	public void createNegativeTest() throws DAOException {
		UserInfo registrationInfo = new UserInfo(USER_LOGIN, NEW_PASSWORD, UserType.CLIENT);
		userDAO.create(registrationInfo);
	}

	@Test
	public void findIdTest1() throws DAOException {
		Integer expected = USER_ID;
		Integer actual = userDAO.findId(USER_LOGIN).get();

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void findIdTest2() throws DAOException {
		Optional<Integer> idOptional = userDAO.findId(WRONG_LOGIN);

		Assert.assertTrue(idOptional.isEmpty());
	}

	@Test
	public void findTest1() throws DAOException {
		UserInfo loginationInfo = new UserInfo(USER_LOGIN, USER_PASSWORD);
		User expected = new User(USER_ID, USER_LOGIN, USER_PASSWORD, UserType.CLIENT);
		User actual = userDAO.find(loginationInfo).get();

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void findTest2() throws DAOException {
		UserInfo loginationInfo = new UserInfo(WRONG_LOGIN, USER_PASSWORD);
		Optional<User> userOptional = userDAO.find(loginationInfo);

		Assert.assertTrue(userOptional.isEmpty());
	}

	@Test
	public void findTest3() throws DAOException {
		UserInfo loginationInfo = new UserInfo(USER_LOGIN, NEW_PASSWORD);
		Optional<User> userOptional = userDAO.find(loginationInfo);

		Assert.assertTrue(userOptional.isEmpty());
	}

	@Test
	public void updateUserTest1() throws DAOException {
		UserInfo userInfo = new UserInfo(UPDATED_USER_ID, UPDATED_LOGIN, NEW_PASSWORD);

		Assert.assertTrue(userDAO.updateUser(userInfo));
	}

	@Test
	public void updateUserNegativeTest1() throws DAOException {
		UserInfo userInfo = new UserInfo(WRONG_ID, UPDATED_LOGIN, NEW_PASSWORD);

		Assert.assertFalse(userDAO.updateUser(userInfo));
	}

	@Test(expected = DAOException.class)
	public void updateUserNegativeTest2() throws DAOException {
		UserInfo userInfo = new UserInfo(UPDATED_USER_ID, USER_LOGIN, NEW_PASSWORD);
		userDAO.updateUser(userInfo);
	}

}
