package myhomeaudio.server.database.object;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import myhomeaudio.server.node.Node;

public class DatabaseNode extends DatabaseObject<Node> implements JSONAware {
	
	public DatabaseNode(int id, String ipAddress, String name) {
		super(id, new Node(ipAddress, name));
	}
	
	public DatabaseNode(int id, Node node) {
		super(id, new Node(node));
	}
	
	public DatabaseNode(DatabaseNode dbNode) {
		super(dbNode.getId(), new Node(dbNode.getIpAddress(), dbNode.getName()));
	}
	
	public String getIpAddress() {
		return this.object.getIpAddress();
	}
	
	public String getName() {
		return this.object.getName();
	}
	
	@Override
	public String toJSONString() {
		JSONObject nodeJSON = new JSONObject();
		nodeJSON.put("id", id);
		nodeJSON.put("name", this.object.getName());
		nodeJSON.put("ipaddress", this.object.getIpAddress());
		return nodeJSON.toString();
	}
	
}
