package myhomeaudio.server.database.object;

import org.json.simple.JSONObject;

import myhomeaudio.server.user.User;

/**
 * The Database representation for a user, and the one that UserManager uses
 * internally to keep track of users.
 * 
 * @author Cameron
 * 
 */
public class DatabaseUser extends DatabaseObject<User> {

	private boolean loggedIn;

	public DatabaseUser(int id, String username, String password) {
		super(id, new User(username, password));
	}

	public DatabaseUser(int id, User user) {
		super(id, new User(user));
	}

	public DatabaseUser(DatabaseUser dbUser) {
		super(dbUser.getId(), new User(dbUser.getUsername(), dbUser.getPassword()));
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
	
	public String getUsername() {
		return this.object.getUsername();
	}
	
	public String getPassword() {
		return this.object.getPassword();
	}
	
	public JSONObject toJSONObject(){
		JSONObject object = new JSONObject();
		object.put("id", this.getId());
		object.put("name", this.object.getUsername());
		object.put("loggedIn", this.loggedIn);
		return object;
	}
}
