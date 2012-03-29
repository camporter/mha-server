package myhomeaudio.server.locations;

import java.util.ArrayList;
import java.util.Iterator;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.database.object.DatabaseClient;

public class Triangulation {

	private static Triangulation instance = null;
	private ArrayList<ClientInitialization> clients;
	
	public Triangulation(){
		clients = new ArrayList<ClientInitialization>();
	}
	
	public static Triangulation getInstance(){
		if (instance == null) {
			instance = new Triangulation();
		}
		return instance;
	}
	public void addNodeConfiguration(ClientInitialization client){
		ClientInitialization clientInitial = getClientInitialization(client.getClient());
		if(clientInitial == null){
			clients.add(client);
		}else{
			removeClientInitialization(clientInitial.getClient());
			clients.add(client);
		}
		for(ClientInitialization clients : this.clients){
			
		}
	}
	
	public ClientInitialization getClientInitialization(String macAddress){
	
		for(ClientInitialization clients : this.clients){
			if(clients.isClient(macAddress)){
				return clients;
			}
		}
		return null;
	}
	
	public void removeClientInitialization(String macAddress){
		for(ClientInitialization client : this.clients){
			if(client.getClient().equals(macAddress)){
				this.clients.remove(client);
			}
		}
	}
	
	public boolean isPresent(Client client){
		return false;
	}
}
