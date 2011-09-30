package database;
import java.util.ArrayList;

/**
 * Represents each table in a database
 * @author Cameron
 *
 */
public class DatabaseTable {
	private ArrayList<String> schema;
	private ArrayList<String> items;
	private Database database;
	private String name;
	
	public DatabaseTable(Database db, String tableName)
	{
		this.name = tableName;
		this.schema = new ArrayList<String>();
		this.database = db;
	}
	
}
