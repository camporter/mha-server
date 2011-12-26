package myhomeaudio.server.database;

import java.util.ArrayList;

public class DBTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			Database db = Database.getInstance();
			ArrayList<DatabaseTable> tables = db.getTables();
			System.out.println(tables);
			
			
	}

}
