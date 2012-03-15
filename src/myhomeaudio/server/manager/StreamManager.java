package myhomeaudio.server.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
			statement
					.executeUpdate("CREATE TABLE IF NOT EXISTS "
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

			ResultSet streamResults = statement
					.executeQuery("SELECT * FROM streams;");
			while (streamResults.next()) {
				streamList.add(new Stream(streamResults.getString("name"),
						streamResults.getInt("id")));
			}
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.db.unlock();

		return result;
	}

	/**
	 * Adds a Stream to the StreamManager.
	 * 
	 * @param stream Stream object representing the Stream to add.
	 * @return StatusCode which shows the outcome of the operation.
	 */
	public int addStream(Stream stream) {
		int result = STATUS_FAILED;
		
		if (stream != null && getStream(stream.name()) != null) {
			// Stream with that name already exists
			result = STATUS_REG_DUPLICATE;
		} else {
			int newId = -1;
			
			// Add the Stream to the database
			this.db.lock();
			Connection conn = this.db.getConnection();
			try {
				PreparedStatement pstatement = conn.prepareStatement("INSERT INTO streams (name) VALUES (?);");
				pstatement.setString(1, stream.name());
				pstatement.executeUpdate();
				
				// We want the id of the new Stream, so let's get it back
				PreparedStatement statement = conn.prepareStatement("SELECT id FROM streams WHERE name = ? LIMIT 1;");
				statement.setString(1, stream.name());
				ResultSet resultSet = statement.executeQuery();
				newId = resultSet.getInt("id");
				
				// Add the new Stream to the streamList with their new id
				this.streamList.add(new Stream (newId, stream));
				
				result = STATUS_OK;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.db.unlock();
		}
		
		return result;
	}
	
	/**
	 * Removes a Stream from the manager.
	 * 
	 * @param stream Stream object representing the Stream to remove.
	 * @return StatusCode which shows the outcome of the operation.
	 */
	public int removeStream(Stream stream) {
		int result = STATUS_FAILED;
		
		
		if (stream != null) {
			Stream dbStream = getStream(stream.name());
			if (dbStream != null) {
				this.db.lock();
				Connection conn = this.db.getConnection();
				try {
					PreparedStatement pstatement = conn.prepareStatement("DELETE FROM streams WHERE id = ?;");
					pstatement.setInt(1, dbStream.id());
					pstatement.executeUpdate();
					
					streamList.remove(dbStream);
					
					result = STATUS_OK;
				} catch (SQLException e) {
					e.printStackTrace();
					result = STATUS_FAILED;
				}
				this.db.unlock();
			}
		}
		
		return result;
	}

	/**
	 * Gets the Stream object associated with the given stream.
	 * 
	 * @param name
	 *            Name assigned to the stream to find.
	 * @return Returns the respective Stream, or null if a stream with this name
	 *         doesn't exist.
	 */
	public Stream getStream(String name) {
		for (Iterator<Stream> i = this.streamList.iterator(); i.hasNext();) {
			Stream nextStream = i.next();
			if (nextStream.name().equals(name)) {
				return new Stream(nextStream);
			}
		}
		return null;
	}

	/**
	 * Gets the list of Stream objects as a string.
	 * 
	 * @return JSON String representing an Stream array.
	 */
	public JSONArray getListJSON() {
		JSONArray result = new JSONArray();

		// TODO: retest this way!
		// result.addAll(streamList);

		// Other way to do it...
		Iterator<Stream> i = streamList.iterator();
		while (i.hasNext()) {
			result.add(i.next());
		}
		return result;
	}

}
