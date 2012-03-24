package myhomeaudio.server.locations.layout;

import java.util.ArrayList;
import java.util.Iterator;

import myhomeaudio.server.locations.DeviceObject;
import myhomeaudio.server.node.Node;

public class NodeBoundary {
	private final String id;
	private ArrayList<NodeRange> interference;
	
	public NodeBoundary(String id){
		this.interference = new ArrayList<NodeRange>();
		this.id = id;
	}
	
	public boolean addNodeRange(NodeRange nodeRange){
		if(!id.equals(nodeRange.getNodeId())){
			if(!containsNode(nodeRange.getNodeId())){
				interference.add(nodeRange);
				return true;
			}
		}
		return false;
	}
	
	private boolean containsNode(String id){
		Iterator iterate = interference.iterator();
		while(iterate.hasNext()){
			if(((NodeRange)iterate.next()).getNodeId().equals(id)){
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
