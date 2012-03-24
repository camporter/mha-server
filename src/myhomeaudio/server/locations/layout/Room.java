package myhomeaudio.server.locations.layout;

import java.util.ArrayList;
import java.util.Iterator;

import myhomeaudio.server.locations.DeviceObject;
import myhomeaudio.server.node.Node;

/**
 * 
 * 
 *
 */
public class Room {
	private final String id;
	private ArrayList<NodeSignalRange> interference;
	
	public Room(String id){
		this.interference = new ArrayList<NodeSignalRange>();
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
