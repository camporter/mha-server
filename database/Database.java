package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class that takes care of all database file operations.
 * 
 * The Database object keeps record of its database tables. It communicates with
 * the file I/O to write or read files as needed. There should only be one
 * instance of the database object in the program. Each thread can use the
 * methods here safely, using locks. Some methods need a write lock, since we
 * want the database to remain uncorrupted.
 * 
 * @author Cameron
 * 
 */
public class Database {

	private String databaseFolder;

	private ArrayList<DatabaseTable> tables;

	// Read/write locks
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock read = readWriteLock.readLock();
	private final Lock write = readWriteLock.writeLock();

	/**
	 * Populates the database with its tables from the stored folders/files.
	 */
	public Database(String databaseFolder) {
		this.databaseFolder = databaseFolder;
		
		tables = new ArrayList<DatabaseTable>();

		// Find all table folders and create table objects based off of them
		File dbFolder = new File(this.databaseFolder);
		File[] fileList = dbFolder.listFiles();
		for (File f : fileList) {
			if (f.isDirectory()) {
				DatabaseTable table = new DatabaseTable(this, f.getName());
				
				// Keep record of the table in the database
				this.tables.add(table);
			}
		}
	}

	public boolean createTable(String tableName, ArrayList<String> schema) {
		return false;
	}

	/**
	 * Writes an item to its table at the file level.
	 * 
	 * @param item
	 *            The item to insert into it's associated table.
	 * @return Whether the operation succeeded.
	 */
	public boolean insertIntoTable(DatabaseItem item) {
		write.lock();
		try {
			// The item has its own file in its table's folder
			File file = new File(this.databaseFolder + "/"
					+ item.getTable().getName() + "/" + item.getId());

			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));

				try {
					// Each value is written to its own line
					String output = "";
					for (String value : item.getValues()) {
						output += value;
						output += "\n";
					}
					writer.write(output);
				} finally {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

		} finally {
			write.unlock();
		}
		return false;
	}

	/**
	 * Removes an item from its table at the file level.
	 * 
	 * @param item
	 *            Item to be removed from its table.
	 * @return Whether the operation succeeded.
	 */
	public boolean removeFromTable(DatabaseItem item) {
		write.lock();
		try {
			File file = new File(this.databaseFolder + "/"
					+ item.getTable().getName() + "/" + item.getId());
			return file.delete();
		} finally {
			write.unlock();
		}
	}

	/**
	 * Updates an item in a table at the file level.
	 * 
	 * @param item
	 *            Item to be updated in its table.
	 * @return Whether the operation succeeded.
	 */
	public boolean updateItem(DatabaseItem item) {
		if (this.removeFromTable(item) && this.insertIntoTable(item)) {
			return true;
		}
		return false;
	}

	public ArrayList<DatabaseItem> getTableItems(DatabaseTable table)
	{
		ArrayList<DatabaseItem> result = new ArrayList<DatabaseItem>();
		
		read.lock();
		try {
			File tableFolder = new File(this.databaseFolder + "/" + table.getName());
			File[] itemFileList = tableFolder.listFiles();
			for (File f : itemFileList)
			{
				System.out.println(f.getName());
				if (f.isFile() && !f.getName().equals("schema"))
				{
					
					int id = Integer.parseInt(f.getName());
					DatabaseItem item = new DatabaseItem(table, id, this.readItem(table, id));
					result.add(item);
				}
			}
		}
		finally {
			read.unlock();
		}
		return result;
	}
	
	/**
	 * Read an item's values given the object
	 * @param item The item we want the values from.
	 * @return An ArrayList of the values.
	 */
	public ArrayList<String> readItem(DatabaseItem item)
	{
		return this.readItem(item.getTable(), item.getId());
	}
	
	/**
	 * Read an item's values given the table and its id.
	 * @param table
	 * @param itemId
	 * @return
	 */
	public ArrayList<String> readItem(DatabaseTable table, int itemId)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		read.lock();
		try {
			File itemFile = new File(this.databaseFolder + "/" + table.getName()
					+ "/" + itemId);

			try {
				BufferedReader reader = new BufferedReader(new FileReader(itemFile));
				try {
					// Each line in the item file is a defined value
					String line = null;

					while ((line = reader.readLine()) != null) {
						// Add more values as we go
						result.add(line);
					}
				} finally {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} finally {
			read.unlock();
		}
		return result;
	}
	
	
	/**
	 * Read a table's schema
	 */
	public ArrayList<String> readSchema(String tableName) {
		ArrayList<String> result = new ArrayList<String>();

		read.lock();
		try {
			File schemaFile = new File(this.databaseFolder + "/" + tableName
					+ "/schema");

			try {
				BufferedReader reader = new BufferedReader(new FileReader(schemaFile));
				try {
					// Each line in the schema file is a defined column
					String line = null;

					while ((line = reader.readLine()) != null) {
						// Add more columns as we go
						result.add(line);
					}
				} finally {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} finally {
			read.unlock();
		}
		return result;
	}

	/**
	 * Write a table's schema
	 */
	public void writeSchema(String tableName, ArrayList<String> schema) {
		write.lock();
		try {
			File file = new File(this.databaseFolder + "/" + tableName
					+ "/schema");

			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));

				try {
					String output = "";
					for (String line : schema) {
						output += line;
						output += "\n";
					}
					writer.write(output);
				} finally {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} finally {
			write.unlock();
		}
		return;
	}
}
