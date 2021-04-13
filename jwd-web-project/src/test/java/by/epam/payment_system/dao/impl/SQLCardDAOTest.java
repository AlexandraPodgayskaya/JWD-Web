package by.epam.payment_system.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import by.epam.payment_system.dao.CardDAO;
import by.epam.payment_system.dao.DAOException;
import by.epam.payment_system.dao.DAOFactory;
import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;
import by.epam.payment_system.entity.Card;
import by.epam.payment_system.entity.CardStatus;
import by.epam.payment_system.entity.CardType;

public class SQLCardDAOTest {

	private static final String NAME_CONFIGURATION_FILE = "db_test";

	private static final String NEW_NUMBER_CARD = "5489333344441114";
	private static final String NUMBER_CARD = "5489333344441111";
	private static final String UPDATED_NUMBER_CARD = "5489333344441113";
	private static final String OWNER_2_NUMBER_CARD = "5489333344441112";
	private static final String NUMBER_ACCOUNT = "21081001";
	private static final String IMAGE_PATH = "img/visa.png";
	private static final String CARD_TYPE = "Visa Classic";
	private static final Long LAST_CARD_NUMBER = 5489333344441114L;
	private static final Integer CARD_TYPE_ID = 1;
	private static final Integer OWNER_ID_1 = 1;
	private static final Integer OWNER_ID_2 = 2;
	private static final String SELECT_NEW_CARD_SQL = "SELECT * FROM CARDS WHERE NUMBER_CARD='5489333344441114'";
	private static final CardDAO cardDAO = DAOFactory.getInstance().getCardDAO();

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
		Card card = new Card(NEW_NUMBER_CARD, NUMBER_ACCOUNT, new CardType(CARD_TYPE_ID), CardStatus.ADDITIONAL,
				OWNER_ID_1);
		cardDAO.create(card);

		try (Connection connection = ConnectionPool.getInstance().takeConnection();
				Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(SELECT_NEW_CARD_SQL);
			Assert.assertTrue(resultSet.next());
		}
	}

	@Test
	public void findCardDataTest() throws DAOException {
		Card expected = new Card(NUMBER_CARD, NUMBER_ACCOUNT, new CardType(CARD_TYPE_ID), CardStatus.MAIN, OWNER_ID_1,
				Boolean.FALSE, Boolean.FALSE);
		Card actual = cardDAO.findCardData(NUMBER_CARD).get();

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void findCardsTest() throws DAOException {
		List<Card> expected = Arrays.asList(new Card(OWNER_2_NUMBER_CARD, NUMBER_ACCOUNT,
				new CardType(CARD_TYPE, IMAGE_PATH), CardStatus.ADDITIONAL, OWNER_ID_2, Boolean.FALSE, Boolean.FALSE));
		List<Card> actual = cardDAO.findCards(OWNER_ID_2);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void getLastCardNumberTest() throws DAOException {
		Long expected = LAST_CARD_NUMBER;
		Long actual = cardDAO.getLastCardNumber().get();

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void updateBlockingTest() throws DAOException {
		Card card = new Card(UPDATED_NUMBER_CARD, Boolean.TRUE);

		Assert.assertTrue(cardDAO.updateBlocking(card));
	}

	@Test
	public void setClosedTest() throws DAOException {
		Assert.assertTrue(cardDAO.setClosed(UPDATED_NUMBER_CARD));
	}
}
