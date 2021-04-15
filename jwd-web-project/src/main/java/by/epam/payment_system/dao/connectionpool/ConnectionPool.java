package by.epam.payment_system.dao.connectionpool;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Pool of connections used while the system is running
 * 
 * @author Aleksandra Podgayskaya
 */
public final class ConnectionPool {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Instance of {@link ConnectionPool}
	 */
	private static final ConnectionPool instance = new ConnectionPool();
	/**
	 * Default pool size if incorrect parameter value is set in the configurations
	 */
	private static final int DEFAULT_POOL_SIZE = 5;

	/**
	 * Queue for storing free pool connections
	 */
	private BlockingQueue<Connection> connectionQueue;
	/**
	 * Queue for for accounting for the given pool connections
	 */
	private BlockingQueue<Connection> givenAwayConnectionQueue;

	/**
	 * Database driver name
	 */
	private String driverName;
	/**
	 * URL to get the database connection
	 */
	private String url;
	/**
	 * Database user name to get the database connection
	 */
	private String user;
	/**
	 * Password to get the database connection
	 */
	private String password;
	/**
	 * Number of connections to create
	 */
	private int poolSize;

	/**
	 * Get instance of this class
	 * 
	 * @return {@link ConnectionPool} instance
	 */
	public static ConnectionPool getInstance() {
		return instance;
	}

	private ConnectionPool() {
	}

	/**
	 * Initialize connection pool
	 * 
	 * @param nameConfigurationFile {@link String} the name of the configuration
	 *                              file for the pool
	 * @throws ConnectionPoolException if {@link ClassNotFoundException} or
	 *                                 {@link SQLException} occur
	 */
	public void init(String nameConfigurationFile) throws ConnectionPoolException {

		DBResourceManager dbResourceManager = DBResourceManager.getInstance();
		dbResourceManager.setBundle(nameConfigurationFile);
		driverName = dbResourceManager.getValue(DBParameter.DB_DRIVER);
		url = dbResourceManager.getValue(DBParameter.DB_URL);
		user = dbResourceManager.getValue(DBParameter.DB_USER);
		password = dbResourceManager.getValue(DBParameter.DB_PASSWORD);

		try {
			poolSize = Integer.parseInt(dbResourceManager.getValue(DBParameter.DB_POLL_SIZE));
		} catch (NumberFormatException e) {
			logger.error("pool size setting error", e);
			poolSize = DEFAULT_POOL_SIZE;
		}
		connectionQueue = new ArrayBlockingQueue<>(poolSize);
		givenAwayConnectionQueue = new ArrayBlockingQueue<>(poolSize);

		try {
			Class.forName(driverName);
			for (int i = 0; i < poolSize; i++) {
				Connection connection = DriverManager.getConnection(url, user, password);
				ConnectionWrapper connectionWrapper = new ConnectionWrapper(connection);
				connectionQueue.add(connectionWrapper);
			}
		} catch (ClassNotFoundException e) {
			throw new ConnectionPoolException("can not find database driver class", e);
		} catch (SQLException e) {
			throw new ConnectionPoolException("can not get connection", e);
		}
	}

	/**
	 * Destroy connection pool
	 * 
	 * @throws ConnectionPoolException
	 */
	public void destroy() throws ConnectionPoolException {
		clearConnectionQueue();
	}

	/**
	 * Clear connection queues
	 * 
	 * @throws ConnectionPoolException
	 */
	private void clearConnectionQueue() throws ConnectionPoolException {
		closeConnectionQueue(givenAwayConnectionQueue);
		closeConnectionQueue(connectionQueue);
	}

	/**
	 * Close connection queue
	 * 
	 * @param queue {@link BlockingQueue} of {@link Connection} queue to close
	 * @throws ConnectionPoolException if {@link SQLException} occurs
	 */
	private void closeConnectionQueue(BlockingQueue<Connection> queue) throws ConnectionPoolException {
		Connection connection;
		while ((connection = queue.poll()) != null) {
			try {
				if (!connection.getAutoCommit()) {
					connection.commit();
				}
				((ConnectionWrapper) connection).reallyClose();

			} catch (SQLException e) {
				throw new ConnectionPoolException("can not close connection queue", e);
			}
		}
	}

	/**
	 * Take a connection from the connection pool
	 * 
	 * @return {@link Connection}
	 * @throws ConnectionPoolException if {@link InterruptedException} occurs
	 */
	public Connection takeConnection() throws ConnectionPoolException {
		Connection connection;
		try {
			connection = connectionQueue.take();
			givenAwayConnectionQueue.add(connection);
		} catch (InterruptedException e) {
			throw new ConnectionPoolException("can not take connection", e);
		}
		return connection;
	}

	/**
	 * Class wrapper for connection, changed the body of method close of class
	 * Connection, add method reallyClose
	 */
	private class ConnectionWrapper implements Connection {
		private Connection connection;

		public ConnectionWrapper(Connection connection) {
			this.connection = connection;
		}

		public void reallyClose() throws SQLException {
			connection.close();
		}

		@Override
		public void close() throws SQLException {
			if (connection.isClosed()) {
				throw new SQLException("can not return closed connection");
			}

			if (connection.isReadOnly()) {
				connection.setReadOnly(false);
			}

			if (!connection.getAutoCommit()) {
				connection.rollback();
				connection.setAutoCommit(true);
			}

			if (!givenAwayConnectionQueue.remove(this)) {
				throw new SQLException("can not delete connection from the given away connections pool");
			}

			if (!connectionQueue.offer(this)) {
				throw new SQLException("can not put connection in the pool ");
			}
		}

		@Override
		public void clearWarnings() throws SQLException {
			connection.clearWarnings();
		}

		@Override
		public void commit() throws SQLException {
			connection.commit();
		}

		@Override
		public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
			return connection.createArrayOf(typeName, elements);
		}

		@Override
		public Blob createBlob() throws SQLException {
			return connection.createBlob();
		}

		@Override
		public Clob createClob() throws SQLException {
			return connection.createClob();
		}

		@Override
		public NClob createNClob() throws SQLException {
			return connection.createNClob();
		}

		@Override
		public SQLXML createSQLXML() throws SQLException {
			return connection.createSQLXML();
		}

		@Override
		public Statement createStatement() throws SQLException {
			return connection.createStatement();
		}

		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
			return connection.createStatement(resultSetType, resultSetConcurrency);
		}

		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {
			return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
			return connection.createStruct(typeName, attributes);
		}

		@Override
		public boolean getAutoCommit() throws SQLException {
			return connection.getAutoCommit();
		}

		@Override
		public String getCatalog() throws SQLException {
			return connection.getCatalog();
		}

		@Override
		public Properties getClientInfo() throws SQLException {
			return connection.getClientInfo();
		}

		@Override
		public String getClientInfo(String name) throws SQLException {
			return connection.getClientInfo(name);
		}

		@Override
		public int getHoldability() throws SQLException {
			return connection.getHoldability();
		}

		@Override
		public DatabaseMetaData getMetaData() throws SQLException {
			return connection.getMetaData();
		}

		@Override
		public int getTransactionIsolation() throws SQLException {
			return connection.getTransactionIsolation();
		}

		@Override
		public Map<String, Class<?>> getTypeMap() throws SQLException {
			return connection.getTypeMap();
		}

		@Override
		public SQLWarning getWarnings() throws SQLException {
			return connection.getWarnings();
		}

		@Override
		public boolean isClosed() throws SQLException {
			return connection.isClosed();
		}

		@Override
		public boolean isReadOnly() throws SQLException {
			return connection.isReadOnly();
		}

		@Override
		public boolean isValid(int timeout) throws SQLException {
			return connection.isValid(timeout);
		}

		@Override
		public String nativeSQL(String sql) throws SQLException {
			return connection.nativeSQL(sql);
		}

		@Override
		public CallableStatement prepareCall(String sql) throws SQLException {
			return connection.prepareCall(sql);
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public PreparedStatement prepareStatement(String sql) throws SQLException {
			return connection.prepareStatement(sql);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
			return connection.prepareStatement(sql, autoGeneratedKeys);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
			return connection.prepareStatement(sql, columnIndexes);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
			return connection.prepareStatement(sql, columnNames);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public void rollback() throws SQLException {
			connection.rollback();
		}

		@Override
		public void setAutoCommit(boolean autoCommit) throws SQLException {
			connection.setAutoCommit(autoCommit);
		}

		@Override
		public void setCatalog(String catalog) throws SQLException {
			connection.setCatalog(catalog);
		}

		@Override
		public void setClientInfo(String name, String value) throws SQLClientInfoException {
			connection.setClientInfo(name, value);
		}

		@Override
		public void setHoldability(int holdability) throws SQLException {
			connection.setHoldability(holdability);
		}

		@Override
		public void setReadOnly(boolean readOnly) throws SQLException {
			connection.setReadOnly(readOnly);
		}

		@Override
		public Savepoint setSavepoint() throws SQLException {
			return connection.setSavepoint();
		}

		@Override
		public Savepoint setSavepoint(String name) throws SQLException {
			return connection.setSavepoint(name);
		}

		@Override
		public void setTransactionIsolation(int level) throws SQLException {
			connection.setTransactionIsolation(level);
		}

		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return connection.isWrapperFor(iface);
		}

		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return connection.unwrap(iface);
		}

		@Override
		public void abort(Executor executor) throws SQLException {
			connection.abort(executor);
		}

		@Override
		public int getNetworkTimeout() throws SQLException {
			return connection.getNetworkTimeout();
		}

		@Override
		public String getSchema() throws SQLException {
			return connection.getSchema();
		}

		@Override
		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			connection.releaseSavepoint(savepoint);
		}

		@Override
		public void rollback(Savepoint savepoint) throws SQLException {
			connection.rollback();
		}

		@Override
		public void setClientInfo(Properties properties) throws SQLClientInfoException {
			connection.setClientInfo(properties);
		}

		@Override
		public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
			connection.setNetworkTimeout(executor, milliseconds);
		}

		@Override
		public void setSchema(String schema) throws SQLException {
			connection.setSchema(schema);
		}

		@Override
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
			connection.setTypeMap(map);
		}
	}
}
