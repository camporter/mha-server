package myhomeaudio.server.node;

import myhomeaudio.server.Room;

/**
 * Represents a physical node.
 * @author Cameron
 *
 */
public class Node {
	Room room;
	String ipAddress;
	
	public Node(String ipAddress) {
		this.ipAddress = ipAddress;
		//this.room = room;
	}
	
	public String getIpAddress()
	{
		return this.ipAddress;
	}
}
