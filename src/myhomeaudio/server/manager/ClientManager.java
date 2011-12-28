package myhomeaudio.server.manager;

import java.util.ArrayList;

import myhomeaudio.server.client.Client;

public class ClientManager {

	private static ClientManager instance = null;
	private static int clientCount = 0;
	private ArrayList<Client> clientList;

	protected ClientManager() {
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
		clientCount++;
	}

	public synchronized Client getClient() {
		return clientList.get(0);
	}
}
