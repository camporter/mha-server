package myhomeaudio.server.database;

import java.util.ArrayList;

public interface DatabaseItemI {

	public int getId();

	public DatabaseTable getTable();

	public ArrayList<String> getValues();

}
