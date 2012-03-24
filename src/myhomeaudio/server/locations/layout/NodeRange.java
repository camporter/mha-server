package myhomeaudio.server.locations.layout;

public class NodeRange {
	private final String id;
	private final int min;
	private final int max;
	
	public NodeRange(String id, int min, int max){
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
	
	public String getNodeId(){
		return id;
	}
}
