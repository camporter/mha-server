package myhomeaudio.server.client;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import myhomeaudio.server.database.object.DatabaseClient;
import myhomeaudio.server.locations.layout.DeviceObject;
import myhomeaudio.server.node.Node;
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
	
	public Client(String macAddress, String ipAddress, String bluetoothName) {
		this.macAddress = macAddress;
		this.ipAddress = ipAddress;
		this.bluetoothName = bluetoothName;
	}
	
	public Client(Client client) {
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
	
	public String getBluetoothName() {
		return this.bluetoothName;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof Client) {
			if (((Client) obj).getBluetoothName().equals(this.bluetoothName)
					&& ((Client) obj).getMacAddress().equals(this.macAddress)
					&& ((Client) obj).getIpAddress().equals(this.ipAddress)) {
				return true;
			}
		} else if (obj instanceof DatabaseClient) {
			if (((DatabaseClient) obj).getBluetoothName().equals(this.bluetoothName)
					&& ((DatabaseClient) obj).getMacAddress().equals(this.macAddress)
					&& ((DatabaseClient) obj).getIpAddress().equals(this.ipAddress)) {
				return true;
			}
		}
		return false;
	}
}
