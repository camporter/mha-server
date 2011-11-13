package myhomeaudio.server.client;

public class Client {
	private String ipAddress; //IpAddress of client
	private String nodeIpAddress; //IpAddress of the node the client is closest to
	
	public Client(String ipAddress){
		this.ipAddress = ipAddress;
	}
	
	public void setNodeIpAddress(String nodeIpAddress){
		this.nodeIpAddress = nodeIpAddress;
	}
	
	public String getIpAddress(){
		return this.ipAddress;
	}
	public String getClosestNode(){
		return this.nodeIpAddress;
	}
}
