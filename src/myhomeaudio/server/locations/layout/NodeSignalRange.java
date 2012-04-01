package myhomeaudio.server.locations.layout;

/**
 * Stores node rssi ranges, the
 * maximum and minimum values
 * obtains for a particular node
 *
 */

public class NodeSignalRange {
	private final int id; //node id
	private final int min;
	private final int max;
	
	public NodeSignalRange(int id, int min, int max){
		this.id = id;
		this.min = min;
		this.max = max;
	}
	
	public boolean checkRange(int value){
		if(value >= min && value <= max){
			return true;
		}
		return false;
	}
	
	public int getNodeId(){
		return id;
	}
}
