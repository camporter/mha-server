package database;

import java.util.ArrayList;


/**
 * Represents each item in a database table
 * @author Cameron
 *
 */
public class DatabaseItem implements DatabaseItemI {
	
	protected ArrayList<String> values;
	
	protected DatabaseTable table;
	
	/**
	 * Existing database item, populate the object
	 * @param id
	 */
	
	public DatabaseItem(DatabaseTable table, int id, ArrayList<String> values)
	{
		this.table = table;
		this.values = values;
	}
	
	/**
	 * Write any changes to the item to the database
	 */
	public void write()
	{
		return;
	}
	
	/**
	 * Returns the id of the item in its table
	 */
	public int getId()
	{
		return Integer.parseInt(this.values.get(0));
	}
	
	/**
	 * Returns the DatabaseTable this item is in
	 */
	public DatabaseTable getTable()
	{
		return this.table;
	}
	
	public ArrayList<String> getValues() {
		return new ArrayList<String>(values);
	}
}
