package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import by.epam.payment_system.dao.AdditionalClientDataDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.UserInfo;

public class SQLAdditionalClientDataDAOTest {

	private static final String NAME_CONFIGURATION_FILE = "db_test";

	private static final String SELECT_NEW_CLIENT_DATA_SQL = "SELECT * FROM CLIENT_DETAILS WHERE ID='2'";
	private static final String NEW_SURNAME = "Куксар";
	private static final String NEW_NAME = "Мария";
	private static final String NEW_PATRONYMIC = "Романовна";
	private static final String NEW_DATE_OF_BIRTH = "01.04.1992";
	private static final String NEW_PERSONAL_NUMBER_PASSPORT = "4020492B059PB7";
	private static final String NEW_PHONE = "+375298955566";
	private static final String USER_SURNAME = "Подгайская";
	private static final String USER_NAME = "Александра";
	private static final String USER_PATRONYMIC = "Романовна";
	private static final String USER_DATE_OF_BIRTH = "02.04.1992";
	private static final String USER_PERSONAL_NUMBER_PASSPORT = "4020492B058PB8";
	private static final String USER_PHONE = "+375298136643";
	private static final String WRONG_PERSONAL_NUMBER_PASSPORT = "1111111B111PB1";
	private static final String PERSONAL_NUMBER_PASSPORT = "1234567B111PB1";
	private static final Long WRONG_ID = 0L;
	private static final Long USER_ID = 1L;
	private static final Long UPDATE_ID = 2L;
	private static final Long NEW_ID = 3L;

	private static final AdditionalClientDataDAO additionalClientDataDAO = DAOFactory.getInstance()
			.getAdditionalClientDataDAO();

	private UserInfo userInfo;

	@BeforeClass
	public static void connectionPoolInit() throws ConnectionPoolException {
		ConnectionPool.getInstance().init(NAME_CONFIGURATION_FILE);
	}

	@Before
	public void initUserInfo() {
		userInfo = new UserInfo();
		userInfo.setId(USER_ID);
		userInfo.setSurname(USER_SURNAME);
		userInfo.setName(USER_NAME);
		userInfo.setPatronymic(USER_PATRONYMIC);
		userInfo.setDateBirth(USER_DATE_OF_BIRTH);
		userInfo.setPersonalNumberPassport(USER_PERSONAL_NUMBER_PASSPORT);
		userInfo.setPhone(USER_PHONE);
	}

	@AfterClass
	public static void connectionPoolDestroy() throws ConnectionPoolException {
		ConnectionPool.getInstance().destroy();
	}

	@Test
	public void createTest() throws DAOException, SQLException, ConnectionPoolException {
		UserInfo additionalClientInfo = new UserInfo();
		additionalClientInfo.setId(NEW_ID);
		additionalClientInfo.setSurname(NEW_SURNAME);
		additionalClientInfo.setName(NEW_NAME);
		additionalClientInfo.setPatronymic(NEW_PATRONYMIC);
		additionalClientInfo.setDateBirth(NEW_DATE_OF_BIRTH);
		additionalClientInfo.setPersonalNumberPassport(NEW_PERSONAL_NUMBER_PASSPORT);
		additionalClientInfo.setPhone(NEW_PHONE);
		additionalClientDataDAO.create(additionalClientInfo);

		try (Connection connection = ConnectionPool.getInstance().takeConnection();
				Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(SELECT_NEW_CLIENT_DATA_SQL);
			Assert.assertTrue(resultSet.next());
		}
	}

	@Test(expected = DAOException.class)
	public void createNegativeTest() throws DAOException {
		additionalClientDataDAO.create(userInfo);

	}

	@Test
	public void findDataByPassportTest() throws DAOException {
		UserInfo expected = userInfo;
		UserInfo actual = additionalClientDataDAO.findDataByPassport(USER_PERSONAL_NUMBER_PASSPORT).get();

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void findDataByPassportTest2() throws DAOException {
		Optional<UserInfo> userOptional = additionalClientDataDAO.findDataByPassport(WRONG_PERSONAL_NUMBER_PASSPORT);

		Assert.assertTrue(userOptional.isEmpty());
	}

	@Test
	public void findDataByIdTest() throws DAOException {
		UserInfo expected = userInfo;
		UserInfo actual = additionalClientDataDAO.findDataById(USER_ID).get();

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void findDataByIdTest2() throws DAOException {
		Optional<UserInfo> userOptional = additionalClientDataDAO.findDataById(WRONG_ID);

		Assert.assertTrue(userOptional.isEmpty());
	}

	@Test
	public void updateTest() throws DAOException {
		UserInfo userData = new UserInfo();
		userData.setId(UPDATE_ID);
		userData.setSurname(USER_SURNAME);
		userData.setName(NEW_NAME);
		userData.setPatronymic(USER_PATRONYMIC);
		userData.setDateBirth(NEW_DATE_OF_BIRTH);
		userData.setPersonalNumberPassport(PERSONAL_NUMBER_PASSPORT);
		userData.setPhone(USER_PHONE);

		Assert.assertTrue(additionalClientDataDAO.update(userInfo));
	}

}
