package myhomeaudio.server.database;

import java.util.Hashtable;

import myhomeaudio.server.database.field.DatabaseField;

/**
 * Represents a table's schema.
 * 
 * Please note that this class matches with an existing schema file for a table.
 * Any modification of these objects will be reflected in the database.
 * 
 * @author Cameron
 * 
 */
public class DatabaseTableSchema {

	private Database database;
	private DatabaseTable table;

	private Hashtable<String, DatabaseField> fields = new Hashtable<String, DatabaseField>();

	public DatabaseTableSchema(DatabaseTable table) {
		this.database = Database.getInstance();
		this.table = table;
	}

}
