package myhomeaudio.server.node;

import myhomeaudio.server.Room;

/**
 * Represents a physical node.
 * @author Cameron
 *
 */
public class Node {
	int id;
	Room room;
	String ipAddress;
	
	public Node(int nodeId) {
		this.id = nodeId;
		//this.room = room;
	}
	
	/**
	 * Gets the node's associated id.
	 * @return
	 */
	public int getId() {
		return this.id;
	}
	public String getIpAddress()
	{
		return this.ipAddress;
	}
}
