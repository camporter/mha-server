package myhomeaudio.server.database;

import java.util.ArrayList;

/**
 * Represents each table in a database.
 * 
 * Please note that this class assumes that it exists as a folder with a schema
 * file.
 * 
 * @author Cameron
 * 
 */
public class DatabaseTable {

	private DatabaseTableSchema schema;
	private ArrayList<DatabaseItem> items;
	private Database database;
	private String name;

	/**
	 * Constructor that populates the table with its schema and items
	 * 
	 * @param tableName
	 */
	public DatabaseTable(String tableName) {
		this.database = Database.getInstance();
		this.name = tableName;

		// Get the schema from the file
		this.schema = this.database.readSchema(tableName);

		// Get the items from their files
		this.items = this.database.getTableItems(this);
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Adds an item to the table.
	 * 
	 * @param item
	 * @return
	 */
	public DatabaseItem insert(ArrayList<String> values) {
		DatabaseItem item = new DatabaseItem(this, this.nextId(), values);
		this.database.insertIntoTable(item);
		return item;
	}

	public void update(DatabaseItem item) {

	}

	public int nextId() {
		// the next id is always one greater than the last id, which is the
		// length.
		return this.items.size();
	}
}
