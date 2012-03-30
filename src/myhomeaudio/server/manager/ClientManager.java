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
import myhomeaudio.server.locations.layout.DeviceObject;
import myhomeaudio.server.locations.layout.NodeSignalBoundary;

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

	/**
	 * Retrieve an instance of the ClientManager.
	 * 
	 * @return The ClientManager.
	 */
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
		this.clientList.add(new DatabaseClient(newID, client, sessionId, false));
		return sessionId;
	}

	/**
	 * Gets the DatabaseClient object associated with the given client id.
	 * 
	 * @param id
	 * @return Returns a DatabaseClient object, or null if the id doesn't match
	 *         any existing client.
	 */
	// TODO changed visibilty for clientHelper
	public synchronized DatabaseClient getClient(int id) {
		for (Iterator<DatabaseClient> i = this.clientList.iterator(); i.hasNext();) {
			DatabaseClient nextClient = i.next();
			if (nextClient.getId() == id) {
				return new DatabaseClient(nextClient);
			}
		}
		return null;
	}

	private synchronized DatabaseClient getClientBySession(String sessionId) {
		for (Iterator<DatabaseClient> i = this.clientList.iterator(); i.hasNext();) {
			DatabaseClient nextClient = i.next();
			if (nextClient.getSessionId().equals(sessionId)) {
				return nextClient;
			}
		}
		return null;
	}

	/**
	 * Gets the DatabaseClient object associated with the given session id.
	 * 
	 * @param sessionId
	 *            Session id for a client assigned by the ClientManager.
	 * @return Returns a DatabaseClient object, or null if the session id
	 *         doesn't match any existing client.
	 */
	public synchronized DatabaseClient getClient(String sessionId) {
		DatabaseClient databaseClient = getClientBySession(sessionId);
		if (databaseClient != null) {
			return new DatabaseClient(databaseClient);
		}
		return null;
	}

	public boolean isValidClient(String sessionId) {
		if (getClient(sessionId) != null)
			return true;
		return false;
	}

	/**
	 * Changes the client initialization configuration, which includes the node
	 * signatures for each node.
	 * 
	 * @param sessionId
	 *            The session given to the client we want to set the
	 *            initialization for.
	 * @param nodeSignatures
	 *            The list of NodeSignalBoundaries, which describe what ranges
	 *            are seen around the area for each node.
	 * @return Whether the operation completed successfully.
	 */
	public boolean changeClientInitialization(String sessionId,
			ArrayList<NodeSignalBoundary> nodeSignatures) {
		DatabaseClient databaseClient = getClientBySession(sessionId);
		databaseClient.setNodeSignatures(nodeSignatures);
		
		return updateClientToDatabase(databaseClient);
	}

	/**
	 * Updates the location of the client.
	 * 
	 * @param sessionId
	 *            The session given to the client we want to set the location
	 *            for.
	 * @param devices
	 *            A DeviceObject list that provides the id and signal strength
	 *            for any nodes seen.
	 * @return Whether the operation completed successfully.
	 */
	public synchronized boolean updateClientLocation(String sessionId, ArrayList<DeviceObject> devices) {
		DatabaseClient databaseClient = getClientBySession(sessionId);
		
		return updateClientToDatabase(databaseClient);
	}
	
	/**
	 * Logs out a client.
	 * 
	 * @param sessionId
	 *            Corresponding session id for the client to remove.
	 * @return Whether the removal succeeded.
	 */
	public synchronized boolean logoutClient(String sessionId) {
		DatabaseClient databaseClient = getClientBySession(sessionId);
		databaseClient.logout();
		return updateClientToDatabase(databaseClient);
	}
	
	/**
	 * Updates the client in the database.
	 * @param dbClient The DatabaseClient to use for updating.
	 * @return Whether the operation succeeded.
	 */
	private boolean updateClientToDatabase(DatabaseClient dbClient) {
		boolean result = false;
		
		this.db.lock();
		Connection conn = this.db.getConnection();
		try {
			PreparedStatement pstatement = conn.prepareStatement("UPDATE TABLE clients SET "
					+ "ipaddress = ?, "
					+ "macaddress = ?, "
					+ "bluetoothname = ?, "
					+ "userid = ? "
					+ "WHERE id = ?;");
			pstatement.setString(1, dbClient.getIpAddress());
			pstatement.setString(2, dbClient.getMacAddress());
			pstatement.setString(3, dbClient.getBluetoothName());
			pstatement.setInt(4, dbClient.getLoggedInUserId());
			pstatement.setInt(5, dbClient.getId());
			pstatement.executeUpdate();
			
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.db.unlock();
		return result;
	}

	/**
	 * Creates a session id that will be unique to a specific client.
	 * <p>
	 * It uses the SHA-512 hash function on certain fields of the Client. It
	 * also uses the current timestamp.
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
