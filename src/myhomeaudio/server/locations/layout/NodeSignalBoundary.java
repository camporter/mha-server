package myhomeaudio.server.locations.layout;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import myhomeaudio.server.node.Node;

/**
 * 
 * 
 *
 */
public class NodeSignalBoundary {
	private final String id; //node within room
	private ArrayList<NodeSignalRange> interference;
	
	public NodeSignalBoundary(String id){
		this.interference = new ArrayList<NodeSignalRange>();
		this.id = id;
	}
	
	public NodeSignalBoundary(String id, ArrayList<NodeSignalRange> nsr){
		this.interference = nsr;
		this.id = id;
	}
	
	public boolean addNodeRange(NodeSignalRange nodeSignalRange){
		if(!id.equals(nodeSignalRange.getNodeId())){
			if(!containsNode(nodeSignalRange.getNodeId())){
				interference.add(nodeSignalRange);
				return true;
			}
		}
		return false;
	}
	
	public static ArrayList<NodeSignalRange> parseNodeSignals(String data){
		ArrayList<NodeSignalRange> interference = new ArrayList<NodeSignalRange>();
		Object o = (Object)JSONValue.parse(data);
		JSONArray jArray = (JSONArray)(o);
		//NodeSignalRange[] nsr = (NodeSignalRange[]) jArray.toArray();
		JSONObject jObject;
		for(Object object : jArray){
			jObject = (JSONObject)object;
			interference.add(new NodeSignalRange((String)jObject.get("id"), ((Long)jObject.get("min")).intValue(), ((Long)jObject.get("max")).intValue()));
		}
		return interference;
	}
	
	private boolean containsNode(String id){
		Iterator iterate = interference.iterator();
		while(iterate.hasNext()){
			if(((NodeSignalRange)iterate.next()).getNodeId().equals(id)){
				return true;
			}
		}	
		return false;
	}
	
	public String getNodeId(){
		return id;
	}
	
	public int get(){
		return interference.size();
	}
}
