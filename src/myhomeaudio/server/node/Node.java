package myhomeaudio.server.node;

/**
 * Represents a physical node.
 * 
 * @author Cameron
 * 
 */
public class Node {

	private String ipAddress;
	private String name;

	public Node(String ipAddress, String name) {
		this.ipAddress = ipAddress;
		this.name = name;
	}

	public Node(Node node) {
		this.ipAddress = node.getIpAddress();
		this.name = getName();
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
}
