package myhomeaudio.server.node;

import myhomeaudio.server.Room;

/**
 * Represents a physical node.
 * 
 * @author Cameron
 * 
 */
public class Node {
	private int id;
	private Room room;
	private String ipAddress;
	private String name;

	public Node(String ipAddress, String name) {
		this.ipAddress = ipAddress;
		this.name = name;
		// this.nodeID = nodeCount;
		// this.room = room;
	}

	/**
	 * IpAddress getter
	 * 
	 * @return ipAddress Returns ipAddress of node as a string
	 */
	public String getIpAddress() {
		return this.ipAddress;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setNodeId(int id) {
		this.id = id;
	}

}
