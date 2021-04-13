package by.epam.payment_system.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import by.epam.payment_system.dao.AdditionalClientDataDAO;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;

public class SQLAdditionalClientDataDAOTest {

	private static final String NAME_CONFIGURATION_FILE = "db_test";

	private static final AdditionalClientDataDAO clientDataDAO = DAOFactory.getInstance().getAdditionalClientDataDAO();

	@BeforeClass
	public static void connectionPoolInit() throws ConnectionPoolException {
		ConnectionPool.getInstance().init(NAME_CONFIGURATION_FILE);
	}

	@AfterClass
	public static void connectionPoolDestroy() throws ConnectionPoolException {
		ConnectionPool.getInstance().destroy();
	}
}
