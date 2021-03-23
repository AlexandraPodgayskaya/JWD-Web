package by.epam.payment_system.controller.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import by.epam.payment_system.dao.connectionpool.ConnectionPool;
import by.epam.payment_system.dao.connectionpool.ConnectionPoolException;

public class ContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			ConnectionPool.getInstance().init();
		} catch (ConnectionPoolException e) {
			// log
			throw new RuntimeException("Error in ConnectionPool.init",e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		try {
			ConnectionPool.getInstance().destroy();
		} catch (ConnectionPoolException e) {
			//log
			throw new RuntimeException("Error in ConnectionPool.destroy",e);
		}
	}
}
