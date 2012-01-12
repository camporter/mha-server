package myhomeaudio.server.user;

/**
 * The representation for a user of the server. This is the generic representation
 * of a User, not tied to the database.
 * 
 * @author Cameron
 * 
 */
public class User {

	protected String username;
	protected String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(User user) {
		if (user != null) {
			this.username = user.getUsername();
			this.password = user.getPassword();
		}
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}
}
