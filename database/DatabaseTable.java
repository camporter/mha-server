package database;
import java.util.ArrayList;

/**
 * Represents each table in a database
 * @author Cameron
 *
 */
public class DatabaseTable {
	private ArrayList<String> schema;
	private ArrayList<DatabaseItem> items;
	private Database database;
	private String name;
	
	/**
	 * Constructor that populates the table with its schema and items
	 * @param db
	 * @param tableName
	 * @param schema
	 */
	public DatabaseTable(Database db, String tableName)
	{
		this.database = db;
		this.name = tableName;
		
		// Get the schema from the file
		this.schema = this.database.readSchema(tableName);
		
		
		// Get the items from their files
		this.items = this.database.getTableItems(this);
	}
	
	public String getName()
	{
		return this.name;
	}
}
