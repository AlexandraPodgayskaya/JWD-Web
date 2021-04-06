package by.epam.payment_system.dao;

public interface CardTypeDAO {

	void create(String cardType, String imagePath) throws DAOException;

}
