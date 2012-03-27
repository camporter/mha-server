package myhomeaudio.server.database.object;

import java.util.ArrayList;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.node.Node;
import myhomeaudio.server.user.User;

public class DatabaseClient extends DatabaseObject<Client> {
	
	private String sessionId;
	private Node closestNode;
	
	public DatabaseClient(int id, Client client, String sessionId) {
		super(id, new Client(client));
		this.sessionId = sessionId;
		this.closestNode = null;
	}

	public DatabaseClient(DatabaseClient dbClient) {
		super(dbClient.getId(), new Client(dbClient.getCurrentUser(), dbClient.getMacAddress(),
				dbClient.getIpAddress(), dbClient.getBluetoothName()));
		this.sessionId = dbClient.getSessionId();
		this.closestNode = dbClient.getClosestNode();
	}
	
	public String getSessionId() {
		return this.sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getIpAddress() {
		return this.object.getIpAddress();
	}

	public String getMacAddress() {
		return this.object.getMacAddress();
	}

	public String getBluetoothName() {
		return this.object.getBluetoothName();
	}

	public User getCurrentUser() {
		return this.object.getCurrentUser();
	}

	public void setCurrentUser(User user) {
		this.object.setCurrentUser(user);
	}
	
	public Node getClosestNode(){
		return closestNode;
	}
	public void updateLocation(Node node) {
		this.closestNode = node;
	}

}
