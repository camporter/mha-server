package myhomeaudio.server.database.object;

/**
 * Represents any objects that exist in the Database.
 * 
 * @author Cameron
 *
 */
public class DatabaseObject<T> {
	protected int id;
	protected T object;
	
	public DatabaseObject(int id, T object) {
		this.id = id;
		this.object = object;
	}
	
	public DatabaseObject(DatabaseObject<T> object) {
		this.id = object.getId();
		this.object = object.getObject();
	}
	
	public int getId() {
		return this.id;
	}
	
	protected T getObject() {
		return object;
	}
}
