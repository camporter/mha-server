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
	public boolean addNodeConfiguration(){
		
		return true;
	}
	
	public ClientInitialization getClientInitialization(Client client){
		for (Iterator<ClientInitialization> i = this.clients.iterator(); 
			i.hasNext();) {
			ClientInitialization checkClient = i.next();
		if (checkClient.getClient()) {
			return new DatabaseClient(nextClient);
		}}
	}
	public boolean isPresent(Client client){
		
	}
}
