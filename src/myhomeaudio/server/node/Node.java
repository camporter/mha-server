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
	int nodeID;
	
	private static int nodeCount;
	
	public Node(String ipAddress) {
		this.ipAddress = ipAddress;
		this.nodeID = ++nodeCount;
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
	
	/**
	 * nodeID getter
	 * @return nodeID
	 * 		Returns the nodeID of the specific node
	 */
	public int getnodeID()
	{
		return this.nodeID;
	}
	
	/**
	 * nodeCount getter
	 * @return nodeCount
	 * 		Returns the total number of nodes within the network
	 */
	public static int getNodeCount()
	{
		return nodeCount;
	}
}
