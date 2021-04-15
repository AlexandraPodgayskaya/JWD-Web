package by.epam.payment_system.dao.connectionpool;

import java.util.ResourceBundle;

/**
 * Gives the value of the database parameters from db.properties
 * 
 * @author Aleksandra Podgayskaya
 */
public class DBResourceManager {
	private static final String DEFAULT_BUNDLE_NAME = "db";

	/**
	 * Instance of {@link DBResourceManager}
	 */
	private static final DBResourceManager instance = new DBResourceManager();

	private ResourceBundle bundle;

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
		if (bundle == null) {
			setBundle(DEFAULT_BUNDLE_NAME);
		}
		return bundle.getString(key);
	}

	/**
	 * Set bundle
	 * 
	 * @param nameConfigurationFile {@link String} configuration file name
	 */
	public void setBundle(String nameConfigurationFile) {
		bundle = ResourceBundle.getBundle(nameConfigurationFile);
	}
}
