package myhomeaudio.server.client;

public class Client {
	private String ipAddress; //IpAddress of client
	private String bluetoothName;
	private String currentSong;
	
	public Client(String ipAddress){
		this.ipAddress = ipAddress;
		this.bluetoothName = "";
	}
	
	/*public void setNodeIpAddress(String nodeIpAddress){
		this.nodeIpAddress = nodeIpAddress;
	}*/
	
	public String getIpAddress(){
		return this.ipAddress;
	}
	
	public String getClosestNodeName(){
		return this.bluetoothName;
	}
	
	public void setClosestNodeName(String newBluetoothName) {
		this.bluetoothName = newBluetoothName;
	}
	
	public String getCurrentSong() {
		return this.currentSong;
	}
	
	public void setCurrentSong(String song) {
		this.currentSong = song;
	}
}
