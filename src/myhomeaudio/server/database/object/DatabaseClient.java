package myhomeaudio.server.database.object;

import java.util.ArrayList;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.locations.layout.NodeSignalBoundary;
import myhomeaudio.server.node.Node;

public class DatabaseClient extends DatabaseObject<Client> {
	
	private String sessionId;
	private Node closestNode;
	
	private int loggedUserId;
	
	private ArrayList<NodeSignalBoundary> nodeSignatures;
	
	public DatabaseClient(int id, Client client, String sessionId) {
		super(id, new Client(client));
		this.sessionId = sessionId;
		this.closestNode = null;
		this.loggedUserId = -1; // Set logged out
		this.nodeSignatures = null; // Starts off as null
	}

	public DatabaseClient(DatabaseClient dbClient) {
		super(dbClient.getId(), new Client(dbClient.getMacAddress(),
				dbClient.getIpAddress(), dbClient.getBluetoothName()));
		this.sessionId = dbClient.getSessionId();
		this.closestNode = dbClient.getClosestNode();
		this.loggedUserId = dbClient.getLoggedInUserId();
	}
	
	public String getSessionId() {
		return this.sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public int getLoggedInUserId() {
		return loggedUserId;
	}
	
	public void login(int userId) {
		loggedUserId = userId;
	}
	
	public int logout() {
		int result = loggedUserId;
		loggedUserId = -1;
		return result;
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
	
	public Node getClosestNode() {
		return closestNode;
	}
	
	public void updateLocation(Node node) {
		closestNode = new Node(node);
	}

	public ArrayList<NodeSignalBoundary> getNodeSignatures() {
		return new ArrayList<NodeSignalBoundary>(nodeSignatures);
	}
	
	public void setNodeSignatures(ArrayList<NodeSignalBoundary> nodeSignatures) {
		this.nodeSignatures = new ArrayList<NodeSignalBoundary>(nodeSignatures);
	}
}
