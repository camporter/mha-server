package myhomeaudio.server.client;

import myhomeaudio.server.user.User;

/**
 * The representation for a client of the server.
 * 
 * @author Cameron
 * 
 */
public class Client {
	
	private String macAddress; 
	private String ipAddress;
	private String bluetoothName;
	private User user;
	private String sessionId;
	
	public Client(User user, String macAddress, String ipAddress, String bluetoothName) {
		this.user = new User(user);
		this.macAddress = macAddress;
		this.ipAddress = ipAddress;
		this.bluetoothName = bluetoothName;
	}
	
	public Client(String macAddress, String ipAddress, String bluetoothName) {
		this.user = null;
		this.macAddress = macAddress;
		this.ipAddress = ipAddress;
		this.bluetoothName = bluetoothName;
	}
	
	public Client(Client client) {
		this.user = client.getCurrentUser();
		this.macAddress = client.getMacAddress();
		this.ipAddress = client.getIpAddress();
		this.bluetoothName = client.getBluetoothName();
	}

	public String getIpAddress() {
		return this.ipAddress;
	}
	
	public String getMacAddress() {
		return this.macAddress;
	}
	
	public User getCurrentUser() {
		if (user == null) return null;
		return new User(user);
	}
	
	public String getBluetoothName() {
		return this.bluetoothName;
	}
	
	public void setCurrentUser(User user) {
		this.user = new User(user);
	}
}
