package by.epam.payment_system.controller.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;

/**
 * Application listener
 * 
 * @author Aleksandra Podgayskaya
 * @see ServletContextListener
 */
public class ContextListener implements ServletContextListener {

	private static final Logger logger = LogManager.getLogger();
	private static final String NAME_CONFIGURATION_FILE = "db";

	/**
	 * Initialization connection pool
	 * 
	 * @param servletContextEvent {@link ServletContextEvent}
	 * @throws RuntimeException if {@link ConnectionPoolException} occurs
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			ConnectionPool.getInstance().init(NAME_CONFIGURATION_FILE);
			logger.info("connection pool is initialized with configuration file name " + NAME_CONFIGURATION_FILE);
		} catch (ConnectionPoolException e) {
			logger.error("connection pool initialization error", e);
			throw new RuntimeException("connection pool initialization error", e);
		}
	}

	/**
	 * Destroy connection pool
	 * 
	 * @param servletContextEvent {@link ServletContextEvent}
	 * @throws RuntimeException if {@link ConnectionPoolException} occurs
	 */
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		try {
			ConnectionPool.getInstance().destroy();
		} catch (ConnectionPoolException e) {
			logger.error("connection pool destruction error", e);
			throw new RuntimeException("connection pool destruction error", e);
		}
	}
}
