package myhomeaudio.server.locations;

import java.util.ArrayList;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.locations.layout.NodeSignalBoundary;

public class ClientInitialization {
	//TODO use something other than macAddress of client
	private final String clientMac;
	private ArrayList<NodeSignalBoundary> nodeSignalBoundaries;
	
	public ClientInitialization(String clientMac, ArrayList<NodeSignalBoundary> nodeSignalBoundaries){
		this.clientMac = clientMac;
		this.nodeSignalBoundaries = nodeSignalBoundaries;
	}
	
	public boolean updateRoomConfig(ArrayList<NodeSignalBoundary> nodeSignalBoundaries){
		if(nodeSignalBoundaries == null)
			return false;
		this.nodeSignalBoundaries = nodeSignalBoundaries;
		return true;
	}
	
	public boolean isClient(String clientMac){
		return this.clientMac.equals(clientMac);
	}
	
	public String getClient(){
		return clientMac;
	}
}
