package database;


/**
 * Represents each item in a database table
 * @author Cameron
 *
 */
public class DatabaseItem {
	
	protected DatabaseTable table;
	
	/**
	 * New database item
	 * @param table
	 */
	public DatabaseItem(DatabaseTable table)
	{
		this.table = table;
		
	}
	/**
	 * Existing database item, populate the object
	 * @param id
	 */
	public DatabaseItem(int id)
	{
		
	}
	
	/**
	 * Write any changes to the item to the database
	 */
	public void write()
	{
		return;
	}
	
}
