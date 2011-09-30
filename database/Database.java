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
 * Class that takes care of all database operations.
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

	private String databaseFolder = "db"; // hardcode this for now

	private ArrayList<DatabaseTable> tables;

	// Read/write locks
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock read = readWriteLock.readLock();
	private final Lock write = readWriteLock.writeLock();

	public Database() {
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
	 * Writes an item to its table
	 * 
	 * @param item
	 *            The item to insert into it's associated table.
	 * @return
	 */
	public boolean insertIntoTable(DatabaseItem item) {
		write.lock();
		try {

		} finally {
			write.unlock();
		}
		return false;
	}

	/**
	 * Removes an item from its table
	 * 
	 * @param item
	 *            Item to be removed.
	 * @return
	 */
	public boolean removeFromTable(DatabaseItem item) {
		write.lock();
		try {

		} finally {
			write.unlock();
		}
		return false;
	}

	/**
	 * Read a table's schema
	 */
	public ArrayList<String> readSchema(String tableName) {
		ArrayList<String> result = new ArrayList<String>();

		read.lock();
		try {
			File file = new File(this.databaseFolder + "/" + tableName
					+ "/schema");

			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
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
