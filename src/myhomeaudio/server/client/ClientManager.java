package myhomeaudio.server.client;

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
	
	/** Updates Client object's closest node ipAddress
	 * 
	 * @param ipAddress
	 * 		ipAddress of client to be updates
	 * @param nodeIpAddress
	 * 		ipAddress of node closest to client
	 */
	public synchronized void updateClosestNode(String ipAddress, String nodeIpAddress){
		int i=0;
		while(i<clientCount){
			if(clientList.get(i) != null){
				if(clientList.get(i).getIpAddress().equals(ipAddress)){
					clientList.get(i).setNodeIpAddress(nodeIpAddress);
					i=clientCount;
				}
			}
			i++;
		}
	}
}
