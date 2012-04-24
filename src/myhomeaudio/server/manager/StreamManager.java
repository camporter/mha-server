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
import myhomeaudio.server.database.object.DatabaseNode;
import myhomeaudio.server.database.object.DatabaseStream;
import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.node.Node;
import myhomeaudio.server.source.FolderSource;
import myhomeaudio.server.source.Source;
import myhomeaudio.server.stream.Stream;
import myhomeaudio.server.stream.StreamAction;
import myhomeaudio.server.stream.StreamState;

/**
 * Manages all the streams existing on the server.
 * 
 * @author Cameron
 * 
 */
public class StreamManager implements StatusCode {

	private static StreamManager instance = null;

	private ArrayList<DatabaseStream> streamList;
	private Database db;
	private ArrayList<Source> sourceList;

	protected StreamManager() {
		System.out.println("*** Starting StreamManager...");
		this.db = Database.getInstance();
		this.streamList = new ArrayList<DatabaseStream>();

		if (!checkStreamsTable() || !updateStreamsFromDB()) {
			System.exit(1); // Exit is there's a problem with the database.
		}
		initializeSources();
	}

	private void initializeSources() {
		ArrayList<Source> sourceList = new ArrayList<Source>();
		sourceList.add(new FolderSource("."));
	}

	public static synchronized StreamManager getInstance() {
		if (instance == null) {
			instance = new StreamManager();
		}
		return instance;
	}

	private synchronized boolean checkStreamsTable() {
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

	private synchronized boolean updateStreamsFromDB() {
		boolean result = false;

		this.db.lock();
		Connection conn = this.db.getConnection();
		try {
			Statement statement = conn.createStatement();

			ResultSet streamResults = statement
					.executeQuery("SELECT * FROM streams;");
			while (streamResults.next()) {
				streamList.add(new DatabaseStream(streamResults.getInt("id"),
						new Stream(streamResults.getString("name")), null));
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
	 * @param stream
	 *            Stream object representing the Stream to add.
	 * @return StatusCode which shows the outcome of the operation.
	 */
	public synchronized int addStream(Stream stream) {
		int result = STATUS_FAILED;

		if (stream != null) {
			if (getStream(stream.name()) != null) {
				// Stream with that name already exists
				result = STATUS_DUPLICATE;
			} else {
				int newId = -1;

				// Add the Stream to the database
				this.db.lock();
				Connection conn = this.db.getConnection();
				try {
					PreparedStatement pstatement = conn
							.prepareStatement("INSERT INTO streams (name) VALUES (?);");
					pstatement.setString(1, stream.name());
					pstatement.executeUpdate();

					// We want the id of the new Stream, so let's get it back
					PreparedStatement statement = conn
							.prepareStatement("SELECT id FROM streams WHERE name = ? LIMIT 1;");
					statement.setString(1, stream.name());
					ResultSet resultSet = statement.executeQuery();
					newId = resultSet.getInt("id");

					// Add the new Stream to the streamList with their new id
					this.streamList.add(new DatabaseStream(newId, stream, null));

					result = STATUS_OK;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				this.db.unlock();
			}
		}

		return result;
	}

	/**
	 * Removes a Stream from the manager.
	 * 
	 * @param stream
	 *            Stream object representing the Stream to remove.
	 * @return StatusCode which shows the outcome of the operation.
	 */
	public synchronized int removeStream(int streamId) {
		int result = STATUS_FAILED;

		DatabaseStream dbStream = getStreamById(streamId);
		if (dbStream != null) {
			this.db.lock();
			Connection conn = this.db.getConnection();
			try {
				PreparedStatement pstatement = conn
						.prepareStatement("DELETE FROM streams WHERE id = ?;");
				pstatement.setInt(1, dbStream.getId());
				pstatement.executeUpdate();

				streamList.remove(dbStream);

				result = STATUS_OK;
			} catch (SQLException e) {
				e.printStackTrace();
				result = STATUS_FAILED;
			}
			this.db.unlock();
		}

		return result;
	}

	public synchronized int setNodes(int streamId, ArrayList<Integer> nodeIds) {
		int result = STATUS_FAILED;
		NodeManager nm = NodeManager.getInstance();

		/*
		 * The list to build of INTERNAL references to DatabaseNodes in the
		 * NodeManager
		 */
		ArrayList<DatabaseNode> nodeList = new ArrayList<DatabaseNode>();

		// Validate the nodes
		for (Iterator<Integer> i = nodeIds.iterator(); i.hasNext();) {
			DatabaseNode dbNode = nm.getNodeById(i.next());
			// We don't error on an invalid id, the node may have been deleted
			// elsewhere while this is running.
			if (dbNode != null) {
				// Node is valid, add to the assigned list
				nodeList.add(dbNode);
			}
		}

		// Validate the stream
		DatabaseStream dbStream = getStreamById(streamId);
		if (dbStream != null) {
			// Stream exists, so assign to the stream the new nodes
			dbStream.setAssignedNodes(nodeList);
			result = STATUS_OK;
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
	public DatabaseStream getStream(String name) {
		// TODO: Look up streams based off their id instead! (or a combination
		// ideally)
		for (Iterator<DatabaseStream> i = this.streamList.iterator(); i
				.hasNext();) {
			DatabaseStream nextStream = i.next();
			if (nextStream.name().equals(name)) {
				return new DatabaseStream(nextStream);
			}
		}
		return null;
	}

	private DatabaseStream getStreamById(int streamId) {
		for (Iterator<DatabaseStream> i = this.streamList.iterator(); i
				.hasNext();) {
			DatabaseStream nextStream = i.next();
			if (nextStream.getId() == streamId) {
				return nextStream;
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
		Iterator<DatabaseStream> i = streamList.iterator();
		while (i.hasNext()) {
			result.add(i.next());
		}
		return result;
	}
	
	/**
	 * Performs an action on a stream (such as resume, pause, previous, next).
	 * 
	 * @param streamId The id of the stream to perform the action on.
	 * @param action The action as defined in StreamAction.
	 * @return
	 */
	public synchronized int doAction(Integer streamId, Integer action) {
		int result = STATUS_FAILED;
		
		DatabaseStream dbStream = getStreamById(streamId);
		
		// TODO: need to tell the ModeManager to actually do these actions
		switch(action) {
		case StreamAction.RESUME:
			dbStream.setCurrentState(StreamState.PLAYING);
			break;
		case StreamAction.PAUSE:
			dbStream.setCurrentState(StreamState.PAUSED);
			break;
		case StreamAction.PREVIOUS:
			// TODO: Change the current media
			break;
		case StreamAction.NEXT:
			// TODO: Change the current media
			break;
		}
		
		return result;
	}

}
