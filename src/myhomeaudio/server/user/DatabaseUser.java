package myhomeaudio.server.user;

/**
 * The Database representation for a user, and the one that UserManager uses
 * internally to keep track of users.
 * 
 * @author Cameron
 * 
 */
public class DatabaseUser extends User {

	private int id;
	private boolean loggedIn;

	public DatabaseUser(int id, String username, String password) {
		super(username, password);
		this.id = id;
	}

	public DatabaseUser(int id, User user) {
		super(user);
		this.id = id;
	}

	public DatabaseUser(DatabaseUser user) {
		super(user);
		this.id = user.getId();
	}

	public int getId() {
		return this.id;
	}

	public boolean isLoggedIn() {
		return this.loggedIn;
	}

	public void setLoggedIn() {
		this.loggedIn = true;
	}

	public void setLoggedOut() {
		this.loggedIn = false;
	}
}
