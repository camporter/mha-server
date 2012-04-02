package myhomeaudio.server.locations.layout;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import myhomeaudio.server.node.Node;

/**
 * 
 * 
 *
 */
public class NodeSignalBoundary implements JSONAware {

	private final int id; // node within room
	private ArrayList<NodeSignalRange> foundNodes;

	public NodeSignalBoundary(int i) {
		this.foundNodes = new ArrayList<NodeSignalRange>();
		this.id = i;
	}

	public NodeSignalBoundary(int id, ArrayList<NodeSignalRange> nsr) {
		this.foundNodes = nsr;
		this.id = id;
	}

	public boolean addNodeRange(NodeSignalRange nodeSignalRange) {
		if (!(id == nodeSignalRange.getNodeId())) {
			if (!containsNode(nodeSignalRange.getNodeId())) {
				foundNodes.add(nodeSignalRange);
				return true;
			}
		}
		return false;
	}

	public NodeSignalRange getNodeRange(int id){
		Iterator<NodeSignalRange> i = foundNodes.iterator();
		NodeSignalRange signal;
		while(i.hasNext()){
			signal = i.next();
			if(signal.getNodeId() == id){
				return signal;
			}
		}
		return null;
	}
	
	public static ArrayList<NodeSignalRange> parseNodeSignals(String data){
		ArrayList<NodeSignalRange> interference = new ArrayList<NodeSignalRange>();
		Object o = (Object) JSONValue.parse(data);
		JSONArray jArray = (JSONArray) (o);
		// NodeSignalRange[] nsr = (NodeSignalRange[]) jArray.toArray();
		JSONObject jObject;
		for (Object object : jArray) {
			jObject = (JSONObject) object;
			interference.add(new NodeSignalRange((Integer) jObject.get("id"), ((Long) jObject
					.get("min")).intValue(), ((Long) jObject.get("max")).intValue()));
		}
		return interference;
	}

	private boolean containsNode(int id) {
		Iterator iterate = foundNodes.iterator();
		while (iterate.hasNext()) {
			if (((NodeSignalRange) iterate.next()).getNodeId() == id) {
				return true;
			}
		}
		return false;
	}

	public int getNodeId() {
		return id;
	}

	public int get() {
		return foundNodes.size();
	}
	

	@Override
	public String toJSONString() {
		JSONObject object = new JSONObject();
		object.put("id", id);
		
		JSONArray array = new JSONArray();
		Iterator i = foundNodes.iterator();
		while(i.hasNext()){
			array.add(i.next());
		}
		object.put("foundNodes", array);
		return object.toJSONString();
	}
}
