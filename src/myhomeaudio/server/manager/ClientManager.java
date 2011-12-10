package myhomeaudio.server.manager;

import java.util.ArrayList;

import myhomeaudio.server.client.Client;

public class ClientManager {
	private static ClientManager instance = null;
	private static int clientCount = 0;
	private ArrayList<Client> clientList = new ArrayList<Client>();
	
	protected ClientManager() {
		
	}

	public static synchronized ClientManager getInstance() {
		return (instance == null) ? (new ClientManager()) : instance;
	}
	
	//TODO add remove
	public synchronized void addClient(Client client){
		clientList.add(client);
		clientCount++;
	}
	
	public synchronized Client getClient() {
		return clientList.get(0);
	}
}
