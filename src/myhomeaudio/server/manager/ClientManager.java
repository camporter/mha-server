package myhomeaudio.server.manager;

import java.util.ArrayList;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.database.Database;

/**
 * Stores and maintains all of the clients on the server. This object maintains
 * the clients table in the database.
 * 
 * @author Cameron
 * 
 */
public class ClientManager {

	private static ClientManager instance = null;

	private ArrayList<Client> clientList;

	private Database db;

	protected ClientManager() {
		System.out.println("*** Starting ClientManager...");
		clientList = new ArrayList<Client>();
	}

	public static synchronized ClientManager getInstance() {
		if (instance == null) {
			instance = new ClientManager();
		}
		return instance;
	}

	// TODO add remove
	public synchronized void addClient(Client client) {

		this.clientList.add(client);
	}

	public synchronized Client getClient() {
		return clientList.get(0);
	}
}
