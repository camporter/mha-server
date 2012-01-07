package myhomeaudio.server.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import myhomeaudio.server.database.Database;

/**
 * Stores and manages all of the users on the server. This object maintains the
 * users table in the Database.
 * 
 * @author Cameron
 * 
 */
public class UserManager {

	/*
	 * The userList stores all the current users in memory. This should always
	 * match what is in the database. ArrayList indexes DO NOT match the id of
	 * the DatabaseUser! (use getId instead)
	 */
	private ArrayList<DatabaseUser> userList;
	Database db;

	// Status codes for user registration
	public static final int REGISTER_OK = 0;
	public static final int REGISTER_FAILED = 1;
	public static final int REGISTER_BAD_PASSWORD = 2;
	public static final int REGISTER_DUPLICATE_USERNAME = 3;

	public UserManager() {
		this.db = Database.getInstance();
		this.userList = new ArrayList<DatabaseUser>();

		if (!checkUsersTable() || !updateUsersFromDB()) {
			System.exit(1); // Exit if the table doesn't exist or we can't
							// update?
		}

	}

	private boolean checkUsersTable() {
		boolean result = false;

		// Make sure the table exists, create it if it doesn't
		this.db.lock();
		Connection conn = this.db.getConnection();
		try {
			Statement statement = conn.createStatement();
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT);");
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.db.unlock();

		return result;
	}

	private boolean updateUsersFromDB() {
		boolean result = false;

		this.db.lock();
		Connection conn = this.db.getConnection();
		try {
			Statement statement = conn.createStatement();

			// Create each DatabaseUser object using rows from the users table
			ResultSet userResults = statement.executeQuery("SELECT * FROM users;");
			while (userResults.next()) {
				DatabaseUser dbUser = new DatabaseUser(userResults.getInt("id"),
						userResults.getString("username"), userResults.getString("password"));
				// Populate the userList
				this.userList.add(dbUser);
			}
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.db.unlock();

		return result;
	}

	/**
	 * Registers a new user to the server. Checks to make sure the username
	 * doesn't already exist, and that the password is valid.
	 * 
	 * @param user
	 *            The User object representing the user to add.
	 * @return Registration status code. See the static fields for UserManager.
	 */
	public int registerUser(User user) {
		if (getUser(user.getUsername()) != null) {
			return UserManager.REGISTER_DUPLICATE_USERNAME;
		} else if (user.getPassword().length() < 0) {
			/*
			 * TODO: Set password requirements somewhere sane and do further
			 * checking here.
			 */
			return UserManager.REGISTER_BAD_PASSWORD;
		} else {
			int newId = -1;

			// Add the new user to the database
			this.db.lock();
			Connection conn = this.db.getConnection();
			try {
				PreparedStatement pstatement = conn
						.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?);");
				pstatement.setString(1, user.getUsername());
				pstatement.setString(2, user.getPassword());
				pstatement.executeUpdate();

				// We want the id of the new user, so let's get it back
				PreparedStatement statement = conn
						.prepareStatement("SELECT id FROM users WHERE username = ? LIMIT 1;");
				statement.setString(1, user.getUsername());
				ResultSet resultSet = statement.executeQuery();
				newId = resultSet.getInt("id");
			} catch (SQLException e) {
				e.printStackTrace();
				return UserManager.REGISTER_FAILED;
			}
			this.db.unlock();

			// Add the registered user to the userList with their id
			this.userList.add(new DatabaseUser(newId, user));
			return UserManager.REGISTER_OK;
		}
	}

	public boolean loginUser(User user) {
		return false;

	}

	public boolean logoutUser(String username) {
		return false;

	}

	/**
	 * Get a list of users currently logged in.
	 * 
	 * @return
	 */
	public ArrayList<DatabaseUser> getLoggedInUsers() {
		return null;
	}

	/**
	 * Gets the User object that resides in the userList which corresponds with
	 * the User object being given.
	 * 
	 * @param user
	 */
	/*private getMatchingUser(User user) {
		for (Iterator<DatabaseUser> i = this.userList.iterator(); i.hasNext();) {
			User nextUser = i.next();
			if (nextUser.getUsername().equals(user.getUsername())
					&& nextUser.getPassword().equals(user.getPassword())) {
				// Usernames match
				return nextUser;
			}
		}
	}*/

	/**
	 * Gets the User object associated with the given user id.
	 * 
	 * @param id
	 * @return Returns a DatabaseUser object, or null if the id doesn't match any
	 *         existing user.
	 */
	public DatabaseUser getUser(int id) {
		for (Iterator<DatabaseUser> i = this.userList.iterator(); i.hasNext();) {
			DatabaseUser nextUser = i.next();
			if (nextUser.getId() == id) {
				// User id match!
				return new DatabaseUser(nextUser);
			}
		}
		return null;
	}

	/**
	 * Gets the User object associated with the given username.
	 * 
	 * @param username
	 * @return Returns a DatabaseUser object, or null if the username doesn't match any
	 *         existing user.
	 */
	public DatabaseUser getUser(String username) {
		for (Iterator<DatabaseUser> i = this.userList.iterator(); i.hasNext();) {
			DatabaseUser nextUser = i.next();
			if (nextUser.getUsername().equals(username)) {
				// Username match!
				return new DatabaseUser(nextUser);
			}
		}
		return null;
	}
}
