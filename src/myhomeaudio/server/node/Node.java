package myhomeaudio.server.node;

import myhomeaudio.server.database.object.DatabaseNode;

/**
 * Represents a physical node.
 * 
 * @author Cameron
 * 
 */
public class Node {

	private String ipAddress;
	private String name;
	private String bluetoothAddress;

	public Node(String name, String ipAddress, String bluetoothAddress) {
		this.ipAddress = ipAddress;
		this.name = name;
		this.bluetoothAddress = bluetoothAddress;
	}

	public Node(Node node) {
		this.ipAddress = node.getIpAddress();
		this.name = node.getName();
		this.bluetoothAddress = node.getBluetoothAddress();
	}

	/**
	 * IpAddress getter
	 * 
	 * @return ipAddress Returns ipAddress of node as a string
	 */
	public String getIpAddress() {
		return this.ipAddress;
	}

	public String getBluetoothAddress() {
		return this.bluetoothAddress;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof Node) {
			if (((Node) obj).getName().equals(this.name)
					&& ((Node) obj).getIpAddress().equals(this.ipAddress)
					&& ((Node) obj).getBluetoothAddress().equals(this.bluetoothAddress)) {
				return true;
			}
		} else if (obj instanceof DatabaseNode) {
			if (((DatabaseNode) obj).getName().equals(this.name)
					&& ((DatabaseNode) obj).getIpAddress().equals(this.ipAddress)
					&& ((DatabaseNode) obj).getBluetoothAddress().equals(this.bluetoothAddress)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Node [ipAddress=" + ipAddress + ", name=" + name
				+ ", bluetoothAddress=" + bluetoothAddress + "]";
	}
}
