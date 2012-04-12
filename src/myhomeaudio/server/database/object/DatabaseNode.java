package myhomeaudio.server.database.object;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import myhomeaudio.server.node.Node;

public class DatabaseNode extends DatabaseObject<Node> implements JSONAware {
	
	boolean isActive;

	public DatabaseNode(int id, String name, String ipAddress, String bluetoothAddress) {
		super(id, new Node(name, ipAddress, null));
		isActive = false;
	}

	public DatabaseNode(int id, Node node) {
		super(id, new Node(node));
		isActive = false;
	}

	public DatabaseNode(DatabaseNode dbNode) {
		super(dbNode.getId(), new Node(dbNode.getName(), dbNode.getIpAddress(),
				dbNode.getBluetoothAddress()));
		isActive = false;
	}

	public String getIpAddress() {
		return this.object.getIpAddress();
	}

	public String getName() {
		return this.object.getName();
	}

	public String getBluetoothAddress() {
		return this.object.getBluetoothAddress();
	}
	
	public void setName(String name){
		this.object.setName(name);
	}
	
	public boolean isActive(){
		return isActive;
	}
	public void setActive(boolean active){
		isActive = active;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DatabaseNode [id=" + id + ", object=" + object + "]";
	}

	public Node getNode(){
		return new Node(this.object);
	}

	@Override
	public String toJSONString() {
		JSONObject nodeJSON = new JSONObject();
		nodeJSON.put("id", id);
		nodeJSON.put("name", this.object.getName());
		nodeJSON.put("ipaddress", this.object.getIpAddress());
		nodeJSON.put("bluetoothaddress", this.object.getBluetoothAddress());
		return nodeJSON.toString();
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof Node) {
			if (((Node) obj).getName().equals(this.object.getName())
					&& ((Node) obj).getIpAddress().equals(this.object.getIpAddress())
					&& ((Node) obj).getBluetoothAddress().equals(this.object.getBluetoothAddress())) {
				return true;
			}
		} else if (obj instanceof DatabaseNode) {
			if (((DatabaseNode) obj).getName().equals(this.object.getName())
					&& ((DatabaseNode) obj).getIpAddress().equals(this.object.getIpAddress())
					&& ((DatabaseNode) obj).getBluetoothAddress().equals(
							this.object.getBluetoothAddress())) {
				return true;
			}
		}
		return false;
	}
}
