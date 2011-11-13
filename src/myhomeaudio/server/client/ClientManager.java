package myhomeaudio.server.client;

import java.util.ArrayList;

import myhomeaudio.server.client.Client;

public class ClientManager {
	private static ClientManager instance = null;
	private ArrayList<Client> clientList = new ArrayList<Client>();
	
	protected ClientManager() {
		
	}

	public static synchronized ClientManager getInstance() {
		return (instance == null) ? (new ClientManager()) : instance;
	}
	public synchronized void addClient(Client client){
		clientList.add(client);
	}
}
