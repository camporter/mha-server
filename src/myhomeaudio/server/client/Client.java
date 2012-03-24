package myhomeaudio.server.client;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import myhomeaudio.server.locations.DeviceObject;
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
	private ArrayList<DeviceObject> location;//current location data
	
	
	
	
	public Client(User user, String macAddress, String ipAddress, String bluetoothName) {
		this.user = new User(user);
		this.macAddress = macAddress;
		this.ipAddress = ipAddress;
		this.bluetoothName = bluetoothName;
		this.location = null;
	}
	
	public Client(String macAddress, String ipAddress, String bluetoothName) {
		this.user = null;
		this.macAddress = macAddress;
		this.ipAddress = ipAddress;
		this.bluetoothName = bluetoothName;
		this.location = null;
	}
	
	public Client(Client client) {
		this.user = client.getCurrentUser();
		this.macAddress = client.getMacAddress();
		this.ipAddress = client.getIpAddress();
		this.bluetoothName = client.getBluetoothName();
		this.location = null;
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
	
	public String getLocations(){
		JSONArray jArray = new JSONArray();
		while(!location.isEmpty()){
			jArray.add(location.remove(0).toJSONString());
		}
		return jArray.toJSONString();
	}
	
	public boolean updateLocations(String location){
		this.location = new ArrayList<DeviceObject>();
		Object object = JSONValue.parse(location);
		JSONArray jArray = (JSONArray)object;
		JSONObject jObject;
		while(!jArray.isEmpty()){
			jObject = (JSONObject) jArray.remove(0);
			this.location.add(new DeviceObject((String)jObject.get("id"), ((Long)jObject.get("rssi")).intValue()));
		}	
		return true;
	}
}
