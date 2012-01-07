package myhomeaudio.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;

public class Database extends ReentrantLock {

	/*
	 * The single Database object instance. Since one individual Connection
	 * locks SQLite, we only want one Database.
	 */
	private static Database instance = null;

	private String databaseName = "database.db";
	private Connection connection;

	/**
	 * Connects to the database through JDBC using the SQLite driver. The
	 * constructor is protected to prevent any other instances of the Database
	 * object from being created.
	 * 
	 */
	protected Database() {
		super();

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// Library unable to load
			e.printStackTrace();
			System.err.println("SQLite library not found!");
			System.exit(1);
		}
		
		connect();
	}
	
	private void connect() {
		try {
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.databaseName);
		} catch (SQLException e) {
			// Can't access the database!
			e.printStackTrace();
			System.err.println("Unable to access the database file: " + this.databaseName);
			System.exit(1);
		}
	}

	/**
	 * Gets the Database singleton instance. This method should be the only way
	 * to get access to the Database object.
	 * 
	 * @return The Database instance.
	 */
	public static synchronized Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

	/**
	 * Gets the Connection object for the database. The Database instance must
	 * first be locked for the thread requesting the Connection.
	 * 
	 * @return The Connection instance, or null if the Database isn't held by
	 *         the thread.
	 */
	public Connection getConnection() {
		if (this.isHeldByCurrentThread()) {
			return this.connection;
		} else {
			return null;
		}
	}
}
