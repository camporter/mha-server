package myhomeaudio.server.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import myhomeaudio.server.database.Database;
import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.stream.StreamBase;

/**
 * Manages all the streams existing on the server.
 * 
 * @author Cameron
 * 
 */
public class StreamManager implements StatusCode {

	private static StreamManager instance = null;

	private ArrayList<StreamBase> streamList;
	private Database db;

	protected StreamManager() {
		System.out.println("*** Starting StreamManager...");
		this.db = Database.getInstance();
		this.streamList = new ArrayList<StreamBase>();

		if (!checkStreamsTable() && !updateStreamsFromDB()) {
			System.exit(1); // Exit is there's a problem with the database.
		}
	}

	public static synchronized StreamManager getInstance() {
		if (instance == null) {
			instance = new StreamManager();
		}
		return instance;
	}

	private boolean checkStreamsTable() {
		boolean result = false;

		// Make sure table exists, create it if it doesn't
		this.db.lock();
		Connection conn = this.db.getConnection();
		try {
			Statement statement = conn.createStatement();
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS "
							+ "streams (id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER );");
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.db.unlock();
		return result;
	}

	private boolean updateStreamsFromDB() {
		boolean result = false;

		this.db.lock();
		Connection conn = this.db.getConnection();
		try {
			Statement statement = conn.createStatement();

			ResultSet streamResults = statement
					.executeQuery("SELECT * FROM streams;");
			while (streamResults.next()) {
				switch (streamResults.getInt("type")) {
				case 0:
				default:
					break;
				}

			}
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.db.unlock();

		return result;
	}

}
