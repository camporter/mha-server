package myhomeaudio.server.node;

import myhomeaudio.server.Room;

/**
 * Represents a physical node.
 * @author Cameron
 *
 */
public class Node {
	private Room room;
	private String ipAddress;
	private String bluetoothName;
	
	
	public Node(String ipAddress, String bluetoothName) {
		this.ipAddress = ipAddress;
		this.bluetoothName = bluetoothName;
		//this.nodeID = nodeCount;
		//this.room = room;
	}
	
	/**
	 * IpAddress getter
	 * @return ipAddress
	 * 		Returns ipAddress of node as a string
	 */
	public String getIpAddress()
	{
		return this.ipAddress;
	}
	
	public String getBluetoothName() {
		return this.bluetoothName;
	}
	
	public void setBluetoothName(String name) {
		this.bluetoothName = name;
	}
	
}
