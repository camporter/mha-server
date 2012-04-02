package myhomeaudio.server.locations.layout;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Stores node rssi ranges, the
 * maximum and minimum values
 * obtains for a particular node
 *
 */

public class NodeSignalRange implements JSONAware{
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

	@Override
	public String toJSONString() {
		JSONObject object = new JSONObject();
		object.put("max", max);
		object.put("min", min);
		object.put("id", id);
		return object.toJSONString();
	}
}
