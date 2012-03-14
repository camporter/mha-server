package myhomeaudio.server.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import myhomeaudio.server.database.Database;
import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.stream.Stream;

/**
 * Manages all the streams existing on the server.
 * 
 * @author Cameron
 * 
 */
public class StreamManager implements StatusCode {

	private static StreamManager instance = null;

	private ArrayList<Stream> streamList;
	private Database db;

	protected StreamManager() {
		System.out.println("*** Starting StreamManager...");
		this.db = Database.getInstance();
		this.streamList = new ArrayList<Stream>();
		
		if (!checkStreamsTable() || !updateStreamsFromDB()) {
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
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS "
					+ "streams (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT );");
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

			ResultSet streamResults = statement.executeQuery("SELECT * FROM streams;");
			while (streamResults.next()) {
				streamList.add(new Stream(streamResults.getInt("id"), streamResults
						.getString("name")));
			}
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.db.unlock();

		return result;
	}

	public JSONArray getListJSON() {
		JSONArray result = new JSONArray();
		Iterator<Stream> i = streamList.iterator();
		while (i.hasNext()) {
			result.add(i.next());
		}
		return result;
	}

}
