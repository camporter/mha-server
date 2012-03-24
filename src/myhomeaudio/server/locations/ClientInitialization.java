package myhomeaudio.server.locations;

import java.util.ArrayList;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.locations.layout.Room;

public class ClientInitialization {
	//TODO use something other than macAddress of client
	private final String clientMac;
	private ArrayList<Room> rooms;
	
	public ClientInitialization(String clientMac, ArrayList<Room> rooms){
		this.clientMac = clientMac;
		this.rooms = rooms;
	}
	
	public boolean updateRoomConfig(ArrayList<Room> rooms){
		if(rooms == null)
			return false;
		this.rooms = rooms;
		return true;
	}
	
	public boolean isClient(String clientMac){
		return this.clientMac.equals(clientMac);
	}
	
	public String getClient(){
		return clientMac;
	}
}
