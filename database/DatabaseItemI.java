package database;

import java.util.ArrayList;

public interface DatabaseItemI {
	public void write();
	public int getId();
	public DatabaseTable getTable();
	public ArrayList<String> getValues();
	
}
