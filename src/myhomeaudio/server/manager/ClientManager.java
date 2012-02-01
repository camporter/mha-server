package myhomeaudio.server.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.codec.digest.DigestUtils;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.database.Database;
import myhomeaudio.server.database.object.DatabaseClient;

/**
 * Stores and maintains all of the clients on the server. This object maintains
 * the clients table in the database.
 * 
 * @author Cameron
 * 
 */
public class ClientManager {

	private static ClientManager instance = null;

	private ArrayList<DatabaseClient> clientList;
	private Database db;

	protected ClientManager() {
		System.out.println("*** Starting ClientManager...");
		this.db = Database.getInstance();
		this.clientList = new ArrayList<DatabaseClient>();

		if (!checkClientsTable()) {
			System.exit(1); // Exit if there's a problem with the database
		}
	}

	public static synchronized ClientManager getInstance() {
		if (instance == null) {
			instance = new ClientManager();
		}
		return instance;
	}

	private boolean checkClientsTable() {
		boolean result = false;

		// Make sure table exists, create it if it doesn't
		this.db.lock();
		Connection conn = this.db.getConnection();
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS "
					+ "clients (id INTEGER PRIMARY KEY AUTOINCREMENT, " + "ipaddress TEXT, "
					+ "macaddress TEXT UNIQUE, " + "bluetoothname TEXT UNIQUE, "
					+ "userid INTEGER);");
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.db.unlock();
		return result;
	}

	/**
	 * Adds a client to the server.
	 * 
	 * @param client
	 *            The Client object representing the client to add.
	 * @return The session id assigned to the client for their new session.
	 */
	public synchronized String addClient(Client client) {
		int newID = -1; // TODO: Store in the database eventually? We don't
						// necessarily need to save the clients though.
		String sessionId = generateSessionId(client);
		this.clientList.add(new DatabaseClient(newID, client, sessionId));
		return sessionId;
	}

	/**
	 * Gets the DatabaseClient object associated with the given client id.
	 * 
	 * @param id
	 * @return Returns a DatabaseClient object, or null if the id doesn't match
	 *         any existing client.
	 */
	public synchronized DatabaseClient getClient(int id) {
		for (Iterator<DatabaseClient> i = this.clientList.iterator(); i.hasNext();) {
			DatabaseClient nextClient = i.next();
			if (nextClient.getId() == id) {
				return new DatabaseClient(nextClient);
			}
		}
		return null;
	}

	/**
	 * Creates a session id that will be unique to a specific client using
	 * SHA-512. It also uses the current timestamp.
	 * 
	 * @param client
	 *            The client to generate the session id for.
	 * @return The unique session id.
	 */
	private String generateSessionId(Client client) {
		return DigestUtils.sha512Hex(client.getMacAddress() + client.getBluetoothName()
				+ (new Timestamp(new Date().getTime())).toString());
	}
}
