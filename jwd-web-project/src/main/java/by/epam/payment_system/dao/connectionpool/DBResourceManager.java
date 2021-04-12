package by.epam.payment_system.dao.connectionpool;

import java.util.ResourceBundle;

/**
 * Gives the value of the database parameters from db.properties
 * 
 * @author Aleksandra Podgayskaya
 */
public class DBResourceManager {
	private static final String BUNDLE_NAME = "db";

	/**
	 * Instance of {@link DBResourceManager}
	 */
	private static final DBResourceManager instance = new DBResourceManager();

	private ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * Get instance of this class
	 * 
	 * @return {@link DBResourceManager} instance
	 */
	public static DBResourceManager getInstance() {
		return instance;
	}

	/**
	 * Get parameter value by key
	 * 
	 * @param key {@link String} parameter name
	 * @return {@link String} parameter value
	 */
	public String getValue(String key) {
		return bundle.getString(key);
	}
}
