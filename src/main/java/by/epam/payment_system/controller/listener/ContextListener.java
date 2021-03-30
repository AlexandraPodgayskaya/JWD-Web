package by.epam.payment_system.controller.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;

public class ContextListener implements ServletContextListener {

	private static final Logger logger = LogManager.getLogger();
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			ConnectionPool.getInstance().init();
		} catch (ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Error in ConnectionPool.init",e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		try {
			ConnectionPool.getInstance().destroy();
		} catch (ConnectionPoolException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Error in ConnectionPool.destroy",e);
		}
	}
}
