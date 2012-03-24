package myhomeaudio.server.node;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import myhomeaudio.server.Room;

/**
 * Represents a physical node.
 * 
 * @author Cameron
 * 
 */
public class Node implements JSONAware {
	private String id;
	//private Room room;
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
	
	public String getId(){
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setNodeId(String id) {
		this.id = id;
	}

	@Override
	public String toJSONString() {
		JSONObject nodeJSON = new JSONObject();
		nodeJSON.put("id", id);
		nodeJSON.put("name", name);
		return nodeJSON.toString();
	}

}
