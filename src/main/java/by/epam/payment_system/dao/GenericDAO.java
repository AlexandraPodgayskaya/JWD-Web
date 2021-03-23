package by.epam.payment_system.dao;

public interface GenericDAO<T1, T2, K> {
	void create(T1 entity) throws DAOException;
	void update(T1 entity) throws DAOException;
	void delete(K key) throws DAOException;
	T2 find(T1 entity) throws DAOException;
	Integer findId(K key) throws DAOException;
}
